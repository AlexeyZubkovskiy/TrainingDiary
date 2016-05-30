package com.gmail.zubkovskiy.trainingdiary.helpers;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by alexey.zubkovskiy@gmail.com on 26.04.2016.
 */
public class BusProvider {
    //To singleton
    private BusProvider(){}

    public static final Bus BUS = new Bus(ThreadEnforcer.ANY);

    public static Bus getInstance(){
        return BUS;
    }
}
