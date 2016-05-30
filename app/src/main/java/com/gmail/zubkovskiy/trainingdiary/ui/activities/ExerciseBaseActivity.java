package com.gmail.zubkovskiy.trainingdiary.ui.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.events.ExerciseInBaseSelected;
import com.gmail.zubkovskiy.trainingdiary.helpers.BusProvider;
import com.gmail.zubkovskiy.trainingdiary.utils.DataManager;
import com.gmail.zubkovskiy.trainingdiary.listeners.ToolBarListener;
import com.gmail.zubkovskiy.trainingdiary.model.Exercise;
import com.gmail.zubkovskiy.trainingdiary.ui.fragments.BaseFragment;
import com.gmail.zubkovskiy.trainingdiary.ui.fragments.ExerciseBaseFragment;
import com.gmail.zubkovskiy.trainingdiary.ui.fragments.ExerciseDescriptionFragment;
import com.gmail.zubkovskiy.trainingdiary.utils.Logf;
import com.squareup.otto.Subscribe;

import java.util.List;

public class ExerciseBaseActivity extends BaseActivity implements ToolBarListener {
    private static final String TAG = ExerciseBaseActivity.class.getSimpleName();
    private List<Exercise> mExerciseBase;
    private FrameLayout mContainer;
    private TextView mToolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_base);
        mExerciseBase = DataManager.getTitleSortedExerciseBase();
        mContainer = (FrameLayout) findViewById(R.id.activity_exercise_base_container);
        initToolbar();
        Logf.w(TAG, "Before showFragment() in onCreate");
        showFragment(ExerciseBaseFragment.getInstance(getSupportFragmentManager()), false, false);
        Logf.w(TAG, "After showFragment() in onCreate");
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

    @Subscribe
    public void onExerciseDescriptionFragmentStart(ExerciseInBaseSelected exerciseInBaseSelected) {
        showFragment(ExerciseDescriptionFragment.getInstance(getSupportFragmentManager(),
                exerciseInBaseSelected.getExercise()), true, false);
    }

    @Override
    public void onBackPressed() {
        //Clear backstack
        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }
        startActivity(new Intent(this, StartActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
        finish();

    }

    @Override
    public void changeTitle(CharSequence title) {
        mToolbarTitle.setText(title);
    }

    @Override
    public void switchFragment(BaseFragment fragment, boolean addToBackStack, boolean clearBackStack) {
        showFragment(fragment, addToBackStack, clearBackStack);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_exercise_base_toolbar);
        mToolbarTitle = (TextView) findViewById(R.id.activity_exercise_base_toolbar_title_tv);
       // if(toolbar!=null)
       //     toolbar.setNavigationIcon(R.drawable.ic_dehaze_toolbar_24dp);

       // MenuBuilder builder = new MenuBuilder(this);
        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        //To delete standard action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }
    }

    public List<Exercise> getExerciseBase() {
        if (mExerciseBase == null)
            mExerciseBase = DataManager.getTitleSortedExerciseBase();
        return mExerciseBase;
    }

    public void showFragment(BaseFragment fragment, boolean addToBackstack, boolean clearBackStack) {
        Logf.w(TAG, "showFragment()");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        /*transaction.setCustomAnimations() - to set animation from transaction*/

        if (clearBackStack) {
            while (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                getSupportFragmentManager().popBackStackImmediate();

            }
        }


        if (addToBackstack) {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.activity_exercise_base_container, fragment, fragment.getFragmentTag());
        transaction.commit();
    }
}
