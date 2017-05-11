package com.smartcar.team4.controller;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by fwall on 2017-05-03.
 */

public class ClientChar extends AsyncTask<Void, Void, Void> {

    char message;
    String ipAddress = "192.168.42.1";
    int port = 9999;

    ClientChar(char c){
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