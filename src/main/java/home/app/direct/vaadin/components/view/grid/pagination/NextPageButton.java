package home.app.direct.vaadin.components.view.grid.pagination;


import home.app.direct.vaadin.components.view.grid.container.EntityContainerFacade;

public class NextPageButton<T> extends AbstractPageNumberButton<T> {

	private final static String NEXT_PAGE_LABEL = "NEXT";

	public NextPageButton(final PaginationLayout<T> parent){
		super(NEXT_PAGE_LABEL, parent.getMaxPageNumber(), parent);
	}

	@Override
	protected ClickListener changePageNo(Integer pageNo,final PaginationLayout<T> parent) {
		return p-> {
				EntityContainerFacade<T> container = parent.getContainer();
				container.currentPageNo(container.getCurrentPageNo()+1);
				parent.refresh();
				container.refresh();
		};
	}
}
