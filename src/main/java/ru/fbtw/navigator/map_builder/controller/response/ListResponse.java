package ru.fbtw.navigator.map_builder.controller.response;

import java.util.List;

public class ListResponse<T> implements BaseResponse{
    private List<T> body;
    private String message;
    private boolean isSuccess;

    public ListResponse() {
    }

    public ListResponse(List<T> body, String message, boolean isSuccess) {
        this.body = body;
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

    public List<T> getBody() {
        return body;
    }

    public void setBody(List<T> body) {
        this.body = body;
    }
}
