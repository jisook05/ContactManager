package com.example.contactphase1;
/*
 * jxk161230 cs4301.002 ContactPhase3
 * */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;


import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataManager {

    private ArrayList<Contact> contacts = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;

    public DataManager(Context c){
        context=c;
        dbHelper = new DatabaseHelper(context);
    }

    public DataManager open() throws SQLException{
        //dbHelper= new DatabaseHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        //fillContacts();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    /*--------------------------------------------
     * INSERT NEW CONTACT INTO DATABASE : jxk161230
     * -------------------------------------------*/
    public void addContact(String fn, String ln, String pn, String dob, String fdc, String addr1, @Nullable String addr2,String city, String state, String zipcode){

        // Add new contact
        ContentValues contentValues= new ContentValues();
            contentValues.put(DatabaseHelper.FIRST_NAME, fn);
            contentValues.put(DatabaseHelper.LAST_NAME, ln);
            contentValues.put(DatabaseHelper.PHONE_NUMBER, pn);
            contentValues.put(DatabaseHelper.DATE_BIRTH, dob);
            contentValues.put(DatabaseHelper.FIRST_CONTACT, fdc);

        // Phase 4 ++ columns
            contentValues.put(DatabaseHelper.ADDRESS_1, addr1);
            // if address 2 is not filled
            if(addr2==null){
                contentValues.putNull(DatabaseHelper.ADDRESS_2);
            }
            contentValues.put(DatabaseHelper.ADDRESS_2, addr2);
            contentValues.put(DatabaseHelper.CITY, city);
            contentValues.put(DatabaseHelper.STATE, state);
            contentValues.put(DatabaseHelper.ZIPCODE, zipcode);

        // Insert values into the sqlite database
        sqLiteDatabase.insert(DatabaseHelper.TABLE_NAME,null,contentValues);

        // close the db
        close();

    }

    /*--------------------------------------------
     * ++ DELETE CONTACT FROM DATABASE : jxk161230
     * -------------------------------------------*/
    public void deleteContact(Contact contact){

        String rowId=Long.toString(getContactPrimaryId(contact));

        sqLiteDatabase.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.ID+" = ?", new String[]{rowId+""});

        close();

    }


    /*-----------------------------------------------------------------
     * ++ EDIT CONTACT IN DATABASE : jxk161230
     *
     *
     *  ++ update= Adjusted whereClause so it looks for conditions
     *    (zipcode and phonenumber) from oldcontact
     * -------------------------------------------------------------------------------*/
    public void editContact(Contact oldcontact, Contact contact) {
        String rowId=Long.toString(getContactPrimaryId(oldcontact));
        Log.d("ID__________", rowId);
        String zID = oldcontact.zipcode;
        String pID = oldcontact.phoneNum;

        ContentValues contentValues= new ContentValues();
        contentValues.put(DatabaseHelper.FIRST_NAME, contact.f_name);
        contentValues.put(DatabaseHelper.LAST_NAME, contact.l_name);
        contentValues.put(DatabaseHelper.PHONE_NUMBER, contact.phoneNum);
        contentValues.put(DatabaseHelper.DATE_BIRTH, contact.dob);
        contentValues.put(DatabaseHelper.FIRST_CONTACT, contact.f_contact);

        // Phase 4 ++ columns
        contentValues.put(DatabaseHelper.ADDRESS_1, contact.addr1);
        if(contact.addr2==null){
            contentValues.putNull(DatabaseHelper.ADDRESS_2);
        }
        contentValues.put(DatabaseHelper.ADDRESS_2, contact.addr2);
        contentValues.put(DatabaseHelper.CITY, contact.city);
        contentValues.put(DatabaseHelper.STATE, contact.state);
        contentValues.put(DatabaseHelper.ZIPCODE, contact.zipcode);

        sqLiteDatabase.update(DatabaseHelper.TABLE_NAME,contentValues,DatabaseHelper.ZIPCODE+" = ? AND "+
                DatabaseHelper.PHONE_NUMBER+" = ? ", new String[]{zID+"", pID+""});

        close();

    }

    public ArrayList<Contact> loadContactList() {
        return contacts;
    }

    /*--------------------------------------------
     * ++ GET PRIMARY KEY OF CONTACT : jxk161230
     *  by querying using Contact info
     * -------------------------------------------*/
    public long getContactPrimaryId(Contact contact){
        long pi = 0;
        String[] columns = new String[]{DatabaseHelper.ID};
        String whereClause= DatabaseHelper.FIRST_NAME+" = ? AND "+
                DatabaseHelper.LAST_NAME + " = ? AND "+
                DatabaseHelper.PHONE_NUMBER + " = ? ";
        String[] whereargs = new String[]{
                contact.getF_name(),
                contact.getL_name(),
                contact.getPhoneNum(),
        };
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_NAME,
                columns,
                whereClause,
                whereargs,
                null, null,
                null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            pi = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.ID));
        }

        return pi;
    }

    //--------------------------------------
    // FILL CONTACTS FROM DB++ jxk161230
    //------------------------------------
    public void fillContacts(){
        //int id = 0;
        String[] columns = new String[] {DatabaseHelper.ID,
                DatabaseHelper.FIRST_NAME,
                DatabaseHelper.LAST_NAME,
                DatabaseHelper.PHONE_NUMBER,
                DatabaseHelper.DATE_BIRTH,
                DatabaseHelper.FIRST_CONTACT,

                DatabaseHelper.ADDRESS_1,
                DatabaseHelper.ADDRESS_2,
                DatabaseHelper.CITY,
                DatabaseHelper.STATE,
                DatabaseHelper.ZIPCODE
        };

        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_NAME, columns,
                null, null ,null, null,
                DatabaseHelper.LAST_NAME);
        while(cursor.moveToNext()){
            //String id=cursor.getString(cursor.getColumnIndex(DatabaseHelper.ID));
            String fname= cursor.getString(cursor.getColumnIndex(DatabaseHelper.FIRST_NAME));
            String lname= cursor.getString(cursor.getColumnIndex(DatabaseHelper.LAST_NAME));
            String pNumber= cursor.getString(cursor.getColumnIndex(DatabaseHelper.PHONE_NUMBER));
            String bDate= cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE_BIRTH));
            String fContact= cursor.getString(cursor.getColumnIndex(DatabaseHelper.FIRST_CONTACT));

            // Phase 4 + columns
            String addr1 = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDRESS_1));
            String addr2 = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDRESS_2));
            String _city = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CITY));
            String _state = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STATE));
            String zipcode = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ZIPCODE));


           // id++;
            contacts.add(new Contact(fname,lname,pNumber,bDate,fContact, addr1, addr2, _city,_state, zipcode));

        }
    }


    /*--------------------------------------------
     * ++REINITIALIZE DATABASE : jxk161230
     * -------------------------------------------*/
    public void reinitialize(){
        close();
        context.deleteDatabase(DatabaseHelper.DB_NAME);
        contacts.clear();
        open();

    }


}
