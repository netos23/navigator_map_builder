package ru.fbtw.navigator.map_builder.controller.response;

public class Response implements  BaseResponse{
    private String message;
    private boolean isSuccess;

    public Response() {
    }

    public Response(String message, boolean isSuccess) {
        this.message = message;
        this.isSuccess = isSuccess;
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

}
