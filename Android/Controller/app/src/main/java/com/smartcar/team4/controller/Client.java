// @Author - Filip and Karan
package com.smartcar.team4.controller;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client extends AsyncTask<Void, Void, Void> {

    byte[] message;
    String ipAddress = "192.168.42.1";
    int port = 9998;

    Client(byte[] c){
        message = c;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Socket socket = new Socket(ipAddress, port);
            OutputStream output = socket.getOutputStream();
            output.write(message);
            output.flush();

            socket.close();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
