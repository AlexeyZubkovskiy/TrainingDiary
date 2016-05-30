package com.gmail.zubkovskiy.trainingdiary.ui.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.events.LoginRequest;
import com.gmail.zubkovskiy.trainingdiary.events.LoginResponse;
import com.gmail.zubkovskiy.trainingdiary.helpers.BusProvider;
import com.gmail.zubkovskiy.trainingdiary.network.NetworkConstants;
import com.gmail.zubkovskiy.trainingdiary.network.ServiceBroker;
import com.squareup.otto.Subscribe;

public class
LoginActivity extends Activity {

    private ServiceBroker mServiceBroker;
    private Button mLoginButton;
    private Button mRegisterButton;
    private EditText mLoginEmail;
    private EditText mPassword;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to create instance os ServiceBroker for using otto
        mServiceBroker = ServiceBroker.getInstance();

        setContentView(R.layout.activity_login);

        mLoginButton = (Button) findViewById(R.id.activity_login_login_b);
        mRegisterButton = (Button) findViewById(R.id.activity_login_register_b);
        mLoginEmail = (EditText) findViewById(R.id.email_login_et);
        mPassword = (EditText) findViewById(R.id.password_login_et);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_login_pb);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = String.valueOf(mLoginEmail.getText());
                String password = String.valueOf(mPassword.getText());
                if (!TextUtils.isEmpty(login) && !TextUtils.isEmpty(password)) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mRegisterButton.setClickable(false);
                    mLoginButton.setClickable(false);
                    BusProvider.getInstance().post(new LoginRequest(login, password));
                } else
                    Toast.makeText(LoginActivity.this, R.string.fill_all_fields, Toast.LENGTH_SHORT)
                            .show();
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //to register BusProvider by otto to react on events
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //to unregister BusProvider by otto to react on events
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, StartActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        );
        finish();
    }

    @Subscribe
    public void onLoginResponse(LoginResponse response) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRegisterButton.setClickable(true);
        mLoginButton.setClickable(true);
        if (response.isError()) {
            String message = response.getErrorMessage();
            //Check message is response who sent in ServiceBroker
            if (message.equals(NetworkConstants.RESPONSE_ERROR)){
                Toast.makeText(this, R.string.login_register_response_error, Toast.LENGTH_SHORT).show();
                clearEditTextFields();
            }
            else if (message.equals(NetworkConstants.NETWORK_ERROR))
                Toast.makeText(this, R.string.login_register_network_error, Toast.LENGTH_SHORT).show();
        } else {
            //if no error go to start
            Toast.makeText(this, R.string.login_done, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, StartActivity.class));
        }
    }


private void clearEditTextFields(){
    mLoginEmail.setText("");
    mPassword.setText("");
}
}
