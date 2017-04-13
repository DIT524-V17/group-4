package com.smartcar.team4.controller;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.UUID;


public class ControllerActivity extends AppCompatActivity {

    private JoystickView joystick;
    TextView test;
    Button btnDis;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = MenuActivity.btSocket;
    private boolean isBtConnected = false;
    //Not sure if the UUID is correct..
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_controller);


        // Joystick
        joystick = (JoystickView) findViewById(R.id.game);
        joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                switch (direction) {
                    case JoystickView.FRONT:
                        goForward();
                        break;
                    case JoystickView.FRONT_RIGHT:
                        goLeftFront();
                        break;
                    case JoystickView.RIGHT:
                        goRight();
                        break;
                    case JoystickView.RIGHT_BOTTOM:
                        goLeftBottom();
                        break;
                    case JoystickView.BOTTOM:
                        goReverse();
                        break;
                    case JoystickView.BOTTOM_LEFT:
                        goRightBottom();
                        break;
                    case JoystickView.LEFT:
                        goLeft();
                        break;
                    case JoystickView.LEFT_FRONT:
                        goRightFront();
                        break;
                    default: stop();

                }
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);

        // Button for reverse
        final Button buttonForward = (Button) findViewById(R.id.button_forward);
        buttonForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    // when button is pushed down
                    case MotionEvent.ACTION_DOWN:
                        goForward();
                        return true;
                    // when button is released
                    case MotionEvent.ACTION_UP:
                        stop();
                        return true;
                }
                return false;
            }
        });

        // Button for reverse
        final Button buttonReverse = (Button) findViewById(R.id.button_reverse);
        buttonReverse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    // when button is pushed down
                    case MotionEvent.ACTION_DOWN:
                        goReverse();
                        return true;
                    // when button is released
                    case MotionEvent.ACTION_UP:
                        stop();
                        return true;
                }
                return false;
            }
        });

        // Button for turning left
        final Button buttonLeft = (Button) findViewById(R.id.button_left);
        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    // when button is pushed down
                    case MotionEvent.ACTION_DOWN:
                        goLeft();
                        return true;
                    // when button is released
                    case MotionEvent.ACTION_UP:
                        stop();
                        return true;
                }
                return false;
            }
        });

        // Button for turning right
        final Button buttonRight = (Button) findViewById(R.id.button_right);
        buttonRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    // when button is pushed down
                    case MotionEvent.ACTION_DOWN:
                        goRight();
                        return true;
                    // when button is released
                    case MotionEvent.ACTION_UP:
                        stop();
                        return true;
                }
                return false;
            }
        });


        // Change from joystick to analog controller
        ToggleButton toggle = (ToggleButton) findViewById(R.id.button_toggleMode);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // D-Pad enabled
                    Dpad();
                    joystick.setVisibility(View.INVISIBLE);
                    buttonForward.setVisibility(View.VISIBLE);
                    buttonLeft.setVisibility(View.VISIBLE);
                    buttonRight.setVisibility(View.VISIBLE);
                    buttonReverse.setVisibility(View.VISIBLE);
                } else {
                    // Joystick enabled
                    Jpad();
                    joystick.setVisibility(View.VISIBLE);
                    buttonForward.setVisibility(View.INVISIBLE);
                    buttonLeft.setVisibility(View.INVISIBLE);
                    buttonRight.setVisibility(View.INVISIBLE);
                    buttonReverse.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void Jpad() {
        if(btSocket != null ){
            try{
                btSocket.getOutputStream().write("j".getBytes());
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private void Dpad() {
        if(btSocket != null ){
            try{
                btSocket.getOutputStream().write("p".getBytes());
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private void goForward() {
        if(btSocket != null ){
            try{
                btSocket.getOutputStream().write("f".toString().getBytes());
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private void goReverse(){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write("b".toString().getBytes());
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void goRight(){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write("r".toString().getBytes());
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void goLeft(){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write("l".toString().getBytes());
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void goLeftFront(){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write("q".toString().getBytes());
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void goRightFront(){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write("e".toString().getBytes());
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void goLeftBottom(){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write("z".toString().getBytes());
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void goRightBottom(){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write("c".toString().getBytes());
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void stop(){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write("s".toString().getBytes());
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void Disconnect(){
        if (btSocket!=null){
            try {
                btSocket.close();
            }
            catch (IOException e){
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }
        finish();
    }

}
