package com.bloodsugarlevel.androidbloodsugarlevel.common;

import java.util.Map;

public class ApiMapResult extends ApiSuccessResult<Map<String, Object>> {
	
	private QueryMetaInformation metaInformation;
	
	public ApiMapResult(Map<String, Object> data, QueryMetaInformation info) {
		this.data = data;
		this.metaInformation = info;
	}
	
	public QueryMetaInformation getMetaInformation() {
		return metaInformation;
	}

	public void setMetaInformation(QueryMetaInformation metaInformation) {
		this.metaInformation = metaInformation;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getResult(String key) {
		return (T)this.data.get(key);
	}
}
