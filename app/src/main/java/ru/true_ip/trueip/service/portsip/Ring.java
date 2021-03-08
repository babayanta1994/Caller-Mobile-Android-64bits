package ru.true_ip.trueip.service.portsip;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;

/**
 *
 * Created by Andrey Filimonov on 01.03.2018.
 */

public class Ring {

    private static final int TONE_RELATIVE_VOLUME = 70;
    private ToneGenerator mRingbackPlayer;
    private ToneGenerator mDTMFPlayer;
    protected Ringtone mRingtonePlayer;
    int ringRef = 0;
    private Context mContext;

    private static Ring single=null;

    public static Ring getInstance(Context context) {
        if (single == null) {
            single = new Ring(context);
        }
        return single;
    }
    private Ring(Context context){
        mContext = context;
    }
    public boolean stop() {
        stopRingBackTone();
        stopRingTone();
        stopDTMF();

        return true;
    }

    public void startDTMF(int number) {
        if (mDTMFPlayer == null) {
            try {
                mDTMFPlayer = new ToneGenerator(AudioManager.STREAM_VOICE_CALL, TONE_RELATIVE_VOLUME);
            } catch (RuntimeException e) {
                mDTMFPlayer = null;
            }
        }

        if(mDTMFPlayer != null){
            synchronized(mDTMFPlayer){
                switch(number){
                    case 0: mDTMFPlayer.startTone(ToneGenerator.TONE_DTMF_0); break;
                    case 1: mDTMFPlayer.startTone(ToneGenerator.TONE_DTMF_1); break;
                    case 2: mDTMFPlayer.startTone(ToneGenerator.TONE_DTMF_2); break;
                    case 3: mDTMFPlayer.startTone(ToneGenerator.TONE_DTMF_3); break;
                    case 4: mDTMFPlayer.startTone(ToneGenerator.TONE_DTMF_4); break;
                    case 5: mDTMFPlayer.startTone(ToneGenerator.TONE_DTMF_5); break;
                    case 6: mDTMFPlayer.startTone(ToneGenerator.TONE_DTMF_6); break;
                    case 7: mDTMFPlayer.startTone(ToneGenerator.TONE_DTMF_7); break;
                    case 8: mDTMFPlayer.startTone(ToneGenerator.TONE_DTMF_8); break;
                    case 9: mDTMFPlayer.startTone(ToneGenerator.TONE_DTMF_9); break;
                    case 10: mDTMFPlayer.startTone(ToneGenerator.TONE_DTMF_S); break;
                    case 11: mDTMFPlayer.startTone(ToneGenerator.TONE_DTMF_P); break;
                }
            }
        }
    }

    public void stopDTMF() {
        if(mDTMFPlayer != null){
            synchronized(mDTMFPlayer){
                mDTMFPlayer.stopTone();
                mDTMFPlayer = null;
            }
        }
    }

    public void startRingTone() {
        if(mRingtonePlayer != null&&mRingtonePlayer.isPlaying()){
            ringRef++;
            return;
        }

        if(mRingtonePlayer == null&&mContext!=null){
            try{
                mRingtonePlayer = RingtoneManager.getRingtone(mContext, android.provider.Settings.System.DEFAULT_RINGTONE_URI);
            }catch(Exception e){
                e.printStackTrace();
                return;
            }
        }

        if(mRingtonePlayer != null){
            synchronized(mRingtonePlayer){
                ringRef++;
                mRingtonePlayer.play();
            }
        }
    }

    public void stopRingTone() {
        if(mRingtonePlayer != null){
            synchronized(mRingtonePlayer){

                if(--ringRef<=0){
                    mRingtonePlayer.stop();
                    mRingtonePlayer = null;
                }
            }
        }
    }

    public void startRingBackTone() {
        if (mRingbackPlayer == null) {
            try {
                mRingbackPlayer = new ToneGenerator(AudioManager.STREAM_VOICE_CALL, TONE_RELATIVE_VOLUME);
            } catch (RuntimeException e) {
                mRingbackPlayer = null;
            }
        }

        if(mRingbackPlayer != null){
            synchronized(mRingbackPlayer){
                mRingbackPlayer.startTone(ToneGenerator.TONE_SUP_RINGTONE);
            }
        }
    }

    public void stopRingBackTone() {
        if(mRingbackPlayer != null){
            synchronized(mRingbackPlayer){
                mRingbackPlayer.stopTone();
                mRingbackPlayer =null;
            }
        }
    }

}
