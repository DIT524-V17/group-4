//@Author Filip
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

        //setContentView(R.layout.activity_menu);
        setContentView(R.layout.activity_menu);


        Button button_controller = (Button) findViewById(R.id.button_launchController);
        button_controller.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MenuActivity.this, ControllerActivity.class);
                MenuActivity.this.startActivity(myIntent);
            }
        });


        final Button button_dancing = (Button) findViewById(R.id.button_launchDancing);
        button_dancing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MenuActivity.this, DanceControllerActivity.class);
                MenuActivity.this.startActivity(myIntent);
            }
        });


    }

}
