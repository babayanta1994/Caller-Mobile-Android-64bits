package ru.true_ip.trueip.utils;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;

import java.io.File;

/**
 * Created by ektitarev on 15.12.2017.
 */

public class SnapshotsHelper {

    FFmpegFrameGrabber grabber;
    FFmpegFrameRecorder recorder;

    public SnapshotsHelper(String url, File file)
    {
        try {
            if (url != null && !url.isEmpty()) {
                grabber = new FFmpegFrameGrabber(url);
                grabber.setOption("rtsp_transport", "tcp");  // -rtsp_transport tcp
                grabber.start();
            }

            if (file != null) {
                recorder = FFmpegFrameRecorder.createDefault(file, grabber.getImageWidth(), grabber.getImageHeight());
                //recorder.setAudioChannels(grabber.getAudioChannels());
                //recorder.setAudioCodec(grabber.getAudioCodec());
                recorder.start();
            }

        } catch (FrameRecorder.Exception e) {
            e.printStackTrace();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }

    public void saveSnaphot() {
        Frame frame;
        try {
            if (grabber != null && recorder != null) {
                frame = grabber.grabImage();
                recorder.record(frame);
            }

        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        } catch (FrameRecorder.Exception e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            if (recorder != null) {
                recorder.stop();
            }
            if (grabber != null) {
                grabber.stop();
            }

        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        } catch (FrameRecorder.Exception e) {
            e.printStackTrace();
        }
    }
}
