package com.bloodsugarlevel.androidbloodsugarlevel.common;

/**
 * Единая система описания ошибок. Этот enum включает базовые.<br />
 * Используются в конструкторе <code>{@link BaseException#BaseException(ErrorCodeEnum)}</code>
 */
public enum BaseErrors implements ErrorCodeEnum {

	INCORRECT_SET_PASSWORD("Incorrect set password"),
	CONTENT_URL_NOT_FOUND_BY_URL("CONTENT_URL_NOT_FOUND_BY_URL"),
	GROUP_NOT_FOUND_BY_ID("GROUP_NOT_FOUND_BY_ID"),
	WRONG_ERROR_CODE_GOTTEN("Wrong error code gotten");


	private final String text;


	BaseErrors(String text) {
		this.text = text;
	}


	/**
	 * Здаётся строковым литератом при создании константы.
	 *
	 * @return текстовое опиание
	 */
	@Override
	public String toString() {
		return this.text;
	}

	/**
	 * Само имя константы.
	 *
	 * @return код ошибки
	 */
	@Override
	public String code() {
		return this.name();
	}
}