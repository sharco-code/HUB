package com.program.hub;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection extends AsyncTask<String, Void, String> {

    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    private String host;
    private int port;

    Connection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void re(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected String doInBackground(String... voids) {

        String string = voids[0];
        String msg = "error";

        try {
            socket = new Socket();

            socket.connect(new InetSocketAddress(host, port), 1000);


            printWriter = new PrintWriter(socket.getOutputStream(), true);

            printWriter.write(string);
            printWriter.flush();

            bufferedReader =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = bufferedReader.readLine(), result = "";
            while (line != null) {
                result += line;
                line = bufferedReader.readLine();
            }

            msg = result;

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return msg;
    }


}
