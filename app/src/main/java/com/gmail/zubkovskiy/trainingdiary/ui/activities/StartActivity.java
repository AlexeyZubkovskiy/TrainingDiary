package com.gmail.zubkovskiy.trainingdiary.ui.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.database.DBInitHelper;
import com.gmail.zubkovskiy.trainingdiary.global_context.TrainingDiary;
import com.gmail.zubkovskiy.trainingdiary.ui.fragments.RegistrationDialogFragment;
import com.gmail.zubkovskiy.trainingdiary.utils.DataManager;
import com.gmail.zubkovskiy.trainingdiary.utils.Logf;
import com.gmail.zubkovskiy.trainingdiary.utils.SharedPreferenceUtil;

import java.io.IOException;


public class StartActivity extends FragmentActivity {

    private LinearLayout mNewTraining;
    private LinearLayout mDiary;
    private LinearLayout mExerciseBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mNewTraining = (LinearLayout) findViewById(R.id.new_trainig_layout);
        mDiary = (LinearLayout) findViewById(R.id.diary_layout);
        mExerciseBase = (LinearLayout) findViewById(R.id.exercise_base_layout);

        if (!SharedPreferenceUtil.isLogin())
            showRegisterDialog();
        //To prepare all application data to working
        DataManager.initialize();

        mExerciseBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, ExerciseBaseActivity.class));
            }
        });
        mNewTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, TrainingActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        createExitDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void showRegisterDialog() {

        FragmentManager fm = getSupportFragmentManager();
        new RegistrationDialogFragment().show(fm, "rdf");

    }

    public void createExitDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.exit))
                .setMessage(R.string.do_you_want_exit_from_app)
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
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        android.support.v7.app.AlertDialog dialog = builder.create();

        dialog.show();
    }
}
