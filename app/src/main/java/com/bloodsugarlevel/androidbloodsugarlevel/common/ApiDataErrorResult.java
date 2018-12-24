package com.bloodsugarlevel.androidbloodsugarlevel.common;

public class ApiDataErrorResult<T> extends ApiErrorResult {
    
    private T errorDto;
    
    private Object errorData;
    
    public Object getErrorData() {
        return errorData;
    }

    public void setErrorData(Object errorData) {
        this.errorData = errorData;
    }

    public T getErrorDto() {
        return errorDto;
    }
    
    @SuppressWarnings("unchecked")
    public void setErrorDto(Object errorDto) {
        this.errorDto = (T)errorDto;
    }
}
