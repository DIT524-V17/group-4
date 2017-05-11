package com.smartcar.team4.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent newint = getIntent();
        //Calls the bluetooth connection method.

        //setContentView(R.layout.activity_menu);
        setContentView(R.layout.activity_menu);


        Button button_controller = (Button) findViewById(R.id.button_launchController);
        button_controller.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MenuActivity.this, ControllerActivity.class);
                MenuActivity.this.startActivity(myIntent);
                char controller = 'x';
                byte [] mode = intToByteArray('d', 0, controller);
                Client client = new Client(mode);
                client.execute();
            }
        });





        Button button_dancing = (Button) findViewById(R.id.button_launchDancing);
        button_dancing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MenuActivity.this, SongPicker.class);
                MenuActivity.this.startActivity(myIntent);
                ClientChar client = new ClientChar('x');
                client.execute();
            }
        });

    }

    public byte[] intToByteArray(char mode) {
        return new byte[] {
                (byte) mode};
    }

    public byte[] intToByteArray(char type, int power, char direction) {
        return new byte[] {
                (byte)type,
                (byte)power,
                (byte)direction};
    }


}
