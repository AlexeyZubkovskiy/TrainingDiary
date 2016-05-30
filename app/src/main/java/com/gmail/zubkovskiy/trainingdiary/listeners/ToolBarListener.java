package com.gmail.zubkovskiy.trainingdiary.listeners;

import com.gmail.zubkovskiy.trainingdiary.ui.fragments.BaseFragment;

/**
 * Created by alexey.zubkovskiy@gmail.com on 09.05.2016.
 */
public interface ToolBarListener {


    void changeTitle(CharSequence title);

    void switchFragment(BaseFragment fragment, boolean addToBackStack, boolean clearBackStack);


}
