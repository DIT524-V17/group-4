package com.smartcar.team4.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import static android.graphics.Color.BLACK;

public class MainActivity extends AppCompatActivity {

    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        test = (TextView) findViewById(R.id.test_output);

        // Button for turning right
        Button buttonRight = (Button) findViewById(R.id.button_turnRight);
        buttonRight.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    // when button is pushed down
                    case MotionEvent.ACTION_DOWN:
                        test.setText("Turn right");
                        return true;
                    // when button is released
                    case MotionEvent.ACTION_UP:
                        test.setText("Test");
                        return true;
                }
                return false;
            }
        });

        // Button for turning left
        Button buttonLeft = (Button) findViewById(R.id.button_turnLeft);
        buttonLeft.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    // when button is pushed down
                    case MotionEvent.ACTION_DOWN:
                        test.setText("Turn left");
                        return true;
                    // when button is released
                    case MotionEvent.ACTION_UP:
                        test.setText("Test");
                        return true;
                }
                return false;
            }
        });


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
