package com.gmail.zubkovskiy.trainingdiary.ui.activities;
//UNUSED ACTIVITY!!!
//
//
//
//
//
//
//
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.helpers.BusProvider;
import com.gmail.zubkovskiy.trainingdiary.listeners.ToolBarListener;
import com.gmail.zubkovskiy.trainingdiary.model.Exercise;
import com.gmail.zubkovskiy.trainingdiary.ui.fragments.BaseFragment;
import com.gmail.zubkovskiy.trainingdiary.utils.Logf;

import java.util.List;

/**
 * Created by alexey.zubkovskiy@gmail.com on 26.05.2016.
 */
public class CurrentTrainingActivity extends BaseActivity implements ToolBarListener {

    private static final String TAG = TrainingActivity.class.getSimpleName();

    private List<Exercise> mNewTrainingExercises;

    private Button mInfoButton;
    private Button mOkButton;
    private Button mNextButton;
    private Button mPreviousButton;
    private Button mBackButton;
    private String mTrainingTitle;
    private TextView mToolbarTitle;


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
        Logf.w(TAG,"switchFragment(BaseFragment fragment, boolean addToBackStack, boolean clearBackStack)");
        showFragment(fragment, addToBackStack, clearBackStack);

    }


    public void showFragment(BaseFragment fragment, boolean addToBackStack, boolean clearBackStack) {

     //   Logf.e(TAG,"showFragment(BaseFragment fragment, boolean addToBackStack, boolean clearBackStack) START");
        if (clearBackStack) {
            while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStackImmediate();
               // Logf.e(TAG,"showFragment(BaseFragment fragment, boolean addToBackStack, boolean clearBackStack) CLEAR_BACK_STACK");
            }
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_training_container, fragment, fragment.getFragmentTag());
        transaction.commit();
      //  Logf.e(TAG,"showFragment(BaseFragment fragment, boolean addToBackStack, boolean clearBackStack) END");

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
                        startActivity(new Intent(CurrentTrainingActivity.this, StartActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        );
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();

        dialog.show();
    }


}
