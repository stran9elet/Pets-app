package com.example.petss;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.petss.data.*;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private PetDbHelper mDbHelper;
    private PetCursorAdapter petCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new PetDbHelper(this);
        final ListView listView = (ListView) findViewById(R.id.list_view_pet);
        listView.setEmptyView((View) findViewById(R.id.empty_view));


        petCursorAdapter = new PetCursorAdapter(this, null);

        listView.setAdapter(petCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long item_id = petCursorAdapter.getItemId(position);
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                Uri uri = ContentUris.withAppendedId(PetContract.PetEntry.CONTENT_URI, item_id);
                i.setData(uri);
                startActivity(i);
            }
        });

        getLoaderManager().initLoader(PetContract.PetEntry.URL_PET_LOADER, null, this);


    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.

//        PetDbHelper mDbHelper = new PetDbHelper(this);

        // Create and/or open a database to read from it

//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        String table = PetContract.PetEntry.TABLE_NAME;

        String[] columns = {PetContract.PetEntry.PET_ID, PetContract.PetEntry.PET_NAME, PetContract.PetEntry.PET_BREED, PetContract.PetEntry.PET_GENDER, PetContract.PetEntry.PET_WEIGHT};

//        Cursor cursor = db.query(table,columns ,null,null,null,null,null);
        Cursor cursor = getContentResolver().query(PetContract.PetEntry.CONTENT_URI, columns, null, null, null);

        ListView listView = (ListView) findViewById(R.id.list_view_pet);
        listView.setEmptyView((View) findViewById(R.id.empty_view));
        petCursorAdapter = new PetCursorAdapter(this, cursor);
        listView.setAdapter(petCursorAdapter);
//        TextView displayView = (TextView) findViewById(R.id.text_view_pet);
//        displayView.setText("Number of rows in pets database table: " + PetProvider.class.getSimpleName());
//        displayView.append("\n");
//        displayView.append("\n" + PetContract.PetEntry.PET_ID + " - " + PetContract.PetEntry.PET_NAME + " - " + PetContract.PetEntry.PET_BREED + " - " + PetContract.PetEntry.PET_GENDER + " - " + PetContract.PetEntry.PET_WEIGHT);
//        displayView.append("\n");

//        try {

//            while (cursor.moveToNext()){
//
//                int columnIdIndex =  cursor.getColumnIndex(PetContract.PetEntry.PET_ID);
//                int columnNameIndex = cursor.getColumnIndex(PetContract.PetEntry.PET_NAME);
//                int columnBreedIndex = cursor.getColumnIndex(PetContract.PetEntry.PET_BREED);
//                int columnGenderIndex = cursor.getColumnIndex(PetContract.PetEntry.PET_GENDER);
//                int columnWeightIndex = cursor.getColumnIndex(PetContract.PetEntry.PET_WEIGHT);
//
//                int id = cursor.getInt(columnIdIndex);
//                String name = cursor.getString(columnNameIndex);
//                String breed = cursor.getString(columnBreedIndex);
//                int genderInt = cursor.getInt(columnGenderIndex);
//                String gender;
//                switch(genderInt){
//                    case 1:
//                        gender = "Male";
//                        break;
//                    case 2:
//                        gender = "Female";
//                        break;
//                    default:
//                        gender = "Unknown";
//                }
//                int weight = cursor.getInt(columnWeightIndex);
//
//                displayView.append("\n" + id + " - " + name + " - " + breed + " - " + gender + " - " + weight);
//            }
//
//        } finally {
//            // Always close the cursor when you're done reading from it. This releases all its
//            // resources and makes it invalid.
//            cursor.close();
//        }
//    }
    }

    private void insertDummyPet() {

//        SQLiteDatabase database = mDbHelper.getWritableDatabase();
//
        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.PET_NAME, "Garfield");
        values.put(PetContract.PetEntry.PET_BREED, "Tabby");
        values.put(PetContract.PetEntry.PET_GENDER, PetContract.PetEntry.GENDER_MALE);
        values.put(PetContract.PetEntry.PET_WEIGHT, 14);
//
//        long newRowId  = database.insert(PetContract.PetEntry.TABLE_NAME,null,values);
//
//        Log.v("MainActivity","new row id is " + newRowId);

        getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, values);

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        displayDatabaseInfo();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertDummyPet();
//                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                getContentResolver().delete(PetContract.PetEntry.CONTENT_URI, null, null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] columns = {PetContract.PetEntry.PET_ID, PetContract.PetEntry.PET_NAME, PetContract.PetEntry.PET_BREED, PetContract.PetEntry.PET_GENDER, PetContract.PetEntry.PET_WEIGHT};
//        Cursor cursor = getContentResolver().query(PetContract.PetEntry.CONTENT_URI, columns, null, null, null);
        return new CursorLoader(MainActivity.this, PetContract.PetEntry.CONTENT_URI, columns, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        petCursorAdapter.swapCursor(data);
//        petCursorAdapter = new PetCursorAdapter(this,data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        petCursorAdapter.swapCursor(null);
    }
}