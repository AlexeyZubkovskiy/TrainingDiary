package com.gmail.zubkovskiy.trainingdiary.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.adapters.RecyclerTrainingAdapter;
import com.gmail.zubkovskiy.trainingdiary.events.EndTraining;
import com.gmail.zubkovskiy.trainingdiary.helpers.BusProvider;
import com.gmail.zubkovskiy.trainingdiary.listeners.ToolBarListener;
import com.gmail.zubkovskiy.trainingdiary.model.Exercise;
import com.gmail.zubkovskiy.trainingdiary.ui.activities.StartActivity;
import com.gmail.zubkovskiy.trainingdiary.ui.activities.TrainingActivity;
import com.gmail.zubkovskiy.trainingdiary.utils.Logf;

/**
 * Created by alexey.zubkovskiy@gmail.com on 22.05.2016.
 */
public class TrainingFragment extends BaseFragment {

    private static final String TAG = TrainingFragment.class.getSimpleName();

    private Exercise mCurrentExercise;
    private ToolBarListener mToolbarListener;
    private RecyclerView mContainer;
    private Button mInfoButton;
    private Button mOkButton;
    private Button mNextButton;
    private Button mPreviousButton;
    //  private Button mBackButton;
    private TextView mCurrentExerciseTitle;
    private int currentExerciseIndex;
    private int currentExercisesSize;


    public static TrainingFragment getInstance(FragmentManager fragmentManager, Exercise exercise) {

        TrainingFragment fragment = new TrainingFragment();

        fragment.setCurrentExercise(exercise);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training, container, false);
        mToolbarListener.changeTitle(getString(R.string.training));
        mContainer = (RecyclerView) view.findViewById(R.id.fragment_training_rv);
        mCurrentExerciseTitle = (TextView) view.findViewById(R.id.fragment_training_exercise_title_tv);
        mOkButton = (Button) view.findViewById(R.id.fragment_training_ok_button);
        mInfoButton = (Button) view.findViewById(R.id.fragment_training_exercise_info_button);
        mPreviousButton = (Button) view.findViewById(R.id.fragment_training_previous_button);
        mNextButton = (Button) view.findViewById(R.id.fragment_training_next_button);
        //  mBackButton = (Button) view.findViewById(R.id.fragment_training_back_button);

        mToolbarListener.changeTitle(getString(R.string.training));
        mCurrentExerciseTitle.setText(mCurrentExercise.getTitle());

        if (getActivity() instanceof TrainingActivity) {
            currentExerciseIndex = ((TrainingActivity) getActivity()).getNewTrainingExercises().indexOf(mCurrentExercise);
            currentExercisesSize = ((TrainingActivity) getActivity()).getNewTrainingExercises().size();
        }


        {//this two  conditions to make interface more smart
            if (currentExerciseIndex == 0)
                mPreviousButton.setVisibility(View.INVISIBLE);
            else
                mPreviousButton.setVisibility(View.VISIBLE);

            if (currentExerciseIndex == currentExercisesSize - 1)
                mNextButton.setVisibility(View.INVISIBLE);
            else
                mNextButton.setVisibility(View.VISIBLE);
        }

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof TrainingActivity) {
                    mToolbarListener.switchFragment(TrainingFragment.getInstance(getFragmentManager(),
                            ((TrainingActivity) getActivity()).getNewTrainingExercises().get(currentExerciseIndex + 1)),
                            true, false);
                    Logf.w("TF.mNextButton:", "onClickListener() Works");
                }

            }
        });
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof TrainingActivity) {
                    mToolbarListener.switchFragment(TrainingFragment.getInstance(getFragmentManager(),
                            ((TrainingActivity) getActivity()).getNewTrainingExercises().get(currentExerciseIndex - 1)),
                            true, false);
                    Logf.w("TF.mPreviousButton:", "onClickListener() Works");
                }


            }
        });

        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolbarListener.switchFragment(ExerciseDescriptionFragment.getInstance(getFragmentManager(), mCurrentExercise),
                        true, false);
            }
        });

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logf.w(TAG,"mOkButton onClick()");
                createEndTrainingDialog();
            }
        });

        mContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        mContainer.setAdapter(RecyclerTrainingAdapter.getInstance(mCurrentExercise.getApproaches(), getActivity()));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        BusProvider.getInstance().register(this);
        if (getActivity() instanceof TrainingActivity) {
            mToolbarListener = (ToolBarListener) getActivity();
        }
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        BusProvider.getInstance().unregister(this);
        mToolbarListener = null;
        super.onDetach();
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    private void createEndTrainingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Logf.w(TAG,"createEndTrainingDialog() START");
        builder.setTitle(getString(R.string.end_training))
                .setMessage(R.string.do_you_want_end_training)
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
                      /*  startActivity(new Intent(TrainingActivity.this, StartActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        );*/
                        // finish();
                        BusProvider.getInstance().post(new EndTraining(true));
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        Logf.w(TAG,"createEndTrainingDialog() END");
    }

    private void setCurrentExercise(Exercise exercise) {
        this.mCurrentExercise = exercise;
    }
}
