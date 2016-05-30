package com.gmail.zubkovskiy.trainingdiary.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gmail.zubkovskiy.trainingdiary.database.DBInitHelper;
import com.gmail.zubkovskiy.trainingdiary.global_context.TrainingDiary;
import com.gmail.zubkovskiy.trainingdiary.model.Exercise;
import com.gmail.zubkovskiy.trainingdiary.model.Training;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alexey.zubkovskiy@gmail.com on 09.05.2016.
 */
public class DataManager {

    private static final String JSON_FILE_EXTENTION = ".json";
    private static final String INTERNAL_STORAGE_DIRECTORY = TrainingDiary.getInstance().getFilesDir().getPath();

    private static List<Exercise> mExerciseBase;
    private static DBInitHelper mDbHelper;
    private static List<Training> mTrainingsBase;
    //To make  this class saved till application end
    private static Context mGlobalContext;


    //THis method initialize DataManager and readData
    public static synchronized void initialize() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    getTrainingsBase();
                } catch (IOException e) {
                   Logf.e("DataManager.initialize()","getTrainingsBase() Error" + e.getMessage());
                }
                try {
                    copyBaseFromAssets();
                } catch (IOException e) {
                    Logf.e("DataManager.initialize()","copyBaseFromAssets() Error" + e.getMessage());
                }
                getExerciseBase();
                mGlobalContext = TrainingDiary.getInstance();
            }
        });
        thread.start();

    }

    //returns sorted exercises, uses on activities
    public static List<Exercise> getTitleSortedExerciseBase() {

        getExerciseBase();
        Collections.sort(mExerciseBase, Exercise.titleComparator);
        return mExerciseBase;
    }

    public static List<Exercise> getCategorySortedExerciseBase() {
        getExerciseBase();
        Collections.sort(mExerciseBase, Exercise.categoryComparator);
        return mExerciseBase;
    }

    public static List<Training> getTrainingsDiary(){
        return mTrainingsBase;
    }

    public static void sortExercisesByOrder(List<Exercise> exercises) {
        Collections.sort(exercises, Exercise.orderComparator);
    }

    //save users exercise to database
    public static synchronized void saveCustomExercise(final String title, final String description, final String category) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mDbHelper.saveCustomExercise(title, description, category);
                mExerciseBase = null;
                getExerciseBase();
            }
        });
        thread.start();

    }

    //this method save training to json file in internal storage
    public static synchronized void saveTraining(final Training training) throws IOException {

        Gson gson = new Gson();
        String trainingJson = gson.toJson(training) + ",";


        Logf.e("INTERNAL STORAGE DIRECTORY", INTERNAL_STORAGE_DIRECTORY);

        File file = new File(INTERNAL_STORAGE_DIRECTORY + "/" + SharedPreferenceUtil.getLogin() + JSON_FILE_EXTENTION);

        if (!file.exists())
            file.createNewFile();

        rewriteFromDefaultFile(file);

        FileOutputStream fos = new FileOutputStream(file, true);
        fos.write(trainingJson.getBytes());
        fos.close();
        Logf.w("saveTraining()", file.getAbsolutePath());

    }

    //this method for writing data when user unregistered first time , and register later
    private static void rewriteFromDefaultFile(File file) throws IOException {

        File defaultFile = new File(INTERNAL_STORAGE_DIRECTORY + "/" + SharedPreferenceUtil.USER_DEFAULT_LOGIN + JSON_FILE_EXTENTION);

        if (defaultFile.exists()) {
            Logf.w("DataManager.saveTraining()", "on rewriting files start function");
            if (!file.getPath().equals(defaultFile.getPath())) {
                Logf.w("DataManager.saveTraining()", "on rewriting files");
                FileOutputStream fileOutputStream = new FileOutputStream(file, true);

                fileOutputStream.write(readFromFile(defaultFile).getBytes());

                fileOutputStream.close();

                defaultFile.delete();
            }
        }

    }

    @NonNull
    private static String readFromFile(File file) throws IOException{

        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
        Logf.w("DataManager.readFromFile()", "on read files" + file.getAbsolutePath() + "\n" +
                file.getAbsolutePath());

        StringBuffer sb = new StringBuffer();
        String temp = "";
        while ((temp = br.readLine()) != null) {
            sb.append(temp);
        }
        fileInputStream.close();

        return sb.toString();
    }

    //First time run copying dataBase from assets to internal storage
    private static void copyBaseFromAssets()throws IOException{

            //this code to copy data base from assets
           DBInitHelper dbInitHelper = getDbHelper();
            if (!dbInitHelper.isDbExist()) {
                dbInitHelper.createDataBase();
                dbInitHelper.copyDataBase();
            }

    }

    //Returns instance of DbHelper
    private static DBInitHelper getDbHelper() {
        if (mDbHelper == null) {
            mDbHelper = new DBInitHelper(TrainingDiary.getInstance());
        }
        return mDbHelper;
    }

    //Returns all unsorted exercises from database
    private static void getExerciseBase() {
      //  if (mExerciseBase == null)
            mExerciseBase = getDbHelper().getExerciseBase();
    }

    //Save to DataManager trainings history from json file
    private static void getTrainingsBase() throws IOException{

        Gson gson = new Gson();
        File file = new File(INTERNAL_STORAGE_DIRECTORY + "/" + SharedPreferenceUtil.getLogin() + JSON_FILE_EXTENTION);




       if(file.exists()) {
           StringBuilder json = new StringBuilder(readFromFile(file));
           //This operations with json string to make json array from
           json.insert(0, "[");
           json.replace(json.length()-1,json.length()-1,"]");
           //To delete ',' in end of json file
           json.deleteCharAt(json.length()-1);
           Logf.w("DataManager.getTrainingBase","json after replacing" + json.toString());
           JsonReader jr = new JsonReader(new StringReader(json.toString()));
           //Saving trainings from json to array of trainings
           Training[] trainingsBase = gson.fromJson(jr,Training[].class);

           mTrainingsBase = new ArrayList<Training>();
           for (int i = 0; i < trainingsBase.length ; i++) {
               mTrainingsBase.add(trainingsBase[i]);
           }
           //to have sorted collection
           Collections.sort(mTrainingsBase,Training.trainingDateComparator);
          /* for (int i = 0; i < trainingsBase.length ; i++) {
               Logf.w("DataManager.getTrainingBase()", "trainingsBase value = " + trainingsBase[i].toString());
           }*/

       }

    }

}
