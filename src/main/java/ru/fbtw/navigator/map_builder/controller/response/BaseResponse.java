package ru.fbtw.navigator.map_builder.controller.response;

public interface BaseResponse {
    boolean isSuccess();

    void setSuccess(boolean success);

    String getMessage();

    void setMessage(String message);
}
