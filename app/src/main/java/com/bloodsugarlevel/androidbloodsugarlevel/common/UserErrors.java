package com.bloodsugarlevel.androidbloodsugarlevel.common;

public enum UserErrors implements ErrorCodeEnum {

	USER_NOT_FOUND("User not found"),
	USER_ID_IS_NULL("User id is null"),
	USER_WITH_ID_NOT_FOUND("User with id not found"),
	USER_DOES_NOT_PRIVILEGE("User does not privelege"),
	NOT_AUTHORIZED("Not authorized"),;

	private final String text;


	UserErrors(String text) {
		this.text = text;
	}

	@Override
	public String code() {
		return this.name();
	}


	@Override
	public String toString() {
		return this.text;
	}
}
