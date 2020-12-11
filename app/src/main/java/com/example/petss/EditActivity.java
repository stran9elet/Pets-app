package com.example.petss;


import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.petss.data.*;


public class EditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    /**
     * EditText field to enter the pet's name
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the pet's breed
     */
    private EditText mBreedEditText;

    /**
     * EditText field to enter the pet's weight
     */
    private EditText mWeightEditText;

    /**
     * EditText field to enter the pet's gender
     */
    private Spinner mGenderSpinner;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = 0;
    private Uri uri;
    private PetDbHelper mDbHelper;
    private boolean mPetHasChanged = false;
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mPetHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        uri = intent.getData();


        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById(R.id.edit_name);
        mBreedEditText = findViewById(R.id.edit_breed);
        mWeightEditText = findViewById(R.id.edit_weight);
        mGenderSpinner = findViewById(R.id.gender_spinner);

        setupSpinner();

        if (uri == null) {
            setTitle("Add a Pet");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit Pet");
            getLoaderManager().initLoader(2, null, this);
        }

        mNameEditText.setOnTouchListener(touchListener);
        mBreedEditText.setOnTouchListener(touchListener);
        mWeightEditText.setOnTouchListener(touchListener);
        mGenderSpinner.setOnTouchListener(touchListener);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (uri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = PetContract.PetEntry.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = PetContract.PetEntry.GENDER_FEMALE; // Female
                    } else {
                        mGender = PetContract.PetEntry.GENDER_UNKNOWN; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_edit.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                String petName = mNameEditText.getText().toString().trim();
                String petBreed = mBreedEditText.getText().toString().trim();
                String weightString = mWeightEditText.getText().toString().trim();
                if (uri == null) {


                    ContentValues values = new ContentValues();
                    if(petName.equals("") || petBreed.equals("") || weightString.equals(""))  {
                        Toast.makeText(this, "Enter all the details", Toast.LENGTH_SHORT).show();

//                    if (TextUtils.isEmpty(petName) && TextUtils.isEmpty(petBreed) && TextUtils.isEmpty(weightString)) {
//                        finish();
                    }else{
                        int petWeight = Integer.parseInt(weightString.trim());

                        values.put(PetContract.PetEntry.PET_WEIGHT,petWeight);
                        values.put(PetContract.PetEntry.PET_NAME, petName);
                        values.put(PetContract.PetEntry.PET_BREED, petBreed);
                        values.put(PetContract.PetEntry.PET_GENDER, mGender);
//                    } else if ((!TextUtils.isEmpty(petName) && !TextUtils.isEmpty(petBreed) && TextUtils.isEmpty(weightString))) {
//                        values.put(PetContract.PetEntry.PET_WEIGHT, 0);
//                        values.put(PetContract.PetEntry.PET_NAME, petName);
//                        values.put(PetContract.PetEntry.PET_BREED, petBreed);
//                        values.put(PetContract.PetEntry.PET_GENDER, mGender);
//
//
////                database.insert(PetContract.PetEntry.TABLE_NAME,null,values);
//                        getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, values);
//
//                        Toast.makeText(this, "Pet Saved", Toast.LENGTH_SHORT).show();
//
//                        finish();
//                    } else {
//                        int petWeight = Integer.parseInt(mWeightEditText.getText().toString().trim());
//
//                        values.put(PetContract.PetEntry.PET_WEIGHT, petWeight);
//                        values.put(PetContract.PetEntry.PET_NAME, petName);
//                        values.put(PetContract.PetEntry.PET_BREED, petBreed);
//                        values.put(PetContract.PetEntry.PET_GENDER, mGender);


//                database.insert(PetContract.PetEntry.TABLE_NAME,null,values);
                        getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, values);
                        Toast.makeText(this, "Pet Saved", Toast.LENGTH_SHORT).show();
                        finish();
                    }
//                PetDbHelper mDbHelper = new PetDbHelper(this);
//               SQLiteDatabase database = mDbHelper.getWritableDatabase();


                } else {

                    int petWeight = Integer.parseInt(mWeightEditText.getText().toString().trim());

                    if(petName==null) {
                        Toast.makeText(this, "Enter all the details", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ContentValues values = new ContentValues();
                        values.put(PetContract.PetEntry.PET_NAME, petName);
                        values.put(PetContract.PetEntry.PET_BREED, petBreed);
                        values.put(PetContract.PetEntry.PET_GENDER, mGender);
                        values.put(PetContract.PetEntry.PET_WEIGHT, petWeight);

                        getContentResolver().update(uri, values, null, null);

                        Toast.makeText(this, "Pet updated successfully", Toast.LENGTH_SHORT).show();

                        finish();
                    }

                }

                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                int rowsDeleted = getContentResolver().delete(uri, null, null);
                finish();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                if (mPetHasChanged) {
                    // Navigate back to parent activity (CatalogActivity)
                    NavUtils.navigateUpFromSameTask(EditActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListenerObject = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(EditActivity.this);
                    }
                };
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


//        petCursorAdapter = new PetCursorAdapter(this,data);

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] columns = {PetContract.PetEntry.PET_ID, PetContract.PetEntry.PET_NAME, PetContract.PetEntry.PET_BREED, PetContract.PetEntry.PET_GENDER, PetContract.PetEntry.PET_WEIGHT};
//        Cursor cursor = getContentResolver().query(PetContract.PetEntry.CONTENT_URI, columns, null, null, null);
        return new CursorLoader(this, uri, columns, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        EditText nameEditText = (EditText) findViewById(R.id.edit_name);
//        EditText breedEditText = (EditText) findViewById(R.id.edit_breed);
//        EditText weightEditText = (EditText) findViewById(R.id.edit_weight);
//        Spinner genderSpinner = (Spinner) findViewById(R.id.gender_spinner);


        if (data.moveToFirst()) {

            String name = data.getString(data.getColumnIndex(PetContract.PetEntry.PET_NAME));
            String breed = data.getString(data.getColumnIndex(PetContract.PetEntry.PET_BREED));
            int weight = Integer.parseInt(data.getString(data.getColumnIndex(PetContract.PetEntry.PET_WEIGHT)));
            int gender = Integer.parseInt(data.getString(data.getColumnIndex(PetContract.PetEntry.PET_GENDER)));

            switch (gender) {
                case PetContract.PetEntry.GENDER_MALE:
                    mGenderSpinner.setSelection(PetContract.PetEntry.GENDER_MALE);
                    break;
                case PetContract.PetEntry.GENDER_FEMALE:
                    mGenderSpinner.setSelection(PetContract.PetEntry.GENDER_FEMALE);
                    break;
                default:
                    mGenderSpinner.setSelection(0);
                    break;
            }

            mNameEditText.setText(name);
            mBreedEditText.setText(breed);
            mWeightEditText.setText(Integer.toString(weight));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText(null);
        mBreedEditText.setText(null);
        mWeightEditText.setText(null);
        mGenderSpinner.setSelection(PetContract.PetEntry.GENDER_UNKNOWN);

    }

    public void showDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard changes?");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("keep editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {

        if (mPetHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        };

        showDialog(discardClickListener);

    }

}