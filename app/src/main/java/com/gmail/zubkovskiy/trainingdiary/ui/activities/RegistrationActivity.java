package com.gmail.zubkovskiy.trainingdiary.ui.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gmail.zubkovskiy.trainingdiary.R;
import com.gmail.zubkovskiy.trainingdiary.events.RegisterRequest;
import com.gmail.zubkovskiy.trainingdiary.events.RegisterResponse;
import com.gmail.zubkovskiy.trainingdiary.helpers.BusProvider;
import com.gmail.zubkovskiy.trainingdiary.network.NetworkConstants;
import com.gmail.zubkovskiy.trainingdiary.network.ServiceBroker;
import com.gmail.zubkovskiy.trainingdiary.utils.Logf;
import com.squareup.otto.Subscribe;

public class RegistrationActivity extends Activity {

    private ServiceBroker mServiceBroker;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mRePassword;
    private Button mRegister;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to create instance os ServiceBroker for use otto
        mServiceBroker = ServiceBroker.getInstance();

        setContentView(R.layout.activity_registration);
        mEmail = (EditText) findViewById(R.id.email_registration_et);
        mPassword = (EditText) findViewById(R.id.password_registraion_et);
        mRePassword = (EditText) findViewById(R.id.password_reenter_registration_et);
        mRegister = (Button) findViewById(R.id.register_b);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_registration_pb);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(mEmail.getText());
                String password = String.valueOf(mPassword.getText());
                String rePassword = String.valueOf(mRePassword.getText());
                if (email != null && password != null && rePassword != null) {
                    Logf.d("registrationActivity", "Base ultimatum");
                    if (!email.contains("@")) {
                        Logf.d("registrationActivity", " if !email.contains()");
                        Toast.makeText(RegistrationActivity.this, R.string.incorrect_email_registration,
                                Toast.LENGTH_SHORT).show();
                    } else if (!password.equals(rePassword)) {
                        Toast.makeText(RegistrationActivity.this, R.string.re_enter_password_incorrect,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        BusProvider.getInstance().post(new RegisterRequest(email, password));
                        mRegister.setClickable(false);
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                } else
                    Toast.makeText(RegistrationActivity.this, R.string.fill_all_fields,
                            Toast.LENGTH_SHORT).show();
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

    //This method is  reaction on RegisterResponse event in ServiceBroker register() method.
    @Subscribe
    public void onRegisterResponse(RegisterResponse response) {

        mRegister.setClickable(true);
        mProgressBar.setVisibility(View.INVISIBLE);

        if (response.isError()) {
            String message = response.getErrorMessage();
            //Check message is response who sent in ServiceBroker
            if (message.equals(NetworkConstants.RESPONSE_ERROR))
                Toast.makeText(this, R.string.login_register_response_error, Toast.LENGTH_SHORT).show();
            else if (message.equals(NetworkConstants.NETWORK_ERROR))
                Toast.makeText(this, R.string.login_register_network_error, Toast.LENGTH_LONG).show();

        } else {
            //if no error go to login
            Toast.makeText(this, R.string.registration_done, Toast.LENGTH_LONG).show();
           // Intent intent = new Intent(this,LoginActivity.class);
           // intent.putExtra()
            startActivity(new Intent(this, LoginActivity.class));

        }
    }
}
