package com.gmail.zubkovskiy.trainingdiary.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.utils.DataManager;
import com.gmail.zubkovskiy.trainingdiary.listeners.ToolBarListener;

/**
 * Created by alexey.zubkovskiy@gmail.com on 09.05.2016.
 */
public class NewExerciseFragment extends BaseFragment {

    private static final String TAG = NewExerciseFragment.class.getSimpleName();

    private ToolBarListener mToolBarListener;
    private Button mOkButton;
    private Button mClearButton;
    private Button mBackButton;
    private EditText mTitleExercise;
    private EditText mDescriptionExercise;
    private EditText mCategoryExercise;

    public static NewExerciseFragment getInstance(FragmentManager fragmentManager) {

        NewExerciseFragment fragment = (NewExerciseFragment) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null)
            fragment = new NewExerciseFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_exercise, container, false);
        mOkButton = (Button) view.findViewById(R.id.fragment_new_exercise_add_button);
        mClearButton = (Button) view.findViewById(R.id.fragment_new_exercise_clear_button);
        mBackButton = (Button) view.findViewById(R.id.fragment_new_exercise_back_button);
        mTitleExercise = (EditText) view.findViewById(R.id.fragment_new_exercise_title_et);
        mDescriptionExercise = (EditText) view.findViewById(R.id.fragment_new_exercise_description_et);
        mCategoryExercise = (EditText) view.findViewById(R.id.fragment_new_exercise_category_et);
        mToolBarListener.changeTitle(getString(R.string.add_new_exercise));
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTitleExercise != null && mDescriptionExercise != null) {
                    mTitleExercise.setText("");
                    mDescriptionExercise.setText("");
                    mCategoryExercise.setText("");
                }
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //or getFragmentManager().popBackStackImmediate();
                getFragmentManager().popBackStackImmediate();
            }
        });
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitleExercise.getText().toString();
                String description = mDescriptionExercise.getText().toString();
                String category = mCategoryExercise.getText().toString();
                if (title.length()>0 && description.length() > 0 && category.length() > 0) {
                    DataManager.saveCustomExercise(title, description, category);
                    getFragmentManager().popBackStackImmediate();
                }
                else
                    Toast.makeText(getActivity(),getString(R.string.fill_all_fields),Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        //to working adjust_pan
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof ToolBarListener)
            mToolBarListener = (ToolBarListener) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mToolBarListener = null;
        super.onDetach();
    }


    @Override
    public String getFragmentTag() {
        return TAG;
    }
}
