package com.gmail.zubkovskiy.trainingdiary.ui.fragments;


import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.ui.activities.LoginActivity;
import com.gmail.zubkovskiy.trainingdiary.ui.activities.RegistrationActivity;

public class RegistrationDialogFragment extends DialogFragment{

    private Button mRegisterButton;
    private Button mLoginButton;
    private Button mCancelButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_registration,container);
        getDialog().setCancelable(false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FIRST_SUB_WINDOW,WindowManager.LayoutParams.FIRST_SUB_WINDOW);
        mRegisterButton = (Button) view.findViewById(R.id.fragment_dialog_registration_b);
        mLoginButton = (Button) view.findViewById(R.id.fragment_dialog_login_b);
        mCancelButton = (Button) view.findViewById(R.id.fragment_dialog_cancel_b);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start LoginActivity
                startActivity(new Intent(getActivity(),LoginActivity.class));
                dismiss();
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start RegistrationActivity
                startActivity(new Intent(getActivity(),RegistrationActivity.class));
                dismiss();
            }
        });

        return view;



    }

    //This method starts when dialog is dismiss
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    //This method starts when pressed back[<-] button
    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
