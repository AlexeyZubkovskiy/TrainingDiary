package com.gmail.zubkovskiy.trainingdiary.network.api;


import com.gmail.zubkovskiy.trainingdiary.events.LoginRequest;
import com.gmail.zubkovskiy.trainingdiary.events.RegisterRequest;
import com.gmail.zubkovskiy.trainingdiary.model.Users;
import com.gmail.zubkovskiy.trainingdiary.network.NetworkConstants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IBaasApi {

    @POST(NetworkConstants.APP_VERSION + "/users/login")
    Call<Users> login(@Body LoginRequest loginRequest);

    @POST(NetworkConstants.APP_VERSION + "/users/register")
    Call<Users> register(@Body RegisterRequest registerRequest);



}
