package ru.fbtw.navigator.map_builder.web_controllers.response;

public class AuthResponse implements BaseResponse{
    private boolean isSuccess;
    private String token;
    private String message;

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
