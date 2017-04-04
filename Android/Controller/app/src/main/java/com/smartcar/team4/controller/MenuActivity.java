package com.smartcar.team4.controller;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import static android.R.attr.value;

public class MenuActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_menu);


        Button button_controller = (Button) findViewById(R.id.button_launchController);
        button_controller.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MenuActivity.this, ControllerActivity.class);
                MenuActivity.this.startActivity(myIntent);
            }
        });

        Button button_dancing = (Button) findViewById(R.id.button_launchDancing);
        button_dancing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MenuActivity.this, DancingActivity.class);
                MenuActivity.this.startActivity(myIntent);
            }
        });

        Button button_connect = (Button) findViewById(R.id.button_connectToBt);
        button_connect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                Recieve the adress from Bluetooth class
            Intent newint = getIntent();
                address = newint.getStringExtra(Bluetooth.EXTRA_ADDRESS);

                //Calls the bluetooth connection method.
                new MenuActivity.ConnectToBt().execute();

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

    //Bluetooth connection method.
    private class ConnectToBt extends AsyncTask<Void, Void, Void>
    {
        private boolean success = true;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(MenuActivity.this, "Connecting...", "...");
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
