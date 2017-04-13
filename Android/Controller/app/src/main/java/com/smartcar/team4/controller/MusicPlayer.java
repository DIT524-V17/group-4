package com.smartcar.team4.controller;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayer extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private TextView songDuration;
    private SeekBar seekBar;
    private double timeStart = 0, finalTime = 0;
    private int forwardTime = 2000, backwardTime = 2000;
    private Handler durationHandler = new Handler();
    private static final int MY_PERMISSION_REQUEST = 1;
    ArrayList<String> arrayList;
    ListView listView;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        songDuration = (TextView) findViewById(R.id.songDuration);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax((int) finalTime);
        seekBar.setClickable(false);
        if(ContextCompat.checkSelfPermission(MusicPlayer.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MusicPlayer.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MusicPlayer.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MusicPlayer.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else{
                dostuff();
            }
        }

    public void dostuff(){
        listView = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
        @Override
            public void onItemClick(AdapterView<?> adapterView,View view, int i, long l){
            if(mediaPlayer != null){
                mediaPlayer.release();
            }
           // mediaPlayer.start();
        }
        });
    }

    public void getMusic(){
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri,null,null,null,null);

        if(songCursor != null && songCursor.moveToFirst()){
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do{
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentLocation = songCursor.getString(songLocation);
                arrayList.add(currentTitle + "\n" + currentArtist + "\n" + currentLocation);
            } while(songCursor.moveToNext());
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case MY_PERMISSION_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MusicPlayer.this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                        dostuff();
                    }
                }else{
                    Toast.makeText(this,"no",Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }




    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            timeStart = mediaPlayer.getCurrentPosition();
            seekBar.setProgress((int) timeStart);
            double timeRemaining = finalTime - timeStart;
            songDuration.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
            durationHandler.postDelayed(this, 100);
        }
    };

    public void play() {
            mediaPlayer.start();
        }
//        mediaPlayer.start();
//        timeStart = mediaPlayer.getCurrentPosition();
//        seekBar.setProgress((int) timeStart);
//        durationHandler.postDelayed(updateSeekBarTime, 100);

    public void pause() {
            mediaPlayer.pause();
            mediaPlayer.release();
        }

    public void forward(View view) {
        if ((timeStart + forwardTime) <= finalTime) {
            timeStart = timeStart - backwardTime;
            mediaPlayer.seekTo((int) timeStart);
        }
    }
    public void backforward(View view) {
        //check if we can go back at backwardTime seconds after song starts
        if ((timeStart - backwardTime) > 0) {
            timeStart = timeStart - backwardTime;
            mediaPlayer.seekTo((int) timeStart);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
