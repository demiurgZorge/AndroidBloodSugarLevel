package com.bloodsugarlevel.androidbloodsugarlevel.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class QueryMetaInformation {
	
	private QueryState query;
	private Long recordCount = 0L;

	public QueryState getQuery() {
		return query;
	}
	
	public void setQuery(QueryState query) {
		this.query = query;
	}
	
	public Long getRecordCount() {
		return recordCount;
	}
	
	public void setRecordCount(Long totalRecords) {
		this.recordCount = totalRecords;
	}
}