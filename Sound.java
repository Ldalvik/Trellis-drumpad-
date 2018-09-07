package com.example.student.piano;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

public class Sound {
    private Context ctx;
    MediaPlayer mediaPlayer = null;
    private String fileName;
    boolean paused = false;

    public Sound(Context ctx, String fileName){
        this.ctx = ctx;
        this.fileName = fileName;
    }
        public void playSound() {
        if(!paused && mediaPlayer!=null){
            mediaPlayer.stop();
        }
        if(paused){
            mediaPlayer.start();
            paused = false;
        }
            mediaPlayer = new MediaPlayer();
            try {
                AssetFileDescriptor afd = ctx.getAssets().openFd(fileName+".wav");
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                mediaPlayer.prepare();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        }

    public void stopSound(boolean latch) {
        if(latch) {
            mediaPlayer.pause();
            paused = true;
        } else {
            playSound();
            paused = false;
        }
    }
}
