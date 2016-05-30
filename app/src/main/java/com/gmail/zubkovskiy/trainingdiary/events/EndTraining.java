package com.gmail.zubkovskiy.trainingdiary.events;

/**
 * Created by alexey.zubkovskiy@gmail.com on 28.05.2016.
 */
public class EndTraining {

    private boolean isEnd;

    public EndTraining(boolean isEnd){
        this.isEnd = isEnd;
    }

    public boolean isEnd(){
        return this.isEnd;
    }
}
