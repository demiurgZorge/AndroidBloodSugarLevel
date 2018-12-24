package com.bloodsugarlevel.androidbloodsugarlevel.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 8739998487480187268L;

	protected String errorCode;
	protected String message;
	protected ApiErrorResult error;

	public BaseException(){
	}

	/**
	 * Инициализация исключения ошибкой.
	 *
	 * @param errCode код и описание
	 */
	public BaseException(final ErrorCodeEnum errCode) {
		super(errCode.toString());
		this.message = errCode.toString();
		this.errorCode = errCode.code();
	}

	public BaseException(final String errCode) {
		super(errCode);
		this.errorCode = errCode;
		this.message = "";
	}

	public BaseException(Throwable ex) {
	    super(ex);
	}

	@SuppressWarnings("unchecked")
	public <T> ApiDataErrorResult<T> getDataErrorResult() {
        if (error instanceof ApiDataErrorResult<?>) {
            return (ApiDataErrorResult<T>)error;
        }
        return null;
    }

	public <T> T getErrorData() {
	    ApiDataErrorResult<T> result = getDataErrorResult();
	    if (result != null) {
	        return result.getErrorDto();
	    }
	    return null;
	}

    @Override
	public String getMessage() {
		return message;
	}

	public String getErrorCode() {
		return errorCode;
	}

    @Override
    public String toString() {
        String s = "ERROR: " + errorCode;
        if (this.message != null) {
            s = s + " \n Message: " + this.message;
        }
        return s;
    }

    public BaseException(ApiErrorResult error) {
        super();
        this.errorCode = error.getCode();
        this.message = error.getMessage();
        this.error = error;
    }

	public static BaseException create(ApiErrorResult error) {
        BaseException ex = new BaseException(error);
        return ex;
    }

    public static BaseException create(Exception e) {
        BaseException ex = new BaseException(e);
        return ex;
    }


	/**
	 * Проверить текущий код ошибки на принадлежность списку.
	 *
	 * @param codes список кодов исключения
	 * @return если текущий код присутствует в списке - true, иначе - false
	 */
	public boolean codeEquals(final ErrorCodeEnum... codes) {

		for (ErrorCodeEnum c : codes) {
			if (this.errorCode.equals(c.code())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Выполняет проверку кода исключения.<br />
	 * Если текущий код совпадает с одним из предложенных вариантов - метод завершится.<br />
	 * Есл же не совпадает - будет выброшена {@link AssertionError} с сообщением "Wrong error code gotten".
	 * @param codes допустимые коды ошибки.
	 */
	public void assertCode(ErrorCodeEnum ... codes) {
		//Assert.assertTrue("Wrong error code gotten", this.codeEquals(codes));
	}
}
