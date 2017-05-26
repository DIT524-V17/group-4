package com.smartcar.team4.controller;
/*
*Author Qing Lin
*/
import android.bluetooth.BluetoothAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import android.util.Log;


public class DancingActivity extends AppCompatActivity implements View.OnClickListener {


    private MusicService musicService;
    private SeekBar seekBar;
    private TextView musicStatus, musicTime;
    private Button btnPlayOrPause, btnStop, btnQuit;
    private SimpleDateFormat time = new SimpleDateFormat("m:ss");
    private ServiceConnection sc = new ServiceConnection() { // define the format of playing time display        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = ((MusicService.MyBinder)iBinder).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };
    private void bindServiceConnection() {
        Intent intent = new Intent(DancingActivity.this, MusicService.class);
        startService(intent);
        bindService(intent, sc, this.BIND_AUTO_CREATE);

    }
    public android.os.Handler handler = new android.os.Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(musicService.mp.isPlaying()) {
                musicStatus.setText(getResources().getString(R.string.playing));
                btnPlayOrPause.setText(getResources().getString(R.string.pause).toUpperCase());
            } else {
                musicStatus.setText(getResources().getString(R.string.pause));
                btnPlayOrPause.setText(getResources().getString(R.string.play).toUpperCase());
            }
            musicTime.setText(time.format(musicService.mp.getCurrentPosition()) + "/"
                    + time.format(musicService.mp.getDuration()));
            seekBar.setProgress(musicService.mp.getCurrentPosition());
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        musicService.mp.seekTo(seekBar.getProgress());
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            handler.postDelayed(runnable, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dancing);

        Log.d("hint", "ready to new MusicService");
        musicService = new MusicService();
        Log.d("hint", "finish to new MusicService");
        bindServiceConnection();

        seekBar = (SeekBar)this.findViewById(R.id.MusicSeekBar);
        seekBar.setProgress(musicService.mp.getCurrentPosition());
        seekBar.setMax(musicService.mp.getDuration());

        musicStatus = (TextView)this.findViewById(R.id.MusicStatus);
        musicTime = (TextView)this.findViewById(R.id.MusicTime);

        btnPlayOrPause = (Button)this.findViewById(R.id.BtnPlayorPause);

        Log.d("hint", Environment.getExternalStorageDirectory().getAbsolutePath()+"/safeslow.mp3");
    }

    @Override
    protected void onResume() {
        if(musicService.mp.isPlaying()) {
            musicStatus.setText(getResources().getString(R.string.playing));
        } else {
            musicStatus.setText(getResources().getString(R.string.pause));
        }

        seekBar.setProgress(musicService.mp.getCurrentPosition());
        seekBar.setMax(musicService.mp.getDuration());
        handler.post(runnable);
        super.onResume();
        Log.d("hint", "handler post runnable");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.BtnPlayorPause:
                musicService.playOrPause();
                break;
            case R.id.BtnStop:
                musicService.stop();
                seekBar.setProgress(0);
                break;
            case R.id.BtnQuit:
                handler.removeCallbacks(runnable);
                unbindService(sc);
                try {
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnPre:
                musicService.preMusic();
                break;
            case R.id.btnNext:
                musicService.nextMusic();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        unbindService(sc);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


/*public class DancingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dancing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }



}
 */