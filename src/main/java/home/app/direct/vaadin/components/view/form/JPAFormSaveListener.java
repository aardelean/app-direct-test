package home.app.direct.vaadin.components.view.form;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import home.app.direct.vaadin.components.view.grid.container.EntityContainerFacade;
import home.app.direct.vaadin.components.view.grid.fields.EntityFieldGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPAFormSaveListener<T> implements Button.ClickListener {

	private static final Logger logger = LoggerFactory.getLogger(JPAFormSaveListener.class);
	protected EntityFieldGroup<T> fieldGroup;
	protected JPAForm<T> form;
	protected EntityContainerFacade<T> container;
	private PersistEntityEvent<T> persistEntityEvent;

	public JPAFormSaveListener(EntityFieldGroup<T> fieldGroup, JPAForm<T> form, EntityContainerFacade<T> container) {
		this.fieldGroup = fieldGroup;
		this.form = form;
		this.container = container;
	}

	@Override
	public void buttonClick(Button.ClickEvent event) {
		try {
			T t = fieldGroup.getEntity();
			//save
			form.commit();
			persistEntityEvent.executePersist(container, t);
			container.refresh();
			//after saving event
			form.invokeAfterSaving(t);
		} catch (FieldGroup.CommitException e) {
			Notification.show("Sorry, could not insert new entity! Check the fields above.");
			logger.error("error commiting a new entity: ", e);
		}
	}

	public void setPersistEntityEvent(PersistEntityEvent<T> persistEntityEvent) {
		this.persistEntityEvent = persistEntityEvent;
	}
}
