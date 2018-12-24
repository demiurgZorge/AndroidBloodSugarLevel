package com.bloodsugarlevel.androidbloodsugarlevel.common;

public class PagingState {
    
	public static final int  MAX_PAGE_SIZE = 100;
	
    private Integer currentPosition;
    private Integer pageSize;
     
    public PagingState() {
    	construct(null, null);
    }
    
    public PagingState(Integer position, Integer size) {
		construct(position, size);
    }
    
    private void construct(Integer position, Integer size) {
    	if(position == null) {
			currentPosition = 0;
		}
		else {
			currentPosition = position;
		}
		
		if(size == null) {
			pageSize = MAX_PAGE_SIZE;
		}
		else {
			pageSize = size;
		}
    }
    
    public void setCurrentPosition(int currentPosition){
        this.currentPosition = currentPosition;
    }
    
    public int getCurrentPosition() {
        return currentPosition;
    }
    
    public void setPageSize(int pageSize){
        this.pageSize = pageSize;
    }
    
    public int getPageSize(){
        return pageSize;
    }
     
}
