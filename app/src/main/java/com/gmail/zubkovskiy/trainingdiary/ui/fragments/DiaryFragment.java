package com.gmail.zubkovskiy.trainingdiary.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by alexey.zubkovskiy@gmail.com on 30.05.2016.
 */
public class DiaryFragment extends BaseFragment {

    private static final String TAG = DiaryFragment.class.getSimpleName();

    public static DiaryFragment getInstance(FragmentManager fragmentManager) {

        DiaryFragment fragment = (DiaryFragment) fragmentManager.findFragmentByTag(TAG);

        if (fragment == null) {
            fragment = new DiaryFragment();
        }
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}
