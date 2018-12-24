package com.bloodsugarlevel.androidbloodsugarlevel.common;

import com.bloodsugarlevel.androidbloodsugarlevel.common.SortState.SortType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonInclude(value = Include.NON_NULL)
public class QueryState {

	private String queryString;

	@JsonProperty("paging")
    private PagingState paging;

	@JsonProperty("sorting")
    private SortState sorting;

	private List<FilterState> filters;
    private String view;

    public static QueryState create(Integer currentPosition, Integer pageSize){
        QueryState list = new QueryState();
        list.paging = new PagingState();
        list.paging.setCurrentPosition(currentPosition);
        list.paging.setPageSize(pageSize);
        list.filters = new ArrayList<FilterState>();
        return list;
    }

	public QueryState(String queryString) {
        this();
        this.queryString = queryString;
    }

	public QueryState(PagingState pagingState, SortState sortState, List<FilterState> filters) {
        this.paging = pagingState;
        this.sorting = sortState;
		this.filters = filters != null ?
				new ArrayList<FilterState>(filters) :
        		new ArrayList<FilterState>() ;
    }

    public QueryState() {
        paging = new PagingState();
        filters = new ArrayList<FilterState>();
    }


	@Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        }
        catch (JsonProcessingException e) {
            return super.toString();
        }
	}
//
//

    public JSONObject toJSONObject(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return new JSONObject(this.toString());
        }
        catch (Exception e) {
            return null;
        }
    }

    public PagingState getPagingState() {
        return paging;
    }

    public void setPagingState(PagingState pagingState) {
        this.paging = pagingState;
    }

    public SortState getSorting() {
        return sorting;
    }

    public void setSorting(SortState sortState) {
        this.sorting = sortState;
	}

    public void setSorting(SortEnum sort, SortType type) {
        this.sorting = new SortState(sort.toString(), type);
	}

    public void setSortState(String name, SortType type) {
    	this.sorting = new SortState(name, type);
	}

    public List<FilterState> getFilters() {
        return Collections.unmodifiableList(filters);
	}

    public FilterState addFilter(String name, Object value) {
    	if(name != null && value != null ) {
    		FilterState filterState = new FilterState(name, value);
    		filters.add(filterState);
    		return filterState;
    	}
    	return null;
	}

    public FilterState addFilter(FilterEnum filter, Object value) {
    	return addFilter(filter.toString(), value);
	}

    public void clearFilters() {
        filters.clear();
	}

    public void setFiltersState(List<FilterState> filters) {
        this.filters = new ArrayList<FilterState>(filters);
    }


	public String getQueryString() {
		return queryString;
	}


	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * Удобное добавление фильтров. Используется {@link #addFilter(String, Object)}.
	 *
	 * @param filter название фильтра
	 * @param val  значение
	 * @return спецификация с добавленным фильтром.
	 */
	public QueryState filterBy(final FilterEnum filter, Object val) {
        this.addFilter(filter, val);
        return this;
    }
	
	@Deprecated
	public QueryState filterBy(final String name, Object val) {
		this.addFilter(name, val);
		return this;
	}

	/**
	 * Удобное добавление сортировки. Используется {@link #setSorting(SortState)}
	 *
	 * @param field поле, по которому нужно сортировать
	 * @param ord   порядок сортировки
	 * @return спецификация с заданной сортировкой
	 */
    public QueryState sortBy(final SortEnum sort, final SortType ord) {

        this.setSorting(sort, ord);
        return this;
    }
	
	@Deprecated
	public QueryState sortBy(final String field, final SortType ord) {

		this.setSortState(field, ord);
		return this;
	}

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
}
