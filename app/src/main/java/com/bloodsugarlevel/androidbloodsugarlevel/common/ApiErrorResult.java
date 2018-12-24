package com.bloodsugarlevel.androidbloodsugarlevel.common;

public class ApiErrorResult extends ApiResult {

	private String message;
	private String code;
	private BaseException exception;

	public BaseException getException() {
        return exception;
    }

    public void setException(BaseException exception) {
        this.exception = exception;
    }

    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Status: false");
		sb.append("\n Code: " + code);
		sb.append("\n Message: " + message);
		return sb.toString();
	}

}
