package com.example.petss;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.petss.data.*;

public class PetCursorAdapter extends CursorAdapter {


    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,null,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);

        String name = cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.PET_NAME));
        String breed = cursor.getString(cursor.getColumnIndex(PetContract.PetEntry.PET_BREED));
        int genderInt = cursor.getInt(cursor.getColumnIndex(PetContract.PetEntry.PET_GENDER));
        int weight = cursor.getInt(cursor.getColumnIndex(PetContract.PetEntry.PET_WEIGHT));

        String gender;
        switch (genderInt) {
            case PetContract.PetEntry.GENDER_MALE:
                gender = "Male";
                break;
            case PetContract.PetEntry.GENDER_FEMALE:
                gender = "Female";
                break;
            default:
                gender = "Unknown";
                break;
        }

        nameTextView.setText(name);
        summaryTextView.setText(breed + "\n" + gender + "\n" + weight + "kg");


    }

}
