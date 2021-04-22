package com.example.contactphase1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
/*
 * jxk161230 cs4301.002 ContactPhase1
 * */

public class ContactDetail extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG= "ContactDetail";
    private static final int EDIT_SAVE= 1;
    private static final int DELETE= 2;

    ContactArrayAdapter mContactArrayAdapter;
    ImageView prof_pic;
    EditText fname, lname, pNumber, dob, fdc;
    EditText addr1, addr2, city, state, zip;
    Button save, delete, button, button2;
    int AddCode;
    Contact contact, oldContact;
    boolean db;
    int CurrentView;
    int editCode;

    ContactManager mContact;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail);

        // mContact = new ContactManager(this);


        //Get intents
        Intent intent= getIntent();
        intent.getExtras();
        AddCode = intent.getIntExtra("code", -1);
        CurrentView = intent.getIntExtra("CurrentView",-1);
        contact = (Contact) intent.getSerializableExtra("Contact");
        oldContact = contact;
        editCode = intent.getIntExtra("editCode",-1);

        // Find views
        prof_pic= findViewById(R.id.id_image);

        // General info
        fname = (EditText)findViewById(R.id.first_name);
        lname = (EditText)findViewById(R.id.last_name);
        pNumber = (EditText)findViewById(R.id.phoneNumber);

        // Dates
        dob = (EditText)findViewById(R.id.dob);
        fdc = (EditText)findViewById(R.id.firstcontact);

        // Address Views
        addr1 = (EditText)findViewById(R.id.address1);
        addr2 = (EditText)findViewById(R.id.address2);
        city = (EditText)findViewById(R.id.city);
        state = (EditText)findViewById(R.id.state);
        zip = (EditText)findViewById(R.id.zipcode);


        /*-------------------------------------
         * SAVE BUTTON: after editing: jxk161230
         *-------------------------------------- */
        save = (Button)findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager mData = new DataManager(ContactDetail.this);
                mData.open();

                // Get string values
                String firstName = fname.getText().toString().trim();
                String lastName = lname.getText().toString().trim();
                String phoneNumber = pNumber.getText().toString().trim().replace("-","");
                String dateBirth = dob.getText().toString().trim();
                String firstContact = fdc.getText().toString().trim();

                // Phase 4 ++ columns
                String address1 = addr1.getText().toString().trim();
                String address2 = addr2.getText().toString().trim();
                String _city = city.getText().toString().trim();
                String _state = state.getText().toString().toUpperCase().trim();
                String zipcode = zip.getText().toString().trim();

                if(editCode==1){


                    // Edit existing contact
                    contact.setF_name(firstName);
                    contact.setL_name(lastName);
                    contact.setPhoneNum(phoneNumber);
                    contact.setDob(dateBirth);
                    contact.setF_contact(firstContact);

                    // Phase 4 ++ columns
                    contact.setAddr1(address1);
                    contact.setAddr2(address2);
                    contact.setCity(_city);
                    contact.setState(_state);
                    contact.setZipcode(zipcode);


                    //mContact.editContact(contact, CurrentView);
 

                    //update data
                   // mData.editContact(contact, editContact, String.valueOf(CurrentView));
                    CurrentView= CurrentView+1;
                   // mData.editContact(contact, String.valueOf(CurrentView));
                    mData.editContact(oldContact, contact);


                    returnToMain();


                }else if(AddCode==1){
                    // Add new contact
                             //mContact.addContact(firstName, lastName, phoneNumber, dateBirth,firstContact);

                    mData.addContact(firstName, lastName, phoneNumber, dateBirth,firstContact, address1, address2, _city, _state, zipcode);



                    returnToMain();


                }

            }
        });



        /*-------------------------------------
         * DELETE BUTTON: after deleting: jxk161230
         *-------------------------------------- */
        delete = (Button)findViewById(R.id.delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager mData = new DataManager(ContactDetail.this);
                mData.open();


                //delete from database
                mData.deleteContact(contact);



                returnToMain();



            }
        });

        // Set the view with existing contact
        if(editCode==1){
            fname.setText(contact.getF_name());
            lname.setText(contact.getL_name());
            pNumber.setText(contact.getPhoneNum());
            dob.setText(contact.getDob());
            fdc.setText(contact.getF_contact());

            // Phase 4 ++ columns
            addr1.setText(contact.getAddr1());
            addr2.setText(contact.getAddr2());
            city.setText(contact.getCity());
            state.setText(contact.getState());
            zip.setText(contact.getZipcode());
        }

        //Date picker buttons
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);

        //Fragments
        FragmentManager fragmentManager = getSupportFragmentManager();



        /*--------------------------------
        * DatePicker Fragment: jxk161230
        * ---------------------------------*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DateFragment();
                newFragment.show(getSupportFragmentManager(), "DatePicker");
                db=true;
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DateFragment();
                newFragment.show(getSupportFragmentManager(), "DatePicker");
                db=false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator=getMenuInflater();
        inflator.inflate(R.menu.contact_map_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //PHASE 4
    /*--------------------------------------
     * Move to Google Maps Activity++ : jxk161230
     * -------------------------------------*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.map_address){
            // Get address of contact
            String address= addr1.getText().toString().replace(" ","+").trim();
            String _city = city.getText().toString().replace(" ","+").trim();
            String _state_ = state.getText().toString().replace(" ","+").toUpperCase().trim();


            // Start MapsActivity
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("Address", address);
            intent.putExtra("City", _city);
            intent.putExtra("State",_state_);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*--------------------------------------
    * Return to MainActivity: jxk161230
    * -------------------------------------*/
    public void returnToMain(){
        Intent main_intent = new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main_intent);
    }


    // Check if all entries are filled
    private boolean isFilled() {
        if(fname.getText().toString().trim().length() == 0&&
                lname.getText().toString().trim().length() == 0&&
                pNumber.getText().toString().trim().length() == 0&&
                dob.getText().toString().trim().length() == 0&&
                fdc.getText().toString().trim().length() == 0){
            return true;
        }else{
            return false;
        }
    }

    // Date selection
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if(db){
            if(month<10){
                dob.setText("0"+(month+1)+"/"+dayOfMonth+"/"+year);
            }else if(dayOfMonth<10){
                dob.setText((month + 1) + "/" + "0" +dayOfMonth + "/" + year);
            }else if(month<10&&dayOfMonth<10){
                dob.setText("0"+(month + 1) + "/" + "0"+dayOfMonth + "/" + year);
            }else {
                dob.setText((month + 1) + "/" + dayOfMonth + "/" + year);
            }
        }else{
            if(month<10){
                fdc.setText("0"+(month+1)+"/"+dayOfMonth+"/"+year);
            }else if(dayOfMonth<10){
                fdc.setText((month + 1) + "/" + "0" +dayOfMonth + "/" + year);
            }else if(month<10&&dayOfMonth<10){
                fdc.setText("0"+(month + 1) + "/" + "0"+dayOfMonth + "/" + year);
            }else {
                fdc.setText((month + 1) + "/" + dayOfMonth + "/" + year);
            }

        }

    }
}
