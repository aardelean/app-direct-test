package home.app.direct.vaadin.components.view.window;

import com.vaadin.ui.Field;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import home.app.direct.vaadin.components.view.form.FormEvent;
import home.app.direct.vaadin.components.view.form.JPAForm;
import home.app.direct.vaadin.components.view.form.PersistEntityEvent;
import home.app.direct.vaadin.components.view.grid.container.EntityContainerFacade;
import home.app.direct.vaadin.components.view.grid.fields.EntityFieldGroup;

import java.util.Collection;

public class WindowForm<T> extends Window {

	private JPAForm form;

	public WindowForm(String name, EntityContainerFacade<T> container, EntityFieldGroup<T> entityFieldGroup){
		super(name);
		init(container, entityFieldGroup);
	}

	protected void init(EntityContainerFacade<T> container, EntityFieldGroup<T> entityFieldGroup){
		VerticalLayout subContent = new VerticalLayout();
		subContent.setMargin(true);
		subContent.addComponent(initForm(container, entityFieldGroup));
		setContent(subContent);
		center();
		setDraggable(true);
		setResizable(false);
		setPositionX(900);
		setPositionY(350);
	}

	private JPAForm initForm(EntityContainerFacade<T> container, EntityFieldGroup<T> entityFieldGroup){
		form = new JPAForm(container, entityFieldGroup, persistEvent()){
			@Override
			protected Field customizeFormField(String fieldId, Field field) {
				return WindowForm.this.customizeFormField(fieldId, field);
			}
		};
		WindowCloser windowCloser = new WindowCloser(this, form);
		form.addActionListener(windowCloser);
		return form;
	}

	protected PersistEntityEvent persistEvent(){
		return null;
	}

	protected Field customizeFormField(String fieldId, Field field) {
		return field;
	}

	public void addActionListeners(Collection<FormEvent> formEvents) {
		form.addActionListeners(formEvents);
	}
}
