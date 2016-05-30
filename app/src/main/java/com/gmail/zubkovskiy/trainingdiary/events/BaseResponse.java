package com.gmail.zubkovskiy.trainingdiary.events;


public class BaseResponse {
    private boolean isError;
    private String ErrorMessage;

    BaseResponse(boolean isError){
       this.isError = isError;
    }

    BaseResponse(boolean isError, String errorMessage){

        this.isError = isError;
        this.ErrorMessage = errorMessage;
    }

    public boolean isError() {
        return isError;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }
}
