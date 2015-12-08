package home.app.direct.vaadin.components.view.grid.container;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import home.app.direct.vaadin.components.view.grid.pagination.PaginationLayout;
import home.app.direct.vaadin.components.container.ContainerDelegate;
import home.app.direct.vaadin.components.container.ViewSetting;
import home.app.direct.vaadin.components.view.grid.fields.EntityEqualsDefinition;

import java.util.List;

public class PagingEntityContainer<T> extends BeanItemContainer<T> implements EntityContainerFacade<T>{

	private ContainerDelegate containerDelegate;
	private EntityEqualsDefinition<T> entityEqualsDefinition;
	private Object sortedPropertyId = null;
	private boolean asc = false;
	private Integer currentPage = 1;
	private Integer pageSize;
	private int totalSize;
	private Class<T> type;
	private PaginationLayout<T> paginationLayout;

	public PagingEntityContainer(Class<T> type,
	                             ContainerDelegate containerDelegate,
	                             EntityEqualsDefinition<T> entityEqualsDefinition, boolean formOnly, int pageSize) throws IllegalArgumentException {
		super(type);
		this.type = type;
		this.pageSize = pageSize;
		this.entityEqualsDefinition = entityEqualsDefinition;
		this.containerDelegate = containerDelegate;
		if(!formOnly) {
			refresh();
		}
	}

	@Override
	public void refresh() {
		super.removeAllItems();
		totalSize = containerDelegate.size(buildViewSettings(), type);
		ViewSetting viewSetting = buildViewSettings();
		List<T> items = containerDelegate.getItems(viewSetting, type);
		super.addAll(items);
		if(paginationLayout!=null){
			paginationLayout.refresh();
		}
	}

	private ViewSetting buildViewSettings() {
		return ViewSetting.buildViewSettings(getFilters(), sortedPropertyId, asc, currentPage, pageSize);
	}

	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
		if(propertyId!=null && propertyId.length>0){
			sortedPropertyId = propertyId[0];
			asc = ascending[0];
			refresh();
		}
	}

	@Override
	protected void addFilter(Filter filter) throws UnsupportedFilterException {
		getFilters().add(filter);
		currentPageNo(1);
	}

	@Override
	public T save(T newEntity){
		T result = null;
		if(entityEqualsDefinition != null) {
			boolean found = false;
			for (T existingItem : getItemIds()) {
				if (entityEqualsDefinition.match(existingItem, newEntity)){
					if(entityEqualsDefinition.keepOldEntity()){
						existingItem = entityEqualsDefinition.mergeNewValuesIntoExistingEntities(existingItem, newEntity);
						result = containerDelegate.mergeEntity(existingItem);
						if(entityEqualsDefinition.persistNewEntityAfterMerge()){
							result = addEntity(newEntity);
						}
					}else{
						delete(existingItem);
						result = addEntity(newEntity);
					}
					found = true;
					break;
				}
			}
			if(!found){
				result = addEntity(newEntity);
			}
		}else{
			result = addEntity(newEntity);
		}
		return result;
	}

	@Override
	public T mergeEntity(T entity) {
		return containerDelegate.mergeEntity(entity);
	}

	private T addEntity(T t) {
		containerDelegate.addEntity(t);
		super.addBean(t);
		return t;
	}

	@Override
	public void delete(T t){
		containerDelegate.deleteEntity(t);
	}

	@Override
	public void currentPageNo(Integer pageNo) {
		this.currentPage = pageNo;
		refresh();
	}

	@Override
	public void pageSize(Integer pageSize) {
		this.pageSize = pageSize;
		refresh();
	}

	@Override
	public Integer getPageSize() {
		return pageSize;
	}

	@Override
	public Integer getTotalSize() {
		return totalSize;
	}

	@Override
	public void setPaginationLayout(PaginationLayout<T> paginationLayout) {
		this.paginationLayout = paginationLayout;
	}

	@Override
	public Integer getCurrentPageNo() {
		return currentPage;
	}
}
