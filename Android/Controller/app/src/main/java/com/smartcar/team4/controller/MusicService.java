package com.smartcar.team4.controller;

/**
 * Author Qing Lin
 */

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class MusicService extends Service {

    private String[] musicDir = new String[]{Environment.getExternalStorageDirectory().getAbsolutePath()+"/Music/safeslow.mp3",
            Environment.getExternalStorageDirectory().getAbsolutePath() +"/Music/led.mp3"};
    private int musicIndex = 1;

    public final IBinder binder = new MyBinder();
    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public static MediaPlayer mp = new MediaPlayer();
    public MusicService() {
        try {
            mp.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Music/safeslow.mp3");
            //mp.setDataSource(Environment.getDataDirectory().getAbsolutePath()+"/You.mp3");
            mp.prepare();
            musicIndex = 1;
        } catch (Exception e) {
            Log.d("hint","can't get to the song");
            e.printStackTrace();
        }
    }
    public void playOrPause() {
        if(mp.isPlaying()){
            mp.pause();
        } else {
            mp.start();
        }
    }
    public void stop() {
        if(mp != null) {
            mp.stop();
            try {
                mp.prepare();
                mp.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void nextMusic() {
        if(mp != null && musicIndex < 3) {
            mp.stop();
            try {
                mp.reset();
                mp.setDataSource(musicDir[musicIndex+1]);
                musicIndex++;
                mp.prepare();
                mp.seekTo(0);
                mp.start();
            } catch (Exception e) {
                Log.d("hint", "can't jump next music");
                e.printStackTrace();
            }
        }
    }
    public void preMusic() {
        if(mp != null && musicIndex > 0) {
            mp.stop();
            try {
                mp.reset();
                mp.setDataSource(musicDir[musicIndex-1]);
                musicIndex--;
                mp.prepare();
                mp.seekTo(0);
                mp.start();
            } catch (Exception e) {
                Log.d("hint", "can't jump pre music");
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onDestroy() {
        mp.stop();
        mp.release();
        super.onDestroy();
    }


    /**
     * onBind and Service 。
     * return null，that means the client could not build connection。
     */
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}

