package com.program.hub;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

public class ConnectionSettingsActivity extends AppCompatActivity {

    private static final String CFG_FILE_NAME = "hub_cfg.txt";

    //Connection settings
    public static TextView IPandPort;

    public static EditText editText_IP;
    public static EditText editText_Port;

    //Buttons
    public static ImageButton imageButton_connection_settings_IPandPortSet;
    public static FloatingActionButton floatingActionButton_back;

    //List
    private static ListView listViewIPs;

    //Database
    private DatabaseHelper myDB;

    //BackupSave variable
    private ArrayList<String> listBackup = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_settings);

        listViewIPs = (ListView) findViewById(R.id.listViewIPs);
        reloadList();
        listViewIPs.setClickable(true);
        listViewIPs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String str = getItemFromList(position);
                String[] parts = str.split(":");
                setIP(parts[0],Integer.parseInt(parts[1]));
            }
        });
        listViewIPs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int pos = position;
                new AlertDialog.Builder(ConnectionSettingsActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Delete?")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yep", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeItem(pos);
                            }

                        })
                        .setNegativeButton("Nope", null)
                        .show();
                reloadList();
                return false;
            }
        });
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
                setIP(editText_IP.getText().toString(), Integer.parseInt(editText_Port.getText().toString()));
            }
        });
    }

    private void addData(String newEntry) {

        if(!listBackup.contains(newEntry)) {
            boolean insertData = myDB.addData(newEntry);

            if(insertData==true){
                //Data inserted
            }else{
                //Something went wrong
            }
        }


    }

    private void reloadActivity() {
        IPandPort.setText(Model.ip+":"+Model.port);
        IPandPort.setTextColor(Color.YELLOW);

        editText_IP.setText(Model.ip);
        editText_Port.setText(Integer.toString(Model.port));
    }

    private void setIP(String ip, Integer port) {
        Model.ip = ip;
        Model.port = port;
        addData(Model.ip + ":" + Model.port);
        Toast.makeText(getApplicationContext(),
                "IP changed to:" + Model.ip + ":" + Model.port,
                Toast.LENGTH_LONG)
                .show();
        MainActivity.print("New IP: "+ Model.ip+":"+Model.port,false);
        reloadList();
        reloadActivity();

    }

    private void removeItem(int position) {
        myDB = new DatabaseHelper(this);
        myDB.deleteOne(getItemFromList(position));
    }

    private String getItemFromList(int position) {
        myDB = new DatabaseHelper(this);

        //populate an ArrayList<String> from the database and then view it
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        if(data.getCount() == 0){
            //Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                theList.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
                listViewIPs.setAdapter(listAdapter);
            }
        }

        return theList.get(position);
    }

    private void reloadList() {
        myDB = new DatabaseHelper(this);

        //populate an ArrayList<String> from the database and then view it
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        if(data.getCount() == 0){
            //Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                theList.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
                listBackup = theList;
                listViewIPs.setAdapter(listAdapter);
            }
        }
    }

    private void back() {
        this.finish();
    }
}
