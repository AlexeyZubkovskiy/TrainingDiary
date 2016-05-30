package com.gmail.zubkovskiy.trainingdiary.network;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.gmail.zubkovskiy.trainingdiary.events.LoginRequest;
import com.gmail.zubkovskiy.trainingdiary.events.LoginResponse;
import com.gmail.zubkovskiy.trainingdiary.events.RegisterRequest;
import com.gmail.zubkovskiy.trainingdiary.events.RegisterResponse;
import com.gmail.zubkovskiy.trainingdiary.helpers.BusProvider;
import com.gmail.zubkovskiy.trainingdiary.model.Users;
import com.gmail.zubkovskiy.trainingdiary.network.api.IBaasApi;
import com.gmail.zubkovskiy.trainingdiary.network.deserializer.DateDeserializer;
import com.gmail.zubkovskiy.trainingdiary.network.interceptors.HttpLoggingInterceptor;
import com.gmail.zubkovskiy.trainingdiary.utils.Logf;
import com.gmail.zubkovskiy.trainingdiary.utils.SharedPreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.Date;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alexey.zubkovskiy@gmail.com on 26.04.2016.
 */
public class ServiceBroker {

    private static final String TAG = ServiceBroker.class.getSimpleName();

    private static final String KEY_TOKEN = "key-token";

    private static ServiceBroker instance = new ServiceBroker();

    private Retrofit mRetrofit;

    private ServiceBroker() {
        //to start react on events by BusProvider //packages events and helpers
        BusProvider.getInstance().register(this);
    }

    public static ServiceBroker getInstance() {
        return instance;
    }

    public Retrofit getRetrofit() {

        if (mRetrofit == null) {

            OkHttpClient httpBuilder = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor())
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {

                            Request request = chain.request();
                            Headers.Builder builder = request.headers().newBuilder();
                            builder.add(NetworkConstants.APPLICATION_ID, NetworkConstants.BAAS_REST_API_ID)
                                    .add(NetworkConstants.SECRET_KEY, NetworkConstants.BAAS_REST_API_KEY)
                                    .add(NetworkConstants.CONTENT_TYPE, NetworkConstants.BAAS_CONTENT_TYPE)
                                    .add(NetworkConstants.APPLICATION_TYPE, NetworkConstants.BAAS_APPLICATION_TYPE);

                            String token = SharedPreferenceUtil.getToken();

                            //if user is login add to request unique user-token
                            if (!TextUtils.isEmpty(token)) {
                                builder.add(NetworkConstants.USER_TOKEN, token);
                            }

                            request = request.newBuilder().headers(builder.build()).build();

                            return chain.proceed(request);

                        }
                    })
                    .build();

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
            Gson gson = gsonBuilder.create();

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(NetworkConstants.API_BASE_URL)
                    .client(httpBuilder)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return mRetrofit;
    }

    @Subscribe
    public void register(RegisterRequest registerRequest) {

        IBaasApi baasApi = getRetrofit().create(IBaasApi.class);

        Call<Users> call = baasApi.register(registerRequest);

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, retrofit2.Response<Users> response) {

                Logf.d(TAG, "register onResponse()");

                Users user = response.body();

                //if is registered
                if (response.isSuccessful() && user != null) {
                    BusProvider.getInstance().post(new RegisterResponse(false));
                    Logf.d(TAG, "register onResponse SUCCESS" + user.toString());
                } else {
                    //if mistake in request, or server find mistake in request do this code:
                    try {
                        Logf.d(TAG, "register onResponse() response error code " + response.code());
                        Logf.d(TAG, "register onResponse() response error body " + response.errorBody().string());

                    } catch (IOException e) {
                        Logf.d(TAG, "register onResponse() try/catch Exception" + e.getMessage());
                    }
                    //CallBack by BusProvider by otto library
                    BusProvider.getInstance().post(new RegisterResponse(true, NetworkConstants.RESPONSE_ERROR));
                }


            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                //This code invoked when error in Network, or no Network, or mistake in code
                Logf.d(TAG, "register onFailure() big error" + t.toString() + "\n" + t.getMessage());

                //CallBack by BusProvider by otto library
                BusProvider.getInstance().post(new LoginResponse(true, NetworkConstants.NETWORK_ERROR));
            }
        });
    }

    //This annotation for otto library to subscribe on events
    @Subscribe
    public void login(LoginRequest loginRequest) {

        IBaasApi baasApi = getRetrofit().create(IBaasApi.class);

        Call<Users> call = baasApi.login(loginRequest);

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, retrofit2.Response<Users> response) {

                Logf.d(TAG, "login onResponse()");

                Users user = response.body();
                //if all is good...
                if (response.isSuccessful() && user != null && !TextUtils.isEmpty(user.getUserToken())) {

                    Logf.d("login onResponse() SUCCESS", user.toString());

                    //send to BusProvider by otto library (CallBack)
                    BusProvider.getInstance().post(new LoginResponse(false));

                    //save user-token and login(email) in SharedPreferences for future use
                    SharedPreferenceUtil.saveToken(user.getUserToken());
                    SharedPreferenceUtil.saveLogin(user.getEmail());
                } else {
                    //if mistake in request, or server find mistake in request do this code:
                    try {
                        Logf.d(TAG, "login onResponse() response error code " + response.code());
                        Logf.d(TAG, "login onResponse() response error body " + response.errorBody().string());

                    } catch (IOException e) {
                        Logf.d(TAG, "login onResponse() try/catch Exception" + e.getMessage());
                    }
                    //CallBack by BusProvider by otto library
                    BusProvider.getInstance().post(new LoginResponse(true, NetworkConstants.RESPONSE_ERROR));


                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {

                //This code invoked when error in Network, or no Network, or mistake in code
                Logf.d(TAG, "login onFailure() big error" + t.toString() + "\n" + t.getMessage());

                //CallBack by BusProvider by otto library
                BusProvider.getInstance().post(new LoginResponse(true, NetworkConstants.NETWORK_ERROR));
            }
        });


    }


}