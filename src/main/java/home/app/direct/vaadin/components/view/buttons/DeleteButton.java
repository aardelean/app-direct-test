package home.app.direct.vaadin.components.view.buttons;

import com.vaadin.ui.Button;
import home.app.direct.vaadin.components.view.grid.JPAGrid;
import home.app.direct.vaadin.components.view.grid.container.EntityContainerFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeleteButton<T> extends Button {

	private static final Logger logger = LoggerFactory.getLogger(DeleteButton.class);

	private EntityContainerFacade<T> container;
	private JPAGrid<T> grid;
	private List<DeleteListener<T>> listeners;

	public DeleteButton(EntityContainerFacade<T> container, JPAGrid<T> grid){
		super("delete");
		this.container = container;
		this.grid = grid;
		setEnabled(false);
		init();
	}

	private void init(){
		addClickListener(event-> {
				if (event.getSource() == DeleteButton.this) {
					Collection<T> selectedRows = (Collection<T>) grid.getSelectedRows();
					for (T row : selectedRows) {
						executeBeforeListeners(row);
						container.delete(row);
						executeAfterListeners(row);
					}
					container.refresh();
					setEnabled(false);
				}
		});
	}
	private void executeBeforeListeners(T row){
		if(listeners!=null && !listeners.isEmpty()){
			listeners.forEach(l->l.beforeDelete(row));
		}
	}
	private void executeAfterListeners(T row){
		if(listeners!=null && !listeners.isEmpty()){
			listeners.forEach(l->l.afterDelete(row));
		}
	}

	public void addDeleteListener(DeleteListener<T> deleteListener){
		if(listeners==null){
			listeners = new ArrayList<>();
		}
		listeners.add(deleteListener);
	}

	public void setListeners(List<DeleteListener<T>> listeners) {
		this.listeners = listeners;
	}
}
