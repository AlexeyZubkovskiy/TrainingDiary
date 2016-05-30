package com.gmail.zubkovskiy.trainingdiary.events;

/**
 * Created by alexey.zubkovskiy@gmail.com on 21.05.2016.
 */
public class ClearOrderValues {
    boolean isClear;

    public ClearOrderValues(boolean isClear) {
        this.isClear = isClear;
    }

    public boolean isClear() {
        return isClear;
    }
}
