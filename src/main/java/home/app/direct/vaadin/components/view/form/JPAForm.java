package home.app.direct.vaadin.components.view.form;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import home.app.direct.vaadin.components.view.grid.container.EntityContainerFacade;
import home.app.direct.vaadin.components.view.grid.fields.EntityFieldGroup;
import home.app.direct.vaadin.components.view.grid.fields.FieldDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JPAForm<T> extends CustomComponent {
	private static Logger logger = LoggerFactory.getLogger(JPAForm.class);

	private EntityContainerFacade<T> container;
	private Button save;
	private Button cancel;
	private VerticalLayout mainLayout;
	protected HorizontalLayout header;
	protected FormLayout form;
	protected HorizontalLayout footer;
	private EntityFieldGroup<T> fieldGroup;
	private boolean hasButtons = true;
	private List<FormEvent> formEventList = new ArrayList<>();
	private PersistEntityEvent<T> persistEntityEvent;

	public JPAForm(){
	}

	public JPAForm(EntityContainerFacade<T> container, EntityFieldGroup<T> fieldGroup){
		this(container, fieldGroup, null);
	}

	public JPAForm(EntityContainerFacade<T> container, EntityFieldGroup<T> fieldGroup, PersistEntityEvent<T> persistEntityEvent){
		this.persistEntityEvent = persistEntityEvent;
		init(container, fieldGroup);
	}

	protected void init(EntityContainerFacade<T> container, EntityFieldGroup<T> fieldGroup) {
		this.container=container;
		this.fieldGroup = fieldGroup;
		this.footer = new HorizontalLayout();
		this.header = new HorizontalLayout();
		this.form = new FormLayout();
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(new MarginInfo(true, true, true, true));
		mainLayout.addComponent(header);
		mainLayout.addComponent(form);
		for(FieldDefinition fieldEntry : fieldGroup.getFormFields()){
			String entityField = fieldEntry.getEntityField();
			addFieldToForm(entityField, fieldGroup.getFieldMap().get(entityField), fieldEntry.isReadOnly());
		}
		additionalFieldsToForm(form);
		mainLayout.addComponent(footer);
		if(hasButtons) {
			addFormButtons();
		}
		setCompositionRoot(mainLayout);
		setSizeUndefined();
	}

	private void addFieldToForm(String fieldId, Field field, boolean readOnly){
		field = fieldGroup.productCodeField(fieldId, field);
		field = customizeFormField(fieldId, field);
		fieldGroup.bind(field, fieldId);
		fieldGroup.dateFields(fieldId, field);
		field.setWidth(8, Unit.EM);
		field.setReadOnly(readOnly);
		form.addComponent(field);
	}

	private void addFormButtons() {
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSizeFull();
		buttons.setSpacing(true);
		save = new Button("Save");
		save.addClickListener(onSave());

		cancel = new Button("Cancel");
		cancel.addClickListener(onCancel());

		buttons.addComponent(save);
		buttons.addComponent(cancel);

		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		buttons.setComponentAlignment(save, Alignment.BOTTOM_RIGHT);
		buttons.setComponentAlignment(cancel, Alignment.BOTTOM_RIGHT);
		footer.getMargin().setMargins(true);
		footer.addComponent(buttons);
	}

	protected Button.ClickListener onSave(){
		JPAFormSaveListener saveListener = new JPAFormSaveListener<>(fieldGroup, this, container);
		if(persistEntityEvent == null){
			persistEntityEvent = (containerFacade, entity)->containerFacade.save(entity);
		}

		saveListener.setPersistEntityEvent(persistEntityEvent);
		return saveListener;
	}

	public void invokeAfterSaving(T t) {
		formEventList.forEach(e->e.afterSaving(this, t));
	}

	protected Button.ClickListener onCancel(){
		return p->formEventList.stream().forEach(e->e.afterCanceling(JPAForm.this));
	}

	public void commit() throws FieldGroup.CommitException {
		fieldGroup.commit();
		fieldGroup.validateBean();
	}

	public EntityFieldGroup getEntityFieldGroup(){
		return fieldGroup;
	}

	public void addActionListener(FormEvent formEvent){
		formEventList.add(formEvent);
	}

	public void addActionListeners(Collection<FormEvent> formEvents){
		formEventList.addAll(formEvents);
	}

	protected void additionalFieldsToForm(FormLayout form){}

	protected Field customizeFormField(String fieldId, Field field){
		return field;
	}

	public Layout getHeader(){
		return header;
	}

	public void setHasButtons(boolean hasButtons){
		this.hasButtons = hasButtons;
	}

	public void setPersistEntityEvent(PersistEntityEvent<T> persistEntityEvent) {
		this.persistEntityEvent = persistEntityEvent;
	}
}