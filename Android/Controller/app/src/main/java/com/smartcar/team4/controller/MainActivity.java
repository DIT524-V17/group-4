package com.smartcar.team4.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;
import com.smartcar.team4.controller.JoystickView.OnJoystickMoveListener;


public class MainActivity extends AppCompatActivity {

    private JoystickView joystick;
    TextView test;
    Button btnDis;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //Not sure if the UUID is correct..
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Recieve the adress from Bluetooth class
        Intent newint = getIntent();
        address = newint.getStringExtra(Bluetooth.EXTRA_ADDRESS);

        //Calls the bluetooth connection method.
        new ConnectToBt().execute();

        //Get disconnect button
        btnDis = (Button)findViewById(R.id.disconnect_btn);

        btnDis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
            }
        });

        joystick = (JoystickView) findViewById(R.id.game);
        joystick.setOnJoystickMoveListener(new OnJoystickMoveListener() {

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



        // Button for turning right
//        joystick = (JoystickView) findViewById(R.id.game);
//            joystick.setOnJoystickMoveListener(new OnJoystickMoveListener() {
//            @Override
//            public void onValueChanged(int angle, int power, int direction) {
//                switch(direction) {
//                    // when button is pushed down
//                    case JoystickView.FRONT:
//                        goForward();
//                        break;
//                        // when button is released
//                    default:
//                        stop();
//                        break;
//                }
//            }
//        }, JoystickView.DEFAULT_LOOP_INTERVAL);


        // Button for turning left
//        Button buttonReverse = (Button) findViewById(R.id.button_reverse);
//        buttonReverse.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    // when button is pushed down
//                    case MotionEvent.ACTION_DOWN:
//                        goReverse();
//                        return true;
//                    // when button is released
//                    case MotionEvent.ACTION_UP:
//                        stop();
//                        return true;
//                }
//                return false;
//            }
//        });

//        Button buttonLeft = (Button) findViewById(R.id.button_left);
//        buttonLeft.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    // when button is pushed down
//                    case MotionEvent.ACTION_DOWN:
//                        goLeft();
//                        return true;
//                    // when button is released
//                    case MotionEvent.ACTION_UP:
//                        stop();
//                        return true;
//                }
//                return false;
//            }
//        });
//
//        Button buttonRight = (Button) findViewById(R.id.button_right);
//        buttonRight.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    // when button is pushed down
//                    case MotionEvent.ACTION_DOWN:
//                        goRight();
//                        return true;
//                    // when button is released
//                    case MotionEvent.ACTION_UP:
//                        stop();
//                        return true;
//                }
//                return false;
//            }
//        });
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

    //Method for disconnect the bluetooth connection.
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

    //Bluetooth connection method.
    private class ConnectToBt extends AsyncTask<Void, Void, Void>
    {
        private boolean success = true;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "...");
        }

        @Override
        protected Void doInBackground(Void... devices)
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            }
            catch (IOException e)
            {
                success = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (!success)
            {
                Toast.makeText(getApplicationContext(), "Connection Failed. Try again.", Toast.LENGTH_LONG).show();
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Connected!", Toast.LENGTH_LONG).show();
                isBtConnected = true;
            }
            progress.dismiss();
        }


    }
}
