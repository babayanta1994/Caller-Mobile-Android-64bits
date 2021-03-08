package ru.true_ip.trueip.di.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.true_ip.trueip.api.HLMApi;
import ru.true_ip.trueip.gson.adapters.ImagesFieldAdapter;
import ru.true_ip.trueip.models.responses.ClaimModel;
import ru.true_ip.trueip.repository.ApiControllerWithReactivation;
import ru.true_ip.trueip.repository.RepositoryController;


@Module(includes = RepositoryModule.class)
public class ApiModule {

    public static final String BASE_URL = "https://ska.true-ip.ru/";
    //public static final String BASE_URL = "https://acs.smartru.com/";

    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(new TypeToken<List<ClaimModel.PhotoUrl>>() {}.getType(), new ImagesFieldAdapter())
                .create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    TrustManager[] provideTrustManagersArray() {
        return new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

                    @Override
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                }
        };
    }

    @Provides
    @Singleton
    SSLSocketFactory provideSSLSocketFactory(TrustManager[] trustManagers) {
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagers, new SecureRandom());

            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }

    @Provides
    @Singleton
    OkHttpClient provideHttpClient(TrustManager[] trustManagers, SSLSocketFactory sslSocketFactory) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-Type", "application/json");
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                });
        if (sslSocketFactory != null) {
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManagers[0]);
        }

        return builder.build();
    }

    @Provides
    @Singleton
    HLMApi provideApi(Retrofit retrofit) {
        return retrofit.create(HLMApi.class);
    }

    @Provides
    @Singleton
    ApiControllerWithReactivation provideRepositoryHelper(HLMApi hlmApi, RepositoryController repositoryController) {
        return new ApiControllerWithReactivation(hlmApi, repositoryController);
    }

}
