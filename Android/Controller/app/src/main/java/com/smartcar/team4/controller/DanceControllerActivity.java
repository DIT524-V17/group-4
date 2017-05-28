// @author Filip
package com.smartcar.team4.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DanceControllerActivity extends AppCompatActivity {

    boolean state = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dance_controller);

        final TextView currentSong = (TextView) findViewById(R.id.textSong);

        // Start dancing
        final Button buttonDance = (Button) findViewById(R.id.buttonDance);
        buttonDance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                    Client c = new Client(intToByteArray('d', 'n', 'c'));
                    c.execute();
                    state = !state;
            }
        });

        // Start stop
        final Button buttonStop = (Button) findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Client c = new Client(intToByteArray('s', 't', 'p'));
                c.execute();
                state = !state;
            }
        });

        // Select song1
        final Button buttonSong1 = (Button) findViewById(R.id.buttonSong1);
        buttonSong1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(true) {
                    Client c = new Client(intToByteArray('s', 't', 'p'));
                    c.execute();
                    Client c = new Client(intToByteArray('s', 'n', '1'));
                    c.execute();
                    currentSong.setText("Song 1");
                }
            }
        });

        // Select song2
        final Button buttonSong2 = (Button) findViewById(R.id.buttonSong2);
        buttonSong2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(true) {
                    Client c = new Client(intToByteArray('s', 't', 'p'));
                    c.execute();
                    Client c = new Client(intToByteArray('s', 'n', '2'));
                    c.execute();
                    currentSong.setText("Song 2");
                }
            }
        });

    }

    public byte[] intToByteArray(char type, char power, char direction) {
        return new byte[] {
                (byte)type,
                (byte)power,
                (byte)direction};
    }

}

