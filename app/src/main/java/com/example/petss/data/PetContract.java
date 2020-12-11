package com.example.petss.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class PetContract {


    public static final String CONTENT_AUTHORITY = "com.example.petss";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PETS = "petss";


    public static final class PetEntry implements BaseColumns {

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_PETS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_PETS;


    public static final int URL_PET_LOADER = 1;

    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PETS);

    public static final String TABLE_NAME = "pets";

    public static final String PET_ID = BaseColumns._ID;

    public static final String PET_NAME = "name";

    public static final String PET_BREED = "breed";

    public static final String PET_GENDER = "gender";

    public static final String PET_WEIGHT = "weight";


    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;
    public static final int GENDER_UNKNOWN = 0;

}



}
