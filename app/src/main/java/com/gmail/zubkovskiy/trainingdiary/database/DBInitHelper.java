package com.gmail.zubkovskiy.trainingdiary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.gmail.zubkovskiy.trainingdiary.model.Exercise;
import com.gmail.zubkovskiy.trainingdiary.utils.Logf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexey.zubkovskiy@gmail.com on 08.05.2016.
 */
public class DBInitHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "training_diary.db";
    private static final String TABLE_EXERCISES = "exercises";
    private static final int VERSION = 1;
    private static final String TABLE_EXERCISES_ID_COLUMN = "_id";
    private static final String TABLE_EXERCISES_TITLE_COLUMN = "title";
    private static final String TABLE_EXERCISES_DESCRIPTION_COLUMN = "description";
    private static final String TABLE_EXERCISES_CATEGORY_COLUMN = "category";

    private SQLiteDatabase mDataBase;
    private String DBPath = null;
    private final Context mContext;

    public DBInitHelper(Context context) {
        super(context, DB_NAME, null, VERSION);

        this.mContext = context;
        this.DBPath = "/data/data/" + context.getPackageName() + "/databases/";
        Logf.e("Data Base PATH:", DBPath);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            createDataBase();
        } catch (IOException e) {
            Logf.e("Except DBInitHelper.onCreate()", e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            try {
                copyDataBase();
            } catch (IOException e) {
                Logf.e("Exception by copyDataBase()", e.getMessage());
            }
        }


    }

    public void createDataBase() throws IOException {


        if (isDbExist()) {
            Logf.e("DBInitHelper.createDataBase()", "Data base is already exist.");
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database.");
            }

        }

    }

    public boolean isDbExist() {

        SQLiteDatabase checkDB = null;
        try {
            String dbPath = DBPath + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Logf.e("Exception in DBInitHelper.checkDb()", e.getMessage());
        }
        if (checkDB != null)
            checkDB.close();

        return checkDB != null ;


    }

    public  void copyDataBase() throws IOException {

        try {
            InputStream inputStream = mContext.getAssets().open(DB_NAME);

            String internalDBFile = DBPath + DB_NAME;

            OutputStream outputStream = new FileOutputStream(internalDBFile);

            byte buffer[] = new byte[32];
            int size;
            while ((size = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, size);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Logf.i("Copy DataBase() IOException", e.getMessage());
        }

    }

    public synchronized void openDataBase() throws SQLException {

        String dbPath = DBPath + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    public synchronized void openDataBaseToWrite() throws SQLException{
        mDataBase = SQLiteDatabase.openDatabase(DBPath + DB_NAME,null ,SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void saveCustomExercise(String title, String description, String category){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLE_EXERCISES_TITLE_COLUMN,title);
        values.put(TABLE_EXERCISES_DESCRIPTION_COLUMN,description);
        values.put(TABLE_EXERCISES_CATEGORY_COLUMN,category);
        db.insert(TABLE_EXERCISES,null,values);
        db.close();

    }

    public List<Exercise> getExerciseBase() {

        List<Exercise> base = new ArrayList<Exercise>();
        openDataBase();
        Cursor cursor = mDataBase.rawQuery("SELECT * FROM " + TABLE_EXERCISES, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            base.add(new Exercise(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            cursor.moveToNext();
        }
        cursor.close();
        close();
        if (base.size() != 0)
            return base;
        else return null;


    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }


}
