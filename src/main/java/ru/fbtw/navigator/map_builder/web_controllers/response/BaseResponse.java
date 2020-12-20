package ru.fbtw.navigator.map_builder.web_controllers.response;

public interface BaseResponse {
    boolean isSuccess();

    void setSuccess(boolean success);

    String getMessage();

    void setMessage(String message);
}
