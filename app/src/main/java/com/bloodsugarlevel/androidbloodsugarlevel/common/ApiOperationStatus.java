package com.bloodsugarlevel.androidbloodsugarlevel.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiOperationStatus<T>{
    private Long id;
    private boolean status;
    private T data;
    private String message;
    private String code;
    private Throwable exception;

    public ApiOperationStatus(){};

    public Long getId() {
        return id;
    }

    public boolean isStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public Throwable getException() {
        return exception;
    }
}
