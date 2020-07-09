package com.program.hub;

import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection extends AsyncTask<String, Void, String> {

    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream in;
    private DataOutputStream out;

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
        String res = "error";

        try {
            socket = new Socket();

            socket.connect(new InetSocketAddress(host, port), 1000);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF(string);


            res = in.readUTF();


            socket.close();

        } catch (IOException e) {
            return null;
        }

        return res;
    }


}
