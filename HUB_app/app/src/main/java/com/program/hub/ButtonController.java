package com.program.hub;

import android.graphics.Color;
import android.view.Display;
import android.view.View;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class ButtonController {

    ButtonController() {

        MainActivity.button_check_connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkconnection();
            }
        });
        MainActivity.imageButton_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                power();
            }
        });
    }

    private void power() {
        Connection connection = new Connection(Model.ip, Model.port);
        try {
            MainActivity.print("Sending: 110  (shutdown)", false);
            String result = connection.execute("110").get();
            if(result.equals("210")) {
                MainActivity.print("Recived: 210  (shutdown_ok)", false);
            } else {
                MainActivity.print("Recived: " + result, true);
            }
        } catch (Exception e) {
            MainActivity.print("Could not get response", true);
        }

    }

    private void checkconnection() {
        Connection connection = new Connection(Model.ip, Model.port);
        try {
            MainActivity.print("Sending: 101  (check_connexion)", false);
            String result = connection.execute("101").get();
            if(result.equals("201")) {
                MainActivity.print("Recived: 201  (check_connexion_ok)", false);
                MainActivity.setConnectionStatus(true);
            } else {
                MainActivity.print("Recived: " + result, true);
                MainActivity.setConnectionStatus(false);
            }
        } catch (Exception e) {
            MainActivity.print("Could not get response", true);
            MainActivity.setConnectionStatus(false);
        }

    }

}
