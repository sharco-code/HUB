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
                Connection connection = new Connection(Model.ip, Model.port);
                connection.execute("apagar");
            }
        });
    }

    private void checkconnection() {

        Connection connection = new Connection(Model.ip, Model.port);

        MainActivity.textView_connection_status.setText("Waiting...");
        MainActivity.textView_connection_status.setTextColor(Color.GRAY);


        try {
            if(connection.execute("check_connexion").get().equals("check_complete")) {
                MainActivity.textView_connection_status.setText("Connected");
                MainActivity.textView_connection_status.setTextColor(Color.GREEN);
            }
        } catch (Exception e) {
            MainActivity.textView_connection_status.setText("Connection Error");
            MainActivity.textView_connection_status.setTextColor(Color.RED);
        }

    }

}
