package com.bloodsugarlevel.androidbloodsugarlevel.common;

public class SortState {
	
	public static enum SortType {
		ASCENDING, DESCENDING
	}
	
	private String sortField;
	private SortType sortType;
	
	public SortState() {
	    
	}
	
	public SortState(String field, SortType type) {
		sortField = field;
		sortType = type;
	}
	
	public String getSortField() {
		return sortField;
	}
	
	public SortType getSortType() {
		return sortType;
	}
	
	public static SortState create(String name, String type) {
    	try {
    		SortType t = SortType.valueOf(type);
    		return new SortState(name, t);
    	}
    	catch(Exception e) {
    		return null;
    	}
    }
	
	public static SortState create(SortEnum name, SortType type) {
		return SortState.create(name.toString(), type.toString());
	}
	
	
	public boolean equals(String name) {
		return sortField.equals(name);
	}
	
	public boolean equals(SortEnum sort) {
		return sortField.equals(sort.toString());
	}
}
