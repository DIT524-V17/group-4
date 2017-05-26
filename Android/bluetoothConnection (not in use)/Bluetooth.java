// @author Simon Löfving

package com.smartcar.team4.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;

public class Bluetooth extends AppCompatActivity {

    Button pairBtn;
    ListView deviceList;
    public static String EXTRA_ADDRESS = "device_address";

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        pairBtn = (Button) findViewById(R.id.pair_btn);
        deviceList = (ListView) findViewById(R.id.btList);


        pairBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Check if the device has bluetooth
                myBluetooth = BluetoothAdapter.getDefaultAdapter();

                //If not, the app give the user a messages and terminates
                if(myBluetooth == null)
                {
                    Toast.makeText(getApplicationContext(), "Your device doesn't support bluetooth. Good bye!", Toast.LENGTH_LONG).show();
                    finish();
                }
                //If the bluetooth is switched off, the app asks the user to turn it on.
                else if(!myBluetooth.isEnabled())
                {

                    Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnBTon,1);
                }
              //If everything is fine, the app opens and when clicking the pair button the method for showing the pairable devices runs.
                else{

                    pairedDevicesList();
                }
            }
        });

    }
    //Method to find pairable bluetooth devices
    private void pairedDevicesList()
    {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(myListClickListener);

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {

            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            //Intent to start a new activity and send bluetooth information with it.
            Intent nextActivity = new Intent(Bluetooth.this, MainActivity.class);

            nextActivity.putExtra(EXTRA_ADDRESS, address);
            startActivity(nextActivity);
        }
    };

}
