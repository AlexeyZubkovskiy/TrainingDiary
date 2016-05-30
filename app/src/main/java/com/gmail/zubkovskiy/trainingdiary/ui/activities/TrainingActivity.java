package com.gmail.zubkovskiy.trainingdiary.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.events.ClearNewTrainingExercises;
import com.gmail.zubkovskiy.trainingdiary.events.EndTraining;
import com.gmail.zubkovskiy.trainingdiary.events.ExerciseForTrainingSelected;
import com.gmail.zubkovskiy.trainingdiary.helpers.BusProvider;
import com.gmail.zubkovskiy.trainingdiary.model.Training;
import com.gmail.zubkovskiy.trainingdiary.utils.DataManager;
import com.gmail.zubkovskiy.trainingdiary.listeners.ToolBarListener;
import com.gmail.zubkovskiy.trainingdiary.model.Exercise;
import com.gmail.zubkovskiy.trainingdiary.ui.fragments.BaseFragment;
import com.gmail.zubkovskiy.trainingdiary.ui.fragments.NewTrainingFragment;
import com.gmail.zubkovskiy.trainingdiary.utils.Logf;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alexey.zubkovskiy@gmail.com on 14.05.2016.
 */
public class TrainingActivity extends BaseActivity implements ToolBarListener {

    private static final String TAG = TrainingActivity.class.getSimpleName();
    private List<Exercise> mExerciseBase;
    private List<Exercise> mNewTrainingExercises;
    private String mNewTrainingTitle;
    private TextView mToolbarTitle;
   // private FrameLayout mContainer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        mExerciseBase = DataManager.getCategorySortedExerciseBase();

        mNewTrainingExercises = new ArrayList<>();
      //  mContainer = (FrameLayout) findViewById(R.id.activity_training_container);
        initToolbar();

        showFragment(NewTrainingFragment.getInstance(getSupportFragmentManager()), true, false);


    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);

    }

    @Override
    public void onBackPressed() {
        createExitDialog();
    }

    @Override
    public void changeTitle(CharSequence title) {
        mToolbarTitle.setText(title);
    }

    @Override
    public void switchFragment(BaseFragment fragment, boolean addToBackStack, boolean clearBackStack) {
        Logf.w(TAG, "switchFragment(BaseFragment fragment, boolean addToBackStack, boolean clearBackStack)");
        showFragment(fragment, addToBackStack, clearBackStack);

    }

    @Subscribe
    public void onTrainingEnd(EndTraining endTraining) {
        if (endTraining.isEnd()) {
            //to start saving in another thread
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    //TODO WRITE LOGIC TO SAVE TRAINING IN FIlE
                    Training training = new Training(mNewTrainingTitle);
                    training.setExercises(new ArrayList<Exercise>(mNewTrainingExercises));
                    training.setDate(Training.trainingDateFormat.format(new Date()));
                    try {
                        DataManager.saveTraining(training);
                    } catch (IOException e) {
                        //TODO WRITE EXCEPTION RESOLVE
                        Logf.e(TAG, "onTrainingEnd() IOexception" + e.getMessage());
                    }
                }
            });
            t.start();
            //   Toolbar toolbar = (Toolbar) findViewById(R.id.activity_training_toolbar);
            //   toolbar.addView(findViewById(R.id.activity_login_pb));
            toStartActivity();

        }
    }

    @Subscribe
    public void onReceiveExerciseForTrainingSelected(ExerciseForTrainingSelected exerciseForTrainingSelected) {

        if (exerciseForTrainingSelected.isSelectOrRemove()) {
            mNewTrainingExercises.add(exerciseForTrainingSelected.getExercise());
        } else
            mNewTrainingExercises.remove(exerciseForTrainingSelected.getExercise());


    }

    @Subscribe
    public void onReceiveClearNewTrainingExercises(ClearNewTrainingExercises clearNewTrainingExercises) {
        if (clearNewTrainingExercises.isClear())
            mNewTrainingExercises.clear();
    }

    public void showFragment(BaseFragment fragment, boolean addToBackStack, boolean clearBackStack) {

        Logf.e(TAG, "showFragment(BaseFragment fragment, boolean addToBackStack, boolean clearBackStack) START");
        if (clearBackStack) {
            while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStackImmediate();
                Logf.e(TAG, "showFragment(BaseFragment fragment, boolean addToBackStack, boolean clearBackStack) CLEAR_BACK_STACK");
            }
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.activity_training_container, fragment, fragment.getFragmentTag());
        transaction.commit();
        Logf.e(TAG, "showFragment(BaseFragment fragment, boolean addToBackStack, boolean clearBackStack) END");

    }

    public List<Exercise> getExerciseBase() {
        return mExerciseBase;
    }

    public List<Exercise> getNewTrainingExercises() {
        Logf.e(TAG, "getNewTrainingExercises() size : " + mNewTrainingExercises.size());
        return mNewTrainingExercises;
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_training_toolbar);
        mToolbarTitle = (TextView) findViewById(R.id.activity_training_toolbar_title_tv);

        //  toolbar.setNavigationIcon(R.drawable.ic_dehaze_toolbar_24dp);
        //toolbar.setM

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }

    }

    public void setNewTrainingTitle(String title) {
        mNewTrainingTitle = title;
    }

    public void createExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.exit))
                .setMessage(R.string.do_you_want_exit_from_training)
                .setCancelable(false)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toStartActivity();
                    }
                });
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    //invoke StartActivity
    private void toStartActivity(){
        startActivity(new Intent(TrainingActivity.this, StartActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        );
        finish();
    }
}



