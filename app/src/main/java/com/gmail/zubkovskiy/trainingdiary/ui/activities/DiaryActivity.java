package com.gmail.zubkovskiy.trainingdiary.ui.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.model.Training;
import com.gmail.zubkovskiy.trainingdiary.ui.fragments.BaseFragment;
import com.gmail.zubkovskiy.trainingdiary.utils.DataManager;
import com.gmail.zubkovskiy.trainingdiary.utils.Logf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexey.zubkovskiy@gmail.com on 30.05.2016.
 */
public class DiaryActivity extends BaseActivity {

    private ArrayList<Training> mTrainingDiary;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_diary);

        initToolbar();
        mTrainingDiary = new ArrayList<>();
        mTrainingDiary.addAll(DataManager.getTrainingsDiary());


    }


    public ArrayList<Training> getTrainingDiary() {
        if (mTrainingDiary != null)
            return mTrainingDiary;
        else
            return null;

    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_diary_toolbar);
        mToolbarTitle = (TextView) findViewById(R.id.activity_diary_toolbar_title_tv);

        //  toolbar.setNavigationIcon(R.drawable.ic_dehaze_toolbar_24dp);
        //toolbar.setM

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }

    }

    public void showFragment(BaseFragment fragment, boolean addToBackStack, boolean clearBackStack) {


        if (clearBackStack) {
            while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.activity_training_container, fragment, fragment.getFragmentTag());
        transaction.commit();


    }
}
