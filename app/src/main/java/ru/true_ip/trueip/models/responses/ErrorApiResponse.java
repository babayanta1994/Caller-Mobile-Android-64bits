package ru.true_ip.trueip.models.responses;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import ru.true_ip.trueip.service.service.Logger;


/**
 * Created by ektitarev on 28.12.2017.
 */

public class ErrorApiResponse {

    private static final String TAG = ErrorApiResponse.class.getName();

    private static class HashMapDeserializer implements JsonDeserializer<Map<String, String[]>> {

        @Override
        public Map<String, String[]> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                Gson gson = new GsonBuilder().setLenient().create();
                Type type = new TypeToken<Map<String, String[]>>() {}.getType();
                return gson.fromJson(json, type);
            }
            return null;
        }
    }

    private static class ErrorInfo {
        ErrorInfo() {
            code = 0;
            message = "";
            errors = new HashMap<>();
        }
        private Integer code;
        private String message;
        private Map<String, String[]> errors;
    }

    private ErrorInfo error;

    private ErrorApiResponse() { error = new ErrorInfo(); }

    private ErrorApiResponse(ErrorApiResponse errorApiResponse) {
        this.error = errorApiResponse.error != null ? errorApiResponse.error : new ErrorInfo();
    }

    public ErrorApiResponse(ResponseBody responseBody) {
        this(ErrorApiResponse.getErrorMessage(responseBody));
    }

    public String getError() { return error.message; }

    public Integer getCode() { return error.code; }

    public Map<String, String[]> getDetailedErrors() { return error.errors; }


    private static ErrorApiResponse getErrorMessage(ResponseBody errorBody) {
        if (errorBody != null) {
            try {
                String json = errorBody.string();
                if (json != null && !json.isEmpty()) {
                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .registerTypeAdapter(Map.class, new HashMapDeserializer())
                            .create();
                    return gson.fromJson(json, ErrorApiResponse.class);
                }
            } catch (IOException e) {
                Logger.error(TAG, e.getMessage());
            }
        }
        return new ErrorApiResponse();
    }
}
