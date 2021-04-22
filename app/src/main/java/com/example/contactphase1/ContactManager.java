package com.example.contactphase1;

import android.content.Context;
import android.util.Log;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.ArrayList;
/*
 * jxk161230 cs4301.002 ContactPhase1
 * */

public class ContactManager {

    ArrayList<Contact> mContactList = new ArrayList<>();
    RandomAccessFile raf;
    File mFilePath;

    public ContactManager() {
        //no arg constructor
    }

    /*-------------------------------------------------------
     * READ IN THE FILE AND STORE INTO ARRAYLIST: jxk161230
     * ------------------------------------------------------*/
    public ContactManager(Context context) {
        File file = new File(context.getFilesDir(), "ContactList.txt");
        mFilePath = file;
        Log.i("filepath____", String.valueOf(mFilePath));

        try {
            // Create random access file
            RandomAccessFile raf = new RandomAccessFile(mFilePath, "rw");
            raf.seek(0);

            if(raf.length()==0){
                Log.d("nothing in the file", "");
                return;
            }

            int b=0;
            long length = raf.length();

            while(b < length) {

                // Deleted contacts are empty 80 bytes, need to skip empty bytes
                // So its not recycled in view
                byte[] fb= new byte[1];
                raf.read(fb);
                int i=new BigInteger(fb).intValue();
                raf.seek(b);
                if(i==0){
                    raf.skipBytes(80); // Skip 80 bytes since each entries are 80
                    b+=80; // Increase amount of byte read by 80
                    continue;
                }


                raf.seek(b+0);
                byte[] firstName = new byte[25];
                raf.read(firstName);
                raf.seek(b+25);
                byte[] lastName = new byte[25];
                raf.read(lastName);
                raf.seek(b+50);
                byte[] phoneNumber = new byte[10];
                raf.read(phoneNumber);
                raf.seek(b+60);
                byte[] dateBirth = new byte[10];
                raf.read(dateBirth);
                raf.seek(b+70);
                byte[] firstContact = new byte[10];
                raf.read(firstContact);

                String fName = new String(firstName);
                String lName = new String(lastName);
                String pNumber = new String(phoneNumber);
                String dBirth = new String(dateBirth);
                String fContact = new String(firstContact);


                // Trim to remove space
                Contact contact = new Contact(fName.trim(), lName.trim(), pNumber.trim(), dBirth.trim(), fContact.trim());
                mContactList.add(contact); // Add to arraylist

                // Each new contact is after 80 bytes
                b+=80;

            }
            // Close File
            raf.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    /*--------------------------------------------
     * ADD NEW CONTACT INTO DATABASE : jxk161230
     * -------------------------------------------*/
    public void addContact(String fn, String ln, String pn, String dob, String fdc){

        // Add new contact
        Contact contact = new Contact(fn,ln,pn,dob,fdc);


        // Create new entry in the file
        try {
            //Find file
            RandomAccessFile raf= new RandomAccessFile(mFilePath, "rw");


            long endOfFile= raf.length();

            // Adding new entry to the end of the file
            raf.seek(endOfFile);


            //Writing to the file
            raf.write(contact.getF_name().getBytes());
            raf.seek(endOfFile+25);
            raf.write(contact.getL_name().getBytes());
            raf.seek(endOfFile+50);
            raf.write(contact.getPhoneNum().getBytes());
            raf.seek(endOfFile+60);
            raf.write(contact.getDob().getBytes());
            raf.seek(endOfFile+70);
            raf.write(contact.getF_contact().getBytes());
            raf.seek(endOfFile+80);

            raf.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        mContactList.add(contact);
    }

    /*--------------------------------------------
     * ++ DELETE CONTACT FROM DATABASE : jxk161230
     * -------------------------------------------*/
    public void deleteContact(int CurrentView){

        try {
            //Find file
           RandomAccessFile raf= new RandomAccessFile(mFilePath, "rw");

           // Find position in file
            int ContactIndex = CurrentView*80;
            raf.seek(ContactIndex);
            byte[] bytes= new byte[80];
            raf.write(bytes);


            raf.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //mContactList.remove(CurrentView);



    }


    /*--------------------------------------------
     * ++ EDIT CONTACT IN DATABASE : jxk161230
     * -------------------------------------------*/
    public void editContact(Contact contact, int currentView) {
        // Each record is 80 bytes, find byte index of the record
        int currentIndex = currentView * 80;
        int cI= (currentView)*80;

        try {
            //Find file
            RandomAccessFile raf = new RandomAccessFile(mFilePath, "rw");

            deleteContact(currentView);

//            // seek to the index
//            raf.seek(currentIndex);
//            byte[] bytes= new byte[80];
//            raf.write(bytes);

            raf.seek(cI);

            raf.write(contact.getF_name().getBytes());
            raf.seek(cI + 25);
            raf.write(contact.getL_name().getBytes());
            raf.seek(cI + 50);
            raf.write(contact.getPhoneNum().getBytes());
            raf.seek(cI + 60);
            raf.write(contact.getDob().getBytes());
            raf.seek(cI + 70);
            raf.write(contact.getF_contact().getBytes());
            raf.seek(cI + 80);

            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public ArrayList<Contact> loadContactList() {

         //Initial sample data
        Contact cnt = new Contact("Jisoo", "Kim","123546784","12/04/1992","12/04/1992");
        Contact cnt3 = new Contact("Seokjin", "Kim","123546784","12/04/1992","12/04/1992");
        Contact cnt2 = new Contact("Jimin", "Park","123546784","11/04/1993","11/04/1993");
        Contact cnt4 = new Contact("Yoongi", "Min","123546784","10/04/1994","10/04/1994");
        mContactList.add(cnt);
        mContactList.add(cnt2);
        mContactList.add(cnt3);
        mContactList.add(cnt4);


        return mContactList;
    }


}
