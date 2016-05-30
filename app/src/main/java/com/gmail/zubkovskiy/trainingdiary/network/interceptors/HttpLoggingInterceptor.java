package com.gmail.zubkovskiy.trainingdiary.network.interceptors;

import com.gmail.zubkovskiy.trainingdiary.utils.Logf;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class HttpLoggingInterceptor implements Interceptor {

    private static final String TAG = "HTTPS";

    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Logf.i(TAG, "Sending request " + request.url());
        Logf.i(TAG, "Request connection " + chain.connection());
        Logf.i(TAG, "Request Headers: " + request.headers());
        Logf.i(TAG, "REQUEST BODY BEGIN");
        Logf.i(TAG, bodyToString(request));
        Logf.i(TAG, "REQUEST BODY END");

        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        String responseBodyString = response.body().string();

        // now we have extracted the response body but in the process
        // we have consumed the original response and can't read it again
        // so we need to build a new one to return from this method

        Response newResponse = response.newBuilder().body(ResponseBody.create(responseBody.contentType(),
                responseBodyString.getBytes())).build();

        Logf.i(TAG, "Received response for " + response.request().url());
        Logf.i(TAG, "Received response Headers: " + response.headers());
        Logf.i(TAG, "RESPONSE BODY BEGIN");
        Logf.i(TAG, responseBodyString);
        Logf.i(TAG, "RESPONSE BODY END");

        return newResponse;
    }

    private static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final Exception e) {
            return "did not work";
        }
    }
}
