package ru.true_ip.trueip.service.portsip;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.portsip.PortSipEnumDefine;
import com.portsip.PortSipSdk;

import java.util.HashMap;

/**
 *
 * Created by Andrey Filimonov on 28.02.2018.
 */

public class SettingConfig {
    public static final String PRE_NAME = "com.portsip_preferences";
    static final String PORTSIP_USERNAME = "PORTSIP_USERNAME";
    static final String PORTSIP_USERPASSWORD = "PORTSIP_USEPWD";
    static final String PORTSIP_SIPSERVER = "PORTSIP_SIPSERVER";
    static final String PORTSIP_SIPSERVERPORT = "PORTSIP_SIPPORT";
    static final String PORTSIP_USERDOMAIN = "PORTSIP_USERDOMAIN";
    static final String PORTSIP_AUTHNAME = "PORTSIP_AUTHNAME";
    static final String PORTSIP_USEDISPALYNAME= "PORTSIP_DISNAME";

    static final String PORTSIP_STUNSVR= "PORTSIP_STUNSVR";
    static final String PORTSIP_STUNPORT= "PORTSIP_STUNPORT";
    static final String PORTSIP_TRANSTYPE= "PORTSIP_TRANSTYPE";
    static final String PORTSIP_SRTPTYPE= "PORTSIP_SRTPTYPE";


    static public  boolean saveSettingConfig(Context context, PortSipSdk sdk) {
        SharedPreferences mPrefrence = context
                .getSharedPreferences(PRE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor mPrefrenceEdit = mPrefrence.edit();

        if (mPrefrenceEdit == null) {
            return false;
        }

//		mPrefrenceEdit.putBoolean(PORTSIP_FEATURE_VAD,mVAD);
//		sdk.enableVAD(mVAD);
//		mPrefrenceEdit.putBoolean(PORTSIP_FEATURE_AEC,mAEC);
//		sdk.enableAEC(mAEC);
//		mPrefrenceEdit.putBoolean(PORTSIP_FEATURE_ANS,mANS);
//		sdk.enableANS(mANS);
//		mPrefrenceEdit.putBoolean(PORTSIP_FEATURE_AGC,mAGC);
//		sdk.enableAGC(mAGC);
//		mPrefrenceEdit.putBoolean(PORTSIP_FEATURE_DTMFINFO,mDTMFINFO);

        return mPrefrenceEdit.commit();
    }

//	static public  void setCodecStatusById(Context context,HashMap<Integer,Boolean> codecs) {
//		if(codecs==null)
//			return;
//		boolean value;
//		SharedPreferences mPrefrence = context.getSharedPreferences(PRE_NAME,Context.MODE_PRIVAT);
//		Editor editor = mPrefrence.edit();
//
//		value = codecs.get(PortSipEnumDefine.ENUM_AUDIOCODEC_G729);
//		editor.putBoolean(PORTSIP_AUDIOCODEC_G729, value);
//		//pcma
//		value = codecs.get(PortSipEnumDefine.ENUM_AUDIOCODEC_PCMA);
//		editor.putBoolean(PORTSIP_AUDIOCODEC_PCMA, value);
//		//pcmu
//		value = codecs.get(PortSipEnumDefine.ENUM_AUDIOCODEC_PCMU);
//		editor.putBoolean(PORTSIP_AUDIOCODEC_PCMU, value);
//		//gsm
//		value = codecs.get(PortSipEnumDefine.ENUM_AUDIOCODEC_GSM);
//		editor.putBoolean(PORTSIP_AUDIOCODEC_GSM, value);
//		//g722
//		value = codecs.get(PortSipEnumDefine.ENUM_AUDIOCODEC_G722);
//		editor.putBoolean(PORTSIP_AUDIOCODEC_G722, value);
//		//ilbc
//		value = codecs.get(PortSipEnumDefine.ENUM_AUDIOCODEC_ILBC);
//		editor.putBoolean(PORTSIP_AUDIOCODEC_ILBC, value);
//		//amr
//		value = codecs.get(PortSipEnumDefine.ENUM_AUDIOCODEC_AMR);
//		editor.putBoolean(PORTSIP_AUDIOCODEC_AMR, value);
//		//amrwb
//		value = codecs.get(PortSipEnumDefine.ENUM_AUDIOCODEC_AMR);
//		editor.putBoolean(PORTSIP_AUDIOCODEC_AMR, value);
//		//speex
//		value = codecs.get(PortSipEnumDefine.ENUM_AUDIOCODEC_SPEEX);
//		editor.putBoolean(PORTSIP_AUDIOCODEC_SPEEX, value);
//		//speexwb
//		value = codecs.get(PortSipEnumDefine.ENUM_AUDIOCODEC_SPEEXWB);
//		editor.putBoolean(PORTSIP_AUDIOCODEC_SPEEXWB, value);
//		//Opus
//		value = codecs.get(PortSipEnumDefine.ENUM_AUDIOCODEC_OPUS);
//		editor.putBoolean(PORTSIP_AUDIOCODEC_OPUS, value);
//		//dtmf
//		value = codecs.get(PortSipEnumDefine.ENUM_AUDIOCODEC_DTMF);
//		editor.putBoolean(PORTSIP_AUDIOCODEC_DTMF, value);
//		//h263
//		value = codecs.get(PortSipEnumDefine.ENUM_VIDEOCODEC_H263);
//		editor.putBoolean(PORTSIP_VIDEOCODEC_H263, value);
//		//h263.98
//		value = codecs.get(PortSipEnumDefine.ENUM_VIDEOCODEC_H263_1998);
//		editor.putBoolean(PORTSIP_VIDEOCODEC_H263_1998, value);
//		//h264
//		value = codecs.get(PortSipEnumDefine.ENUM_VIDEOCODEC_H264);
//		editor.putBoolean(PORTSIP_VIDEOCODEC_H264, value);
//
//		editor.commit();
//	}

    public static HashMap<Integer,Boolean> getCodecs(Context context){
        HashMap<Integer,Boolean> codecs= new HashMap<Integer,Boolean>();
        SharedPreferences mPrefrence = context.getSharedPreferences(PRE_NAME,Context.MODE_PRIVATE);
//		Editor editor = mPrefrence.edit();
        String key;
        boolean value;
        Resources resources= context.getResources();
        //g729
        key = Keys.MEDIA_G729;
        value = mPrefrence.getBoolean(key, false);
        codecs.put(PortSipEnumDefine.ENUM_AUDIOCODEC_G729, value);
        //pcma
        key = Keys.MEDIA_PCMA;
        value = mPrefrence.getBoolean(key, false);
        codecs.put(PortSipEnumDefine.ENUM_AUDIOCODEC_PCMA, value);
        //pcmu
        key = Keys.MEDIA_PCMU;
        value = mPrefrence.getBoolean(key, false);
        codecs.put(PortSipEnumDefine.ENUM_AUDIOCODEC_PCMU, value);
        //gsm
        key = Keys.MEDIA_GSM;
        value = mPrefrence.getBoolean(key, false);
        codecs.put(PortSipEnumDefine.ENUM_AUDIOCODEC_GSM, value);
        //g722
        key = Keys.MEDIA_G722;
        value = mPrefrence.getBoolean(key, false);
        codecs.put(PortSipEnumDefine.ENUM_AUDIOCODEC_G722, value);
        //ilbc
        key = Keys.MEDIA_ILBC;
        value = mPrefrence.getBoolean(key, false);
        codecs.put(PortSipEnumDefine.ENUM_AUDIOCODEC_ILBC, value);
        //amr
        key = Keys.MEDIA_AMR;
        value = mPrefrence.getBoolean(key, false);
        codecs.put(PortSipEnumDefine.ENUM_AUDIOCODEC_AMR, value);
        //amrwb
        key = Keys.MEDIA_AMRWB;
        value = mPrefrence.getBoolean(key, false);
        codecs.put(PortSipEnumDefine.ENUM_AUDIOCODEC_AMRWB, value);
        //speex
        key = Keys.MEDIA_SPEEX;
        value = mPrefrence.getBoolean(key, false);
        codecs.put(PortSipEnumDefine.ENUM_AUDIOCODEC_SPEEX, value);
        //speexwb
        key = Keys.MEDIA_SPEEXWB;
        value = mPrefrence.getBoolean(key, false);
        codecs.put(PortSipEnumDefine.ENUM_AUDIOCODEC_SPEEXWB, value);
        //Opus
        key = Keys.MEDIA_OPUS;
        value = mPrefrence.getBoolean(key, false);
        codecs.put(PortSipEnumDefine.ENUM_AUDIOCODEC_OPUS, value);

        //dtmf
        key = Keys.MEDIA_DTMF;
        value = mPrefrence.getBoolean(key, false);
        codecs.put(PortSipEnumDefine.ENUM_AUDIOCODEC_DTMF, value);
        //h263
//        key = Keys.MEDIA_H263;
//        value = mPrefrence.getBoolean(key, false);
//        codecs.put(PortSipEnumDefine.H  ENUM_VIDEOCODEC_H263, value);
        //h263.98
//        key = Keys.MEDIA_H26398;
//        value = mPrefrence.getBoolean(key, false);
//        codecs.put(PortSipEnumDefine.ENUM_VIDEOCODEC_H263_1998, value);
        //h264
        key = Keys.MEDIA_H264;
        value = mPrefrence.getBoolean(key, false);
        codecs.put(PortSipEnumDefine.ENUM_VIDEOCODEC_H264, value);

        return codecs;
    }

    public static UserInfo getUserInfo(Context context){
        UserInfo infor = new UserInfo();
        SharedPreferences mPrefrence = context
                .getSharedPreferences(PRE_NAME,Context.MODE_PRIVATE);
        String stringitem = mPrefrence.getString(PORTSIP_USERNAME, "");
        infor.setUserName(stringitem);
        stringitem = mPrefrence.getString(PORTSIP_USERPASSWORD, "");
        infor.setUserPwd(stringitem);
        stringitem = mPrefrence.getString(PORTSIP_USEDISPALYNAME, "");
        infor.setUserDisplayName(stringitem);
        stringitem = mPrefrence.getString(PORTSIP_USERDOMAIN, "");
        infor.setUserDomain(stringitem);
        stringitem = mPrefrence.getString(PORTSIP_SIPSERVER, "");
        infor.setSipServer(stringitem);
        int port = mPrefrence.getInt(PORTSIP_SIPSERVERPORT, 5060);
        infor.setSipPort(port);

        stringitem = mPrefrence.getString(PORTSIP_STUNSVR, "");
        infor.setStunServer(stringitem);

        port = mPrefrence.getInt(PORTSIP_STUNPORT, 5060);
        infor.setStunPort(port);

        int tansType = mPrefrence.getInt(PORTSIP_TRANSTYPE, PortSipEnumDefine.ENUM_TRANSPORT_UDP);
        infor.setTranType(tansType);

        int srtpType = mPrefrence.getInt(PORTSIP_SRTPTYPE, PortSipEnumDefine.ENUM_SRTPPOLICY_NONE);
        infor.setSrtpType(srtpType);
        return infor;
    }


    public  static void setUserInfo(Context context,UserInfo infor){
        if(infor!=null){
            SharedPreferences mPrefrence = context
                    .getSharedPreferences(PRE_NAME,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPrefrence.edit();
            String stringitem = infor.getUserName();
            editor.putString(PORTSIP_USERNAME, stringitem);

            stringitem = infor.getUserPassword();
            editor.putString(PORTSIP_USERPASSWORD, stringitem);

            stringitem = infor.getUserDisplayName();
            editor.putString(PORTSIP_USEDISPALYNAME, stringitem);

            stringitem = infor.getUserdomain();
            editor.putString(PORTSIP_USERDOMAIN, stringitem);

            stringitem = infor.getSipServer();
            editor.putString(PORTSIP_SIPSERVER, stringitem);

            int port = infor.getSipPort();
            editor.putInt(PORTSIP_SIPSERVERPORT, port);


            stringitem = infor.getStunServer();
            editor.putString(PORTSIP_STUNSVR, stringitem);


            port = infor.getStunPort();
            editor.putInt(PORTSIP_STUNPORT, port );


            int tansType = infor.getTransType();
            editor.putInt(PORTSIP_TRANSTYPE, tansType );

            int srtpType = infor.getSrtpType();
            editor.putInt(PORTSIP_SRTPTYPE, srtpType );

            editor.commit();
        }

    }


    public static void setSrtpType(Context context,int enum_srtpType,PortSipSdk sdk){
        SharedPreferences mPrefrence = context.getSharedPreferences(PRE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefrence.edit();
        editor.putInt(PORTSIP_SRTPTYPE, enum_srtpType);
        sdk.setSrtpPolicy(enum_srtpType);
        editor.commit();

    }

    public static void setTransType(Context context,int enum_transType){
        SharedPreferences mPrefrence = context.getSharedPreferences(PRE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefrence.edit();
        editor.putInt(PORTSIP_TRANSTYPE, enum_transType);
        editor.commit();

    }

    public static int getSrtpType(Context context){
        SharedPreferences mPrefrence = context.getSharedPreferences(PRE_NAME,Context.MODE_PRIVATE);
        return mPrefrence.getInt(PORTSIP_SRTPTYPE, PortSipEnumDefine.ENUM_SRTPPOLICY_NONE);
    }

    public static void setAVArguments(Context context,PortSipSdk sdk) {

        // audio codecs
        SharedPreferences mPrefrence = context.getSharedPreferences(PRE_NAME,Context.MODE_PRIVATE);
        sdk.clearAudioCodec();
        Resources resources = context.getResources();

        String key = Keys.MEDIA_G722;
        if (mPrefrence.getBoolean(key, false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_G722);
        }

        key = Keys.MEDIA_G729;
        if (mPrefrence.getBoolean(key, true)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_G729);
        }

        key = Keys.MEDIA_AMR;
        if (mPrefrence.getBoolean(key, false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_AMR);
        }

        key = Keys.MEDIA_AMRWB;
        if (mPrefrence.getBoolean(key, false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_AMRWB);
        }

        key = Keys.MEDIA_GSM;
        if (mPrefrence.getBoolean(key, false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_GSM);
        }

        key = Keys.MEDIA_PCMA;
        if (mPrefrence.getBoolean(key, true)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_PCMA);
        }

        key = Keys.MEDIA_PCMU;
        if (mPrefrence.getBoolean(key, true)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_PCMU);
        }

        key = Keys.MEDIA_SPEEX;
        if (mPrefrence.getBoolean(key, false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_SPEEX);
        }

        key = Keys.MEDIA_SPEEXWB;
        if (mPrefrence.getBoolean(key, false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_SPEEXWB);
        }

        key = Keys.MEDIA_ILBC;
        if (mPrefrence.getBoolean(key, false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_ILBC);
        }

        key = Keys.MEDIA_ISACWB;
        if (mPrefrence.getBoolean(key, false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_ISACWB);
        }

        key = Keys.MEDIA_ISACSWB;
        if (mPrefrence.getBoolean(key, false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_ISACSWB);
        }

        key = Keys.MEDIA_OPUS;
        if (mPrefrence.getBoolean(key, false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_OPUS);
        }

        key = Keys.MEDIA_DTMF;
        if (mPrefrence.getBoolean(key, false)) {
            sdk.addAudioCodec(PortSipEnumDefine.ENUM_AUDIOCODEC_DTMF);
        }



        // Video codecs
        sdk.clearVideoCodec();
//        key = Keys.MEDIA_H263;
//        if (mPrefrence.getBoolean(key, false)) {
//            sdk.addVideoCodec(PortSipEnumDefine.ENUM_VIDEOCODEC_H263);
//        }
//        key = Keys.MEDIA_H26398;
//        if (mPrefrence.getBoolean(key, false)) {
//            sdk.addVideoCodec(PortSipEnumDefine.ENUM_VIDEOCODEC_H263_1998);
//        }
        key = Keys.MEDIA_H264;
        if (mPrefrence.getBoolean(key, true)) {
            sdk.addVideoCodec(PortSipEnumDefine.ENUM_VIDEOCODEC_H264);
        }
        key = Keys.MEDIA_VP8;
        if (mPrefrence.getBoolean(key, false)) {
            sdk.addVideoCodec(PortSipEnumDefine.ENUM_VIDEOCODEC_VP8);
        }
        sdk.setVideoNackStatus(true);
        //sdk.enableVideoDecoderCallback(true);
        key = Keys.str_resolution;

        String resolution = mPrefrence.getString(key, "2");
        int width = 352;
        int height = 288;
        if(resolution.equals("1"))//QCIF
        {
            width = 176;
            height = 144;
        }
        else if(resolution.equals("2"))//CIF
        {
            width = 352;
            height = 288;
        }
        else if(resolution.equals("3"))//VGA
        {
            width = 640;
            height = 480;
        }
        else if(resolution.equals("4"))//SVGA
        {
            width = 1024;
            height = 768;
        }
        else if(resolution.equals("5"))//XVGA
        {
            width = 640;
            height = 480;
        }
        else if(resolution.equals("6"))//720P
        {
            width = 1280;
            height = 720;
        }
        else if(resolution.equals("7"))//QVGA
        {
            width = 320;
            height = 240;
        }

        sdk.setVideoResolution(width, height);

//		setForward(mpreferences);
        key = Keys.MEDIA_VAD;
        sdk.enableVAD(mPrefrence.getBoolean(key, true));

        key = Keys.MEDIA_AEC;
        sdk.enableAEC(mPrefrence.getBoolean(key, true));

        key = Keys.MEDIA_ANS;
        sdk.enableANS(mPrefrence.getBoolean(key, false));

        key = Keys.MEDIA_AGC;
        sdk.enableAGC(mPrefrence.getBoolean(key, true));

        key = Keys.MEDIA_CNG;
        sdk.enableCNG(mPrefrence.getBoolean(key, true));

        key = Keys.str_pracktitle;
        if (mPrefrence.getBoolean(key, false)) {
            sdk.enableReliableProvisional(true);
        }

        // Use earphone
        sdk.setAudioDevice(PortSipEnumDefine.AudioDevice.EARPIECE);

        // Use Front Camera
        sdk.setVideoDeviceId(1);
        //sdk.setVideoOrientation(PortSipEnumDefine.ENUM_ROTATE_CAPTURE_FRAME_270);
    }
}