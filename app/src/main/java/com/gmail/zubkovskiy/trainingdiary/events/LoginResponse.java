package com.gmail.zubkovskiy.trainingdiary.events;


public class LoginResponse extends BaseResponse {

    public LoginResponse(boolean isError){
        super(isError);
    }

    public LoginResponse(boolean isError, String errorMessage) {
        super(isError, errorMessage);
    }
}
