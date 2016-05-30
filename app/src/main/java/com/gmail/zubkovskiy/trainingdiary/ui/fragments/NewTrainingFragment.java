package com.gmail.zubkovskiy.trainingdiary.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.adapters.RecyclerNewTrainingAdapter;
import com.gmail.zubkovskiy.trainingdiary.events.ClearNewTrainingExercises;
import com.gmail.zubkovskiy.trainingdiary.events.ExerciseForTrainingSelected;
import com.gmail.zubkovskiy.trainingdiary.helpers.BusProvider;
import com.gmail.zubkovskiy.trainingdiary.listeners.ToolBarListener;
import com.gmail.zubkovskiy.trainingdiary.model.Exercise;
import com.gmail.zubkovskiy.trainingdiary.ui.activities.TrainingActivity;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by alexey.zubkovskiy@gmail.com on 15.05.2016.
 */
public class NewTrainingFragment extends BaseFragment {

    private static final String TAG = NewTrainingFragment.class.getSimpleName();

    private List<Exercise> mExerciseBase;
    private RecyclerView mRecyclerContainer;
    private EditText mNewTrainingTitle;
    private Button mStartTrainingButton;
    private Button mBackButton;
    private Button mClearButton;
    private ToolBarListener mToolbarListener;
    //it is flag for start next fragment
    private boolean isReadyToNext;
    //this counter count added to newTrainingExercises base on activity
    private int exercisesCounter;

    public static NewTrainingFragment getInstance(FragmentManager fragmentManager) {

        NewTrainingFragment fragment = (NewTrainingFragment) fragmentManager.findFragmentByTag(TAG);

        if (fragment == null) {
            fragment = new NewTrainingFragment();
        }

        fragment.isReadyToNext = false;
        fragment.exercisesCounter = 0;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_training, container, false);
        mBackButton = (Button) view.findViewById(R.id.fragment_new_training_back_button);
        mNewTrainingTitle = (EditText) view.findViewById(R.id.fragment_new_training_training_tittle_et);
        mStartTrainingButton = (Button) view.findViewById(R.id.fragment_new_training_start_training_button);
        mRecyclerContainer = (RecyclerView) view.findViewById(R.id.fragment_new_training_rv);
        mClearButton = (Button) view.findViewById(R.id.fragment_new_training_clear_choises_button);
        mToolbarListener.changeTitle(getString(R.string.activity_start_new_training));


        if (getActivity() instanceof TrainingActivity)
            mExerciseBase = ((TrainingActivity) getActivity()).getExerciseBase();

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mStartTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mNewTrainingTitle.getText().toString();
                if (title.length() > 0)
                    ((TrainingActivity) getActivity()).setNewTrainingTitle(title);
                else {
                    Toast.makeText(getActivity(), getString(R.string.enter_training_title), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (exercisesCounter > 0)
                    isReadyToNext = true;
                else
                    isReadyToNext = false;

                if (isReadyToNext) {
                    mToolbarListener.switchFragment(NewTrainingExercisesOrderFragment.getInstance(getFragmentManager()), true, false);
                    if(getActivity() instanceof TrainingActivity){
                        ((TrainingActivity)getActivity()).setNewTrainingTitle(title);
                    }
                }else
                    Toast.makeText(getActivity(), getString(R.string.choose_exercises_for_training), Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerContainer.setAdapter(RecyclerNewTrainingAdapter.getInstance(mExerciseBase, getActivity()));

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);


        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerContainer.removeAllViews();
                mNewTrainingTitle.setText("");
                BusProvider.getInstance().post(new ClearNewTrainingExercises(true));
                isReadyToNext = false;
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        mToolbarListener = null;
        BusProvider.getInstance().unregister(this);
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof TrainingActivity)
            mToolbarListener = (TrainingActivity) context;
        BusProvider.getInstance().register(this);
        super.onAttach(context);
    }

    @Subscribe
    public void onReceiveExerciseSelected(ExerciseForTrainingSelected exerciseForTrainingSelected) {
        if (exerciseForTrainingSelected.isSelectOrRemove())
            exercisesCounter += 1;
        else
            exercisesCounter -= 1;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public void onDestroyView() {
        mExerciseBase = null;
        super.onDestroyView();
    }
}
