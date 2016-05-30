package com.gmail.zubkovskiy.trainingdiary.events;

import com.gmail.zubkovskiy.trainingdiary.model.Exercise;

/**
 * Created by alexey.zubkovskiy@gmail.com on 14.05.2016.
 */
public class ExerciseForTrainingSelected {

    private Exercise exercise;
    private boolean selectOrRemove;

    public ExerciseForTrainingSelected(Exercise exercise, boolean selectOrRemove){
        this.exercise = exercise;
        this.selectOrRemove = selectOrRemove;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public boolean isSelectOrRemove() {
        return selectOrRemove;
    }
}
