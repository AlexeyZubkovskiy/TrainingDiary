package com.gmail.zubkovskiy.trainingdiary.events;

/**
 * Created by alexey.zubkovskiy@gmail.com on 27.04.2016.
 */
public class RegisterResponse extends BaseResponse {

    public RegisterResponse(boolean isError) {
        super(isError);
    }

    public RegisterResponse(boolean isError, String errorMessage) {
        super(isError, errorMessage);
    }
}
