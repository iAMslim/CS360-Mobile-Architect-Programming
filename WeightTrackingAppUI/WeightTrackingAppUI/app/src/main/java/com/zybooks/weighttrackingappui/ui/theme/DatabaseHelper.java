package com.zybooks.weighttrackingappui.ui.theme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "user_id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    private static final String TABLE_WEIGHT = "weight_data";
    private static final String COLUMN_ENTRY_ID = "entry_id";
    private static final String COLUMN_USER_ID_FK = "user_id";
    private static final String COLUMN_DATE_LOGGED = "date_logged";
    private static final String COLUMN_WEIGHT = "weight";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT)";

        String CREATE_WEIGHT_TABLE = "CREATE TABLE " + TABLE_WEIGHT + " ("
                + COLUMN_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID_FK + " INTEGER, "
                + COLUMN_DATE_LOGGED + " TEXT, "
                + COLUMN_WEIGHT + " REAL, "
                + "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_WEIGHT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean registerUser(String email, String password) {
        if (userExists(email)) return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private boolean userExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_EMAIL + "=?", new String[]{email},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean insertWeight(int userId, String date, float weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID_FK, userId);
        values.put(COLUMN_DATE_LOGGED, date);
        values.put(COLUMN_WEIGHT, weight);
        long result = db.insert(TABLE_WEIGHT, null, values);
        return result != -1;
    }

    public Cursor getAllWeights(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_WEIGHT, null, COLUMN_USER_ID_FK + "=?", new String[]{String.valueOf(userId)},
                null, null, COLUMN_DATE_LOGGED + " DESC");
    }

    public boolean updateWeight(int entryId, String newDate, float newWeight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE_LOGGED, newDate);
        values.put(COLUMN_WEIGHT, newWeight);
        int result = db.update(TABLE_WEIGHT, values, COLUMN_ENTRY_ID + "=?", new String[]{String.valueOf(entryId)});
        return result > 0;
    }

    public boolean deleteWeight(int entryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_WEIGHT, COLUMN_ENTRY_ID + "=?", new String[]{String.valueOf(entryId)});
        return result > 0;
    }
}

