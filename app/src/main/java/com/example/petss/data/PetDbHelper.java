package com.example.petss.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.petss.data.PetContract.PetEntry.TABLE_NAME;


public class PetDbHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "petsDatabase.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CREATE_TABLE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " ( " +
            PetContract.PetEntry.PET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PetContract.PetEntry.PET_NAME + " TEXT NOT NULL, " +
            PetContract.PetEntry.PET_BREED + " TEXT, " +
            PetContract.PetEntry.PET_GENDER + " INTEGER DEFAULT 0, " +
            PetContract.PetEntry.PET_WEIGHT + " INTEGER );" ;

    public PetDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    public static final String DROP_TABLE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;


    @Override
    public void onCreate(SQLiteDatabase db) {

         db.execSQL(CREATE_TABLE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_TABLE_ENTRIES);
        onCreate(db);

    }
}
