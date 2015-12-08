package home.app.direct.vaadin.components.view.form;


import home.app.direct.vaadin.components.view.grid.container.EntityContainerFacade;

public interface PersistEntityEvent<T> {
	void executePersist(EntityContainerFacade<T> containerFacade, T entity);
}
