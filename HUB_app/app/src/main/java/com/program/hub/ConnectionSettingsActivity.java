package com.program.hub;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ConnectionSettingsActivity extends AppCompatActivity {

    //Connection settings
    public static TextView IPandPort;


    public static EditText editText_IP;
    public static EditText editText_Port;


    //Buttons
    public static ImageButton imageButton_connection_settings_IPandPortSet;

    public static FloatingActionButton floatingActionButton_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_settings);

        //Connection settings
        imageButton_connection_settings_IPandPortSet = findViewById(R.id.imageButton_connection_settings_IPandPortSet);
        floatingActionButton_back = findViewById(R.id.floatingActionButton_back);

        editText_IP = findViewById(R.id.editText_IP);
        editText_Port = findViewById(R.id.editText_Port);

        IPandPort = findViewById(R.id.textView_IPSetted);
        //Set IP and Port
        reloadActivity();

        this.floatingActionButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        this.imageButton_connection_settings_IPandPortSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIP();
            }
        });
    }


    private void reloadActivity() {
        IPandPort.setText(Model.ip+":"+Model.port);
        IPandPort.setTextColor(Color.YELLOW);
    }

    private void setIP() {
        Model.ip = this.editText_IP.getText().toString();
        Model.port = Integer.parseInt(this.editText_Port.getText().toString());
        MainActivity.print("New IP: "+ Model.ip+":"+Model.port,false);
        reloadActivity();
    }

    private void back() {
        this.finish();
    }
}
