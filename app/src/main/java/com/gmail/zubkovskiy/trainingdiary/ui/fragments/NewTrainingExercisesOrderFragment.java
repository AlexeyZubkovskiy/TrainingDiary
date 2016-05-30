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
import android.widget.Toast;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.adapters.RecyclerNewTrainingExerciseOrderAdapter;
import com.gmail.zubkovskiy.trainingdiary.events.ClearNewTrainingExercises;
import com.gmail.zubkovskiy.trainingdiary.events.ClearOrderValues;
import com.gmail.zubkovskiy.trainingdiary.events.StartTraining;
import com.gmail.zubkovskiy.trainingdiary.events.UnableToStartTraining;
import com.gmail.zubkovskiy.trainingdiary.helpers.BusProvider;
import com.gmail.zubkovskiy.trainingdiary.listeners.ToolBarListener;
import com.gmail.zubkovskiy.trainingdiary.model.Exercise;
import com.gmail.zubkovskiy.trainingdiary.ui.activities.TrainingActivity;
import com.squareup.otto.Subscribe;

import java.util.Collections;
import java.util.List;

/**
 * Created by alexey.zubkovskiy@gmail.com on 21.05.2016.
 */
public class NewTrainingExercisesOrderFragment extends BaseFragment {

    private static final String TAG = NewTrainingExercisesOrderFragment.class.getSimpleName();

    private List<Exercise> mNewTrainingExercises;
    private RecyclerView mRecyclerContainer;
    private Button mStartTrainingButton;
    private Button mBackButton;
    private Button mClearButton;
    private ToolBarListener mToolbarListener;

    public static NewTrainingExercisesOrderFragment getInstance(FragmentManager fragmentManager) {

        NewTrainingExercisesOrderFragment fragment = (NewTrainingExercisesOrderFragment) fragmentManager.findFragmentByTag(TAG);

        if (fragment == null) {
            fragment = new NewTrainingExercisesOrderFragment();
        }

        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_training_exercises_order, container, false);
        mBackButton = (Button) view.findViewById(R.id.fragment_new_training_exercises_order_back_button);
        mStartTrainingButton = (Button) view.findViewById(R.id.fragment_new_training_exercises_order_start_training_button);
        mRecyclerContainer = (RecyclerView) view.findViewById(R.id.fragment_new_training_exercises_order_rv);
        mClearButton = (Button) view.findViewById(R.id.fragment_new_training_exercises_order_clear_choises_button);
        mToolbarListener.changeTitle(getString(R.string.activity_start_new_training));


        if (getActivity() instanceof TrainingActivity)
            mNewTrainingExercises = ((TrainingActivity) getActivity()).getNewTrainingExercises();

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolbarListener.switchFragment(NewTrainingFragment.getInstance(getFragmentManager()), true, true);
                BusProvider.getInstance().post(new ClearNewTrainingExercises(true));
            }
        });
        mStartTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new StartTraining(true));
            }
        });

        mRecyclerContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerContainer.setAdapter(RecyclerNewTrainingExerciseOrderAdapter.getInstance(mNewTrainingExercises, getActivity()));

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   mRecyclerContainer.removeAllViews();
                BusProvider.getInstance().post(new ClearOrderValues(true));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mNewTrainingExercises = null;
    }

    @Subscribe
    public void onReceivedUnableToStartTraining(UnableToStartTraining unableToStartTraining) {
        if (unableToStartTraining.isUnable()) {
            Toast.makeText(getActivity(), getString(R.string.fill_all_fields_of_order_exercises), Toast.LENGTH_LONG).show();
        } else {
            //This for safety
            if (getActivity() instanceof TrainingActivity) {
                Collections.sort(((TrainingActivity) getActivity()).getNewTrainingExercises(), Exercise.orderComparator);
                mToolbarListener.switchFragment(
                        TrainingFragment.getInstance(getFragmentManager(),
                                ((TrainingActivity) getActivity()).getNewTrainingExercises().get(0)),
                        false,true);
            }
        }
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}
