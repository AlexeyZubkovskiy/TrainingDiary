package com.gmail.zubkovskiy.trainingdiary.global_context;


import android.app.Application;

public class TrainingDiary extends Application{

    private static TrainingDiary mTrainigDiary;

    public static TrainingDiary getInstance(){
        return mTrainigDiary;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mTrainigDiary = this;
    }
}
