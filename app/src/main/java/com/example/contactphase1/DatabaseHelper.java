package com.example.contactphase1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final int DB_VERSION=2;
    public static final String DB_NAME="ContactList.db";

    // Table Name
    public static final String TABLE_NAME = "CONTACTS";

    //Table columns
    public static final String ID = "id";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String DATE_BIRTH = "birthDate";
    public static final String FIRST_CONTACT = "firstContact";

    // Table Columns (MAPS) ++ PHASE 4
    public static final String ADDRESS_1 = "address1";
    public static final String ADDRESS_2 = "address2";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String ZIPCODE = "zipcode";

    //Table query
    private static final String CREATE_TABLE= "CREATE TABLE IF NOT EXISTS "+TABLE_NAME
            +"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FIRST_NAME + " TEXT NOT NULL, "
            + LAST_NAME + " TEXT NOT NULL, "
            + PHONE_NUMBER + " TEXT NOT NULL, "
            + DATE_BIRTH + " TEXT NOT NULL, "
            + FIRST_CONTACT + " TEXT NOT NULL, "

            // Map Activity Datas ++
            + ADDRESS_1 + " TEXT NOT NULL, "
            + ADDRESS_2 + " TEXT NOT NULL, "
            + CITY + " TEXT NOT NULL, "
            + STATE + " TEXT NOT NULL, "
            + ZIPCODE + " TEXT " + ");";

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Database ++
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
