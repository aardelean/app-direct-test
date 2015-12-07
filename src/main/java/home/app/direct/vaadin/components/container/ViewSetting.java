package home.app.direct.vaadin.components.container;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.SimpleStringFilter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ViewSetting implements Serializable{

	private String sort;
	private int pageSize = 20;
	private int pageNo = 1;
	private boolean asc = true;
	private Map<String, Object> filters = new HashMap<>();

	public static ViewSetting buildViewSettings(Set<Container.Filter> gridFilters,
	                                            Object propertyId,
	                                            Boolean asc,
	                                            Integer pageNo,
	                                            Integer pageSize){
		ViewSetting viewSetting = new ViewSetting();
		if(propertyId!=null) {
			viewSetting.setSort(propertyId.toString());
			viewSetting.setAsc(asc);
		}
		if(pageNo != null){
			viewSetting.setPageNo(pageNo);
		}
		if(pageSize != null){
			viewSetting.setPageSize(pageSize);
		}
		viewSetting.setFilters(viewSetting, gridFilters);
		return viewSetting;
	}

	private void setFilters(ViewSetting viewSetting, Set<Container.Filter> gridFilters) {
		if(gridFilters!=null && !gridFilters.isEmpty()){
			for(Container.Filter gridFilter: gridFilters){
				if(gridFilter.getClass().isAssignableFrom(SimpleStringFilter.class)){
					SimpleStringFilter simpleStringFilter = (SimpleStringFilter)gridFilter;
					if(!simpleStringFilter.getFilterString().isEmpty()) {
						viewSetting.addFilter(simpleStringFilter.getPropertyId().toString(),
								              simpleStringFilter.getFilterString());
					}
				}
			}
		}
	}

	public ViewSetting addFilter(String key, Object value){
		filters.put(key, value);
		return this;
	}

	public Set<Map.Entry<String, Object>> getFilters(){
		return filters.entrySet();
	}

	public void resetFilters(){
		filters.clear();
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}
	public boolean hasFilters(){
		return !filters.isEmpty();
	}
	public boolean hasSort(){
		return sort!=null;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
}
