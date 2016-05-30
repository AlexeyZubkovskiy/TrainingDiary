package com.gmail.zubkovskiy.trainingdiary.events;

/**
 * Created by alexey.zubkovskiy@gmail.com on 21.05.2016.
 */
public class ClearNewTrainingExercises {
    boolean isClear;

    public ClearNewTrainingExercises(boolean isClear) {
        this.isClear = isClear;
    }

    public boolean isClear() {
        return isClear;
    }


}
