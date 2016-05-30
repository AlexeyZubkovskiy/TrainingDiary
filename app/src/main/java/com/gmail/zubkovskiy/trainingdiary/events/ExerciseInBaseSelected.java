package com.gmail.zubkovskiy.trainingdiary.events;

import com.gmail.zubkovskiy.trainingdiary.model.Exercise;

/**
 * Created by alexey.zubkovskiy@gmail.com on 09.05.2016.
 */
public class ExerciseInBaseSelected {

    private Exercise mExercise;

    public ExerciseInBaseSelected(Exercise exercise) {
        this.mExercise = exercise;
    }
    public Exercise getExercise(){
        if(mExercise!=null)
            return mExercise;
        else return null;
    }
}
