package ru.true_ip.trueip.utils;

import android.content.Context;
import android.net.Uri;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;

/**
 * Created by ektitarev on 06.12.2017.
 */

public class VLCPlayer
{
    // events
    public static final int MediaChanged        = 0x100;
    public static final int Opening             = 0x102;
    public static final int Buffering           = 0x103;
    public static final int Playing             = 0x104;
    public static final int Paused              = 0x105;
    public static final int Stopped             = 0x106;
    public static final int EndReached          = 0x109;
    public static final int EncounteredError   = 0x10a;
    public static final int TimeChanged         = 0x10b;
    public static final int PositionChanged     = 0x10c;
    public static final int SeekableChanged     = 0x10d;
    public static final int PausableChanged     = 0x10e;
    public static final int Vout                = 0x112;
    public static final int ESAdded             = 0x114;
    public static final int ESDeleted           = 0x115;
    public static final int ESSelected          = 0x116;

    public interface PlayerEventListener {
        void onPlayerEvent(int event);
    }

    private LibVLC lib;
    private MediaPlayer player;

    private SurfaceView targetView;
    private IVLCVout.Callback holderCallbacks;

    private int viewPortWidth;
    private int viewPortHeight;

    private String url;
    private PlayerEventListener listener;


    private VLCPlayer (Context context, SurfaceView view, String rtspUrl, ArrayList<String> options)
    {
        targetView = view;
        url = rtspUrl;

        if (options == null || options.isEmpty()) {
            options = new ArrayList<>();
            options.add("--no-drop-late-frames");
            options.add("--no-skip-frames");
            options.add("--aout=opensles");
            options.add("--audio-time-stretch");
            options.add("-vvv");
        }

        lib = new LibVLC(context, options);

        player = new MediaPlayer(lib);

        player.setEventListener(new MediaPlayer.EventListener() {

            @Override
            public void onEvent(MediaPlayer.Event event) {
                if (listener != null) {
                    listener.onPlayerEvent(event.type);
                }
            }
        });
    }

    private VLCPlayer (Context context, SurfaceView view, String rtspUrl) {
        this(context, view, rtspUrl, null);
    }

    public void play(IVLCVout.Callback callbacks) {
        if (player != null) {

            Media media = new Media(lib, Uri.parse(url));

            player.setMedia(media);

            final IVLCVout vout = player.getVLCVout();

            if (vout.areViewsAttached()) {
                vout.detachViews();
            }

            vout.setVideoView(targetView);
            vout.attachViews();

            player.play();
       }
    }

    public void setPlayerEventListener(PlayerEventListener listener) {
        this.listener = listener;
    }

    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    public void stop() {
        if (player != null) {

            if (player.isPlaying()) {
                player.stop();
            }
            final IVLCVout vout = player.getVLCVout();
            if (vout.areViewsAttached()) {
                vout.detachViews();
                vout.removeCallback(holderCallbacks);
            }
            release();
            player = null;
        }
    }

    public void setAspectRatio(String aspectRatio) {
        player.setAspectRatio(aspectRatio);
        player.setScale(0);
    }

    public void setViewportSize(int w, int h)
    {
        viewPortWidth = w;
        viewPortHeight = h;

        player.getVLCVout().setWindowSize(w, h);
    }

    public int getViewportWidth() {
        return viewPortWidth;
    }

    public int getViewportHeight() {
        return viewPortHeight;
    }

    public void release() {
        if(player.isPlaying()) {
            stop();
        }
        player.release();
        lib.release();
    }

    public static VLCPlayer getVLCPlayer(Context context, SurfaceView view, String url) {
        return new VLCPlayer(context, view, url);
    }

    public static VLCPlayer getVLCPlayer(Context context, SurfaceView view, String url, ArrayList<String> opts) {
        return new VLCPlayer(context, view, url, opts);
    }

    public void setVolume(int volume) {
        if (isPlaying())
            player.setVolume(volume);
    }
}
