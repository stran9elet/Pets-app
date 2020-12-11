package com.example.petss.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PetProvider extends ContentProvider {


    private static final UriMatcher PetUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int PETS_CODE = 100;
    private static final int PET_ROW_CODE = 101;
    static{

        PetUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS,PETS_CODE);
        PetUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS+"/#",PET_ROW_CODE);
    }


    private PetDbHelper petDbHelper;

    public static final String LOG_TAG = PetProvider.class.getSimpleName();

    @Override
    public boolean onCreate() {

        petDbHelper = new PetDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = petDbHelper.getReadableDatabase();

        int code = PetUriMatcher.match(uri);

        Cursor cursor;

        switch (code){
            case PETS_CODE:
                cursor = db.query(PetContract.PetEntry.TABLE_NAME,projection,null,null,null,null,null,null);
                break;
            case PET_ROW_CODE:
                String Selection = PetContract.PetEntry._ID + "==?";
                String[] SelectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(PetContract.PetEntry.TABLE_NAME,projection,Selection,SelectionArgs,null,null,null);
                break;
            default:
                throw new IllegalStateException("Unexpected value of URI: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
     return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int code = PetUriMatcher.match(uri);
        SQLiteDatabase db = petDbHelper.getWritableDatabase();
        long newRowId;
        switch (code){
            case PETS_CODE:
                String name = values.getAsString(PetContract.PetEntry.PET_NAME);
                int gender = values.getAsInteger(PetContract.PetEntry.PET_GENDER);
                String weight = values.getAsString(PetContract.PetEntry.PET_WEIGHT);
                if (name == null ){
                    throw new IllegalArgumentException("Pet requires a name");
                }
  //              if (gender != PetContract.PetEntry.GENDER_MALE || gender != PetContract.PetEntry.GENDER_FEMALE || gender != PetContract.PetEntry.GENDER_UNKNOWN)
    //                gender=PetContract.PetEntry.GENDER_UNKNOWN;

                values.put(PetContract.PetEntry.PET_GENDER,gender);
                if (weight == null || Integer.valueOf(weight) < 0){
                    throw new IllegalArgumentException("Pet requires a valid weight");
                }
                newRowId = db.insert(PetContract.PetEntry.TABLE_NAME,null,values);

                getContext().getContentResolver().notifyChange(uri,null);

                if (newRowId == -1){
                    Log.e(LOG_TAG,"Failed to insert new row for " + uri);
                }
                return ContentUris.withAppendedId(uri,newRowId);
            default:
                throw new IllegalStateException("Unexpected value of URI: " + uri);
        }
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int code = PetUriMatcher.match(uri);
        SQLiteDatabase db = petDbHelper.getWritableDatabase();
        int rows;

        if (values.getAsString((PetContract.PetEntry.PET_NAME))==null){
            throw new IllegalArgumentException("Pet requires a name");
        }

        if (values.getAsInteger((PetContract.PetEntry.PET_WEIGHT))==null){
            throw new IllegalArgumentException("Pet requires a weight");
        }

        switch (code){
            case PETS_CODE:
                rows = db.update(PetContract.PetEntry.TABLE_NAME,values,null,null);
                break;
            case PET_ROW_CODE:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rows = db.update(PetContract.PetEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("update is not supported for " + uri);
        }
        if (rows!=0)
            getContext().getContentResolver().notifyChange(uri,null);
        return rows;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = petDbHelper.getWritableDatabase();
        int code = PetUriMatcher.match(uri);
        int rowsDeleted = 0;
        switch (code){
            case PETS_CODE:
                rowsDeleted = db.delete(PetContract.PetEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case PET_ROW_CODE:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(PetContract.PetEntry.TABLE_NAME,selection,selectionArgs);
        }
        if (rowsDeleted!=0)
            getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int code = PetUriMatcher.match(uri);
        switch (code){
            case PETS_CODE:
                return PetContract.PetEntry.CONTENT_LIST_TYPE;
            case PET_ROW_CODE:
                return PetContract.PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown Uri " + uri + "with code " + code);
        }
    }
}
