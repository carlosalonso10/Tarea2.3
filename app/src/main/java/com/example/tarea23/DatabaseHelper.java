package com.example.tarea23;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "photographs.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "photographs";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_DESCRIPTION = "description";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_IMAGE + " BLOB," +
                    COLUMN_DESCRIPTION + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Método para insertar una fotografía en la base de datos
    public long insertPhotograph(byte[] image, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_DESCRIPTION, description);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    // Método para obtener todas las fotografías almacenadas en la base de datos
    public List<Photograph> getAllPhotographs() {
        List<Photograph> photographs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Photograph photograph = new Photograph();
                photograph.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                photograph.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE)));
                photograph.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                photographs.add(photograph);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return photographs;
    }
}
