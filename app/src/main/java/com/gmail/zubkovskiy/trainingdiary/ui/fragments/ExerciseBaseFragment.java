package com.gmail.zubkovskiy.trainingdiary.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.adapters.RecyclerExerciseBaseAdapter;
import com.gmail.zubkovskiy.trainingdiary.listeners.ToolBarListener;
import com.gmail.zubkovskiy.trainingdiary.model.Exercise;
import com.gmail.zubkovskiy.trainingdiary.ui.activities.ExerciseBaseActivity;
import com.gmail.zubkovskiy.trainingdiary.ui.activities.StartActivity;
import com.gmail.zubkovskiy.trainingdiary.utils.Logf;

import java.util.List;

/**
 * Created by alexey.zubkovskiy@gmail.com on 08.05.2016.
 */
public class ExerciseBaseFragment extends BaseFragment {

    private static final String TAG = ExerciseBaseFragment.class.getSimpleName();

    private Button mAddNewButton;
    private Button mBackButton;
    private RecyclerView mRVBaseContainer;
    private ToolBarListener mToolbarListener;

    private List<Exercise> mExerciseBase;


    public static ExerciseBaseFragment getInstance(FragmentManager fragmentManager) {

        ExerciseBaseFragment fragment = (ExerciseBaseFragment) fragmentManager.findFragmentByTag(TAG);

        if (fragment == null) {
            fragment = new ExerciseBaseFragment();
        }

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recycler_exercise_base, container, false);
        mAddNewButton = (Button) view.findViewById(R.id.fragment_recycler_exercise_add_new_button);
        mBackButton = (Button) view.findViewById(R.id.fragment_recycler_exercise_back_button);
        mRVBaseContainer = (RecyclerView) view.findViewById(R.id.fragment_recycler_exercise_base_rv);


        //Bundle bundle = getArguments()  - update arguments from fragment.setArguments(Bundle bundle) from constructor

        mAddNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof ExerciseBaseActivity)                                       //getFragmentManager()
                    ((ExerciseBaseActivity) getActivity()).showFragment(NewExerciseFragment.getInstance(getChildFragmentManager()), true, false);
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), StartActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                getActivity().finish();
            }
        });
        mRVBaseContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRVBaseContainer.setAdapter(new RecyclerExerciseBaseAdapter(mExerciseBase, getActivity()));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        if (mExerciseBase == null) {
            if (context instanceof ExerciseBaseActivity)
                mExerciseBase = ((ExerciseBaseActivity) context).getExerciseBase();
        }

        Logf.w("ExerciseBaseFragment onAttach()", mExerciseBase.toString());

        if (context instanceof ToolBarListener)
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
        if (mToolbarListener != null)
            mToolbarListener.changeTitle(getString(R.string.exercise_base));
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

}
