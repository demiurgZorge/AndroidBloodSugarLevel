package com.bloodsugarlevel.androidbloodsugarlevel.common;

import java.util.List;

public class ApiListResult<L extends List<?>> extends ApiSuccessResult<L> {
	
	private QueryMetaInformation metaInformation;
	
	public ApiListResult(L data, QueryMetaInformation info) {
		super(data);
		this.metaInformation = info;
	}

	public QueryMetaInformation getMetaInformation() {
		return metaInformation;
	}
	
}
