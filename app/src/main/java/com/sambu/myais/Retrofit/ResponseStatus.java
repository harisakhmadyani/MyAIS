package com.sambu.myais.Retrofit;

public class ResponseStatus {

    String status;
    String message;

    public ResponseStatus(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
