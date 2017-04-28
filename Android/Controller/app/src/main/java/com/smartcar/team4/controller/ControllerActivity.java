package com.smartcar.team4.controller;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.UUID;


public class ControllerActivity extends AppCompatActivity {
    RadioGroup group;
    TextView text;
    int dPower;
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

        group = (RadioGroup) findViewById(R.id.RadioGroup);
        text = (TextView) findViewById(R.id.textView5);
        group.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedID){
                RadioButton rb = (RadioButton) group.findViewById(checkedID);
                switch (rb.getId()){
                    case R.id.radioGroup1:
                        //goSlow();
                        dPower = 1;
                        break;
                    case R.id.radioGroup2:
                        //goMedium();
                        dPower = 2;
                        break;
                    case R.id.radioGroup3:
                        //goFast();
                        dPower = 3;
                        break;
                }

            }
        });


        // Joystick
        joystick = (JoystickView) findViewById(R.id.game);
        joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                char dir;
                char type = 'j';
                byte[] bArray;
                switch (direction) {
                    case JoystickView.FRONT:
                        dir = 'f';
                        bArray = intToByteArray(type, power, dir);
                        goForward(bArray);
                        break;
                    case JoystickView.FRONT_RIGHT:
                        dir = 'q';
                        bArray = intToByteArray(type, power, dir);
                        goLeftFront(bArray);
                        break;
                    case JoystickView.RIGHT:
                        dir = 'r';
                        bArray = intToByteArray(type, power, dir);
                        goRight(bArray);
                        break;
                    case JoystickView.RIGHT_BOTTOM:
                        dir = 'z';
                        bArray = intToByteArray(type, power, dir);
                        goLeftBottom(bArray);
                        break;
                    case JoystickView.BOTTOM:
                        dir = 'b';
                        bArray = intToByteArray(type, power, dir);
                        goReverse(bArray);
                        break;
                    case JoystickView.BOTTOM_LEFT:
                        dir = 'c';
                        bArray = intToByteArray(type, power, dir);
                        goRightBottom(bArray);
                        break;
                    case JoystickView.LEFT:
                        dir = 'l';
                        bArray = intToByteArray(type, power, dir);
                        goLeft(bArray);
                        break;
                    case JoystickView.LEFT_FRONT:
                        dir = 'e';
                        bArray = intToByteArray(type, power, dir);
                        goRightFront(bArray);
                        break;
                    default:
                        dir = 's';
                        bArray = intToByteArray(type, power, dir);
                        stop(bArray);

                }
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);

        // Button for reverse
        final Button buttonForward = (Button) findViewById(R.id.button_forward);
        buttonForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                char type = 'd';
                byte[] bArray;
                char dir = 'f';
                bArray = intToByteArray(type, dPower, dir);
                switch(event.getAction()) {
                    // when button is pushed down
                    case MotionEvent.ACTION_DOWN:
                        goForward(bArray);
                        return true;
                    // when button is released
                    case MotionEvent.ACTION_UP:
                        dir = 's';
                        bArray = intToByteArray(type, dPower, dir);
                        stop(bArray);
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
                char type = 'd';
                byte[] bArray;
                char dir = 'b';
                bArray = intToByteArray(type, dPower, dir);
                switch(event.getAction()) {
                    // when button is pushed down
                    case MotionEvent.ACTION_DOWN:
                        goReverse(bArray);
                        return true;
                    // when button is released
                    case MotionEvent.ACTION_UP:
                        dir = 's';
                        bArray = intToByteArray(type, dPower, dir);
                        stop(bArray);
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
                char type = 'd';
                byte[] bArray;
                char dir = 'l';
                bArray = intToByteArray(type, dPower, dir);
                switch(event.getAction()) {
                    // when button is pushed down
                    case MotionEvent.ACTION_DOWN:
                        goLeft(bArray);
                        return true;
                    // when button is released
                    case MotionEvent.ACTION_UP:
                        dir = 's';
                        bArray = intToByteArray(type, dPower, dir);
                        stop(bArray);
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
                char type = 'd';
                byte[] bArray;
                char dir = 'r';
                bArray = intToByteArray(type, dPower, dir);
                switch(event.getAction()) {
                    // when button is pushed down
                    case MotionEvent.ACTION_DOWN:
                        goRight(bArray);
                        return true;
                    // when button is released
                    case MotionEvent.ACTION_UP:
                        dir = 's';
                        bArray = intToByteArray(type, dPower, dir);
                        stop(bArray);
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
                    joystick.setVisibility(View.INVISIBLE);
                    buttonForward.setVisibility(View.VISIBLE);
                    buttonLeft.setVisibility(View.VISIBLE);
                    buttonRight.setVisibility(View.VISIBLE);
                    buttonReverse.setVisibility(View.VISIBLE);
                    group.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                } else {
                    // Joystick enabled
                    joystick.setVisibility(View.VISIBLE);
                    buttonForward.setVisibility(View.INVISIBLE);
                    buttonLeft.setVisibility(View.INVISIBLE);
                    buttonRight.setVisibility(View.INVISIBLE);
                    buttonReverse.setVisibility(View.INVISIBLE);
                    group.setVisibility(View.INVISIBLE);
                    text.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public byte[] intToByteArray(char type, int power, char direction) {
        return new byte[] {
                (byte)type,
                (byte)power,
                (byte)direction};
    }

    private void goForward(byte[] power) {
        if(btSocket != null ){
            try{
                btSocket.getOutputStream().write(power);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void goReverse(byte[] power){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write(power);
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void goRight(byte[] power){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write(power);
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void goLeft(byte[] power){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write(power);
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void goLeftFront(byte[] power){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write(power);
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void goRightFront(byte[] power){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write(power);
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void goLeftBottom(byte[] power){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write(power);
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void goRightBottom(byte[] power){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write(power);
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void stop(byte[] power){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write(power);
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    /* void goSlow(){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write("1".getBytes());
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void goMedium(){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write("2".getBytes());
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }

    private void goFast(){
        if(btSocket != null){
            try{
                btSocket.getOutputStream().write("3".getBytes());
            } catch(IOException f){
                f.printStackTrace();
            }
        }
    }*/

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
