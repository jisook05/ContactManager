package com.example.contactphase1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * jxk161230 cs4301.002 ContactPhase1
 * */
public class MainActivity extends AppCompatActivity {
    static final int ADD=1;

    ArrayList<Contact> contacts = new ArrayList<>();
    RecyclerView contact_recycler;
    RecyclerView.Adapter mAdapter;
    ContactManager mContactManager;
    ContactArrayAdapter mContactArrayAdapter;
    LinearLayoutManager linearLayoutManager;
    FloatingActionButton floatingActionButton;
    File mFilePath;
    Context context;


    // ++
    // Used for SensorActivity
    private Sensor accelerometerSensor;
    private SensorManager mSensorManager;
    private SensorActivity shakeDetector;

    // DB
    private DataManager mDBManager;



    // This function is when first time the application gets loaded
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Connect to main xml containing recycler view

        // Recycler views for the contact
        // + recycler view provides ability to implement both horizontal and vertical layouts
        // RecyclerView --> Adapter --> Dataset
        contact_recycler= findViewById(R.id.contact_recycler);


        //mContactManager= new ContactManager(this);
        mDBManager = new DataManager(this);
        mDBManager.open();
        mDBManager.fillContacts();

        // Load arraylist of contacts to recyclerview
        contacts = mDBManager.loadContactList();


        ContactArrayAdapter caa = new ContactArrayAdapter(MainActivity.this, this, contacts );
        contact_recycler.setAdapter(caa);
        linearLayoutManager= new LinearLayoutManager(this);
        contact_recycler.setLayoutManager(linearLayoutManager);



        // Notify
        caa.notifyDataSetChanged();

        // ++
        // Sort alphabetically
        sortAlphabet(contacts);

        //++
        // Shake Detection
        // Sensor Initialization
        // ++ jxk161230
       // mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Accelerometer measures acceleration and force  of gravity applied to the device
//        accelerometerSensor= mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        shakeDetector = new SensorActivity();
//        shakeDetector.SetOnShakeListener(new SensorActivity.OnShakeListener() {
//            @Override
//            public void onShake(int Shakecount) {
//                Log.d("Sensor detected____", "sensor onShake");
//
//                // ++ reverse contact list Z-A
//                HandleShakeEvent(Shakecount);
//            }
//        });

        //Add new contact: fab button
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdd = new Intent(v.getContext(), ContactDetail.class);
                intentAdd.putExtra("code", ADD);
                startActivity(intentAdd);
//                intentAdd.putExtra("code", ADD);
                //startActivityForResult(intentAdd, ADD, null);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator=getMenuInflater();
        inflator.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //**************************
        // Reinitialize the database++ jxk161230
        //****************************
        if(item.getItemId()==R.id.delete_db){
            DataManager myDB= new DataManager(this);
            myDB.reinitialize();
            recreate();


            //**************************
            // Import++ jxk161230
            //****************************
        }else if(item.getItemId()==R.id.import_data){
            ContactManager ctm = new ContactManager(this);
            ctm.loadContactList();
            ContactArrayAdapter caa = new ContactArrayAdapter(MainActivity.this, this, contacts );
            contact_recycler.setAdapter(caa);
            linearLayoutManager= new LinearLayoutManager(this);
            contact_recycler.setLayoutManager(linearLayoutManager);
        }
        return super.onOptionsItemSelected(item);
    }

    // ++ register listener onResume()
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(shakeDetector, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    // ++ unregister listener onPause()
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(shakeDetector);

    }

//    //++Phase 2
//    // Reverse Contact List
//    // jxk161230
//    public void HandleShakeEvent(int count){
//        if(count!=0) {
//                mDBManager = new DataManager(this);
//
//                 //ContactManager contactMan = new ContactManager(this);
//                 ArrayList<Contact> cts = mDBManager.loadContactList();
//
//            sortAlphabet(cts);
//
//            if(count%2!=0){
//                //reverse contact
//                Collections.reverse(cts);
//            }
//
//            // Set Adaptor
//            mContactArrayAdapter = new ContactArrayAdapter(cts);
//            contact_recycler.setAdapter(mContactArrayAdapter);
//        }
//    }

    // ++Phase 2
    // Sort alphabetically
    // jxk161230
    public void sortAlphabet(ArrayList<Contact> cts){
        Collections.sort(cts, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getL_name().compareToIgnoreCase(o2.getL_name());
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
    }


    //********************************
    // Returning from adding new Contact
    //*******************************
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            recreate();
        }

    }


}
