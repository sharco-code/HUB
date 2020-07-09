package com.program.hub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //HOME
    public static TextView textView_connection_status;

    public static Button button_check_connection;
    public static ImageButton imageButton_connection_settings;
    public static ImageButton imageButton_power;
    public static ImageButton imageButton_command;
    public static ImageButton imageButton_no;
    public static TextView textView_output;
    public static ScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollview = ((ScrollView) findViewById(R.id.scrollview_output));


        //HOME
        textView_connection_status = findViewById(R.id.textView_connection_status);
        textView_output = findViewById(R.id.textView_output);
        button_check_connection = findViewById(R.id.button_check_connection);
        imageButton_power = findViewById(R.id.imageButton_power);
        imageButton_command = findViewById(R.id.imageButton_command);
        imageButton_no = findViewById(R.id.imageButton_connection_settings);
        imageButton_connection_settings = findViewById(R.id.imageButton_connection_settings);

        ButtonController buttonController = new ButtonController();
        Model.init();

        MainActivity.imageButton_connection_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity_ConnectionSettings();
            }
        });
    }

    public void openActivity_ConnectionSettings() {
        Intent intent = new Intent(this, ConnectionSettingsActivity.class);
        startActivity(intent);
    }
    public static void setConnectionStatus(Boolean isConnected) {
        if(isConnected == null) {
            MainActivity.textView_connection_status.setText("Waiting...");
            MainActivity.textView_connection_status.setTextColor(Color.GRAY);
        } else if(isConnected) {
            MainActivity.textView_connection_status.setText("Connected");
            MainActivity.textView_connection_status.setTextColor(Color.GREEN);
        } else {
            MainActivity.textView_connection_status.setText("Connection Error");
            MainActivity.textView_connection_status.setTextColor(Color.RED);
        }
    }
    public static void print(String line, boolean isError) {
        if(isError) {
            textView_output.setText(textView_output.getText() + "\n[ERROR] " +line);
        } else {
            textView_output.setText(textView_output.getText() + "\n[LOG] " +line);
        }
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
