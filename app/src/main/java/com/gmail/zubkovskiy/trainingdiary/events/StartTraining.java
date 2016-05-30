package com.gmail.zubkovskiy.trainingdiary.events;

/**
 * Created by alexey.zubkovskiy@gmail.com on 21.05.2016.
 */
public class StartTraining {

    private boolean isStart;

   public StartTraining(boolean isStart){
        this.isStart = isStart;
    }

   public boolean isStart(){
        return this.isStart;
    }
}
