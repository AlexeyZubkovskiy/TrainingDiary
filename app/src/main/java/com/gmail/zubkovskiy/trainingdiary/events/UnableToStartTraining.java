package com.gmail.zubkovskiy.trainingdiary.events;

/**
 * Created by alexey.zubkovskiy@gmail.com on 22.05.2016.
 */
public class UnableToStartTraining {

    boolean isUnable;

    public UnableToStartTraining(boolean isUnable) {
        this.isUnable = isUnable;
    }

    public boolean isUnable(){
        return isUnable;
    }
}
