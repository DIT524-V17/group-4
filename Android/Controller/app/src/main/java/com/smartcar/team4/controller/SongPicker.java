/*
* Authors: Andrius, Margit
* Qing Lin(worked on fixing contentpriver and  output/input datastream )
*/
package com.smartcar.team4.controller;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class SongPicker extends AppCompatActivity {
    private static final int MY_PERMISSION_REQUEST = 1;
    ArrayList<String> arrayList;
    ArrayList<String> arrayListDisplay;
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        if (ContextCompat.checkSelfPermission(SongPicker.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SongPicker.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(SongPicker.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(SongPicker.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            dostuff();
        }
    }

    private Socket s;

    //  public SongPicker(String host, int port, Uri file) {
//        try {
//            s = new Socket(host, port);
//            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
//            //FileInputStream fis = new FileInputStream(file);
//            InputStream fis =getContentResolver().openInputStream(file);
//            byte[] buffer = new byte[4096];
//
//            while (fis.read(buffer) > 0) {
//                dos.write(buffer);
//            }
//
//            fis.close();
//            dos.close();
//        } catch (UnknownHostException e1) {
//            e1.printStackTrace();
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//    }

    public void dostuff() {
        listView = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        arrayListDisplay = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arrayListDisplay);
        listView.setAdapter(adapter);

        // Define a listener that responds to clicks on a song in the song ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            /*
             * When a filename in the ListView is clicked, get its
             * content URI and send it to the requesting app
             */
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
                Uri songUri = Uri.parse("file:///" + arrayList.get(position));
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                    StrictMode.setThreadPolicy(policy);
                    s = new Socket("192.168.42.1", 9999);
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                    //FileInputStream fis = new FileInputStream(file);
                    InputStream fis =getContentResolver().openInputStream(songUri);
                    byte[] buffer = new byte[4096];

                    while (fis.read(buffer) > 0) {
                        dos.write(buffer);
                    }

                    fis.close();
                    dos.close();
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                //SongPicker fc = new SongPicker("192.168.42.1", 9998, songUri);


            }
        });
    }


    public void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentLocation = songCursor.getString(songLocation);
                arrayList.add(currentLocation);
                arrayListDisplay.add(currentTitle + "\n" + currentArtist);
            } while (songCursor.moveToNext());
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(SongPicker.this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                        dostuff();
                    }
                } else {
                    Toast.makeText(this, "no", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }
}