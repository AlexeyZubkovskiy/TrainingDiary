package com.gmail.zubkovskiy.trainingdiary.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.listeners.ToolBarListener;
import com.gmail.zubkovskiy.trainingdiary.model.Exercise;

/**
 * Created by alexey.zubkovskiy@gmail.com on 10.05.2016.
 */
public class ExerciseDescriptionFragment extends BaseFragment {

    private TextView mTitle;
    private TextView mDescription;
    private Exercise mExercise;
    private Button mOkButton;
    private ToolBarListener mToolbarListener;

    private static final String TAG = ExerciseDescriptionFragment.class.getSimpleName();

    public static ExerciseDescriptionFragment getInstance(FragmentManager fragmentManager, Exercise exercise) {

        ExerciseDescriptionFragment edf = (ExerciseDescriptionFragment) fragmentManager.findFragmentByTag(TAG);

        if (edf == null)
            edf = new ExerciseDescriptionFragment();

        edf.setExercise(exercise);

        return edf;
    }

    public void setExercise(Exercise exercise) {
        mExercise = exercise;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_exercise_description, container, false);
        mTitle = (TextView) view.findViewById(R.id.fragment_exercise_description_title_tv);
        mDescription = (TextView) view.findViewById(R.id.fragment_exercise_description_description_tv);
        mOkButton = (Button) view.findViewById(R.id.fragment_exercise_description_button);

        mTitle.setText(mExercise.getTitle());
        mDescription.setText(mExercise.getDescription());
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
                
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        if (getActivity() instanceof ToolBarListener)
            mToolbarListener = (ToolBarListener) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mToolbarListener = null;
        super.onDetach();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mToolbarListener.changeTitle(getString(R.string.exercise_description));
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}
