package home.app.direct.vaadin.components.view;

import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import home.app.direct.vaadin.components.container.ContainerDelegate;
import home.app.direct.vaadin.components.view.auditing.AuditCommitEvent;
import home.app.direct.vaadin.components.view.auditing.AuditDeleteListener;
import home.app.direct.vaadin.components.view.buttons.DeleteListener;
import home.app.direct.vaadin.components.view.form.FormEvent;
import home.app.direct.vaadin.components.view.form.PersistEntityEvent;
import home.app.direct.vaadin.components.view.form.ValidationEvent;
import home.app.direct.vaadin.components.view.grid.GridFooter;
import home.app.direct.vaadin.components.view.grid.JPAGrid;
import home.app.direct.vaadin.components.view.grid.JPAGridBuilder;
import home.app.direct.vaadin.components.view.grid.PropertyResolver;
import home.app.direct.vaadin.components.view.grid.container.EntityContainerFacade;
import home.app.direct.vaadin.components.view.grid.container.PagingEntityContainer;
import home.app.direct.vaadin.components.view.grid.fields.EntityFieldGroupFactory;
import home.app.direct.vaadin.components.view.grid.fields.FieldDefinitionContainer;
import home.app.direct.vaadin.components.view.grid.container.EagerEntityContainerFacade;
import home.app.direct.vaadin.components.view.grid.fields.EntityEqualsDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author aardelean
 *
 * Generic class for adding ratanet - like grids all in one easily. Having a CRUD operations for any table
 * with import to csv and export to csv, sortable, filterable, hideble columns would be just as easy as
 * extending this class and overriding the abstract methods which are mandatory.
 *
 *
 * For building the appropriate container @see EntityContainerFacade
 *
 *
 * @param <T>
 */
public abstract class CompleteGrid<T> extends CustomComponent {

	private static final Logger logger = LoggerFactory.getLogger(CompleteGrid.class);
	public static final String START_VALIDITY_PROPERTY = "validFrom";
	public static final String END_VALIDITY_PROPERTY = "validTo";

	protected EntityContainerFacade<T> entityContainer;
	private EntityFieldGroupFactory<T> entityFieldGroupFactory;

	private VerticalLayout mainLayout;

	protected JPAGrid<T> grid;
	private boolean writeAccess;
	private GridFooter footerButtons;

	private List<FormEvent> formEventList = new ArrayList<>();
	private List<DeleteListener<T>> listeners;
	private List<FieldGroup.CommitHandler> commitHandlers = new ArrayList<>();
	private EntityEqualsDefinition entityEqualsDefinition = null;
	private boolean firstPageOnly = false;
	private boolean hasPagination = false;
	private int pageSize = 30;
	private boolean addAllowed = true;
	private boolean stopAllowed = true;
	private boolean deleteAllowed = false;
	private boolean exportAllowed = true;
	private boolean inlineEditAllowed = false;
	private PersistEntityEvent persistEntityEvent= null;
	private List<PropertyResolver> gridPropertyResolvers = new ArrayList<>();
	private String formLabel = "";
	private boolean usePopupForm = true;
	private Set<String> filterFields = null;
	private Set<String> gridFields = null;

	public CompleteGrid() {
	}

	public CompleteGrid start(boolean writeAccess, String username){
		this.writeAccess = writeAccess;
		this.entityContainer = buildEntityContainerFacade();
		entityFieldGroupFactory = buildEntityFieldGroupFactory();
		mainLayout = new VerticalLayout();
		grid = buildGrid();
		addAuditingCapabilities(writeAccess, username);
		footerButtons = footerActions();
		mainLayout.addComponent(grid);
		mainLayout.addComponent(footerButtons);
		setCompositionRoot(mainLayout);
		return this;
	}

	private void addAuditingCapabilities(boolean writeAccess, String username) {
		if(writeAccess && username != null){
			AuditCommitEvent auditCommitEvent = new AuditCommitEvent(username);
			addCommitHandler(auditCommitEvent);
			AuditDeleteListener<T> auditDeleteListener = new AuditDeleteListener<>(username);
			addDeleteListener(auditDeleteListener);
		}
	}

	private GridFooter<T> footerActions() {
		return new GridFooter<T>()
				.deleteAllowed(isDeleteAllowed())
				.entityFieldGroupFactory(entityFieldGroupFactory)
				.exportAllowed(isExportAllowed())
				.formEventList(formEventList)
				.formLabel(getFormLabel())
				.popupForm(usePopupForm())
			    .addAllowed(isAddAllowed())
				.writeRights(hasWriteRights())
			    .hasPaginationComponent(hasPagination() && !firstPageOnly())
				.grid(grid)
				.persistEntityEvent(getPersistEntityEvent())
			    .deleteListeners(listeners)
				.entityContainer(entityContainer)
				.init();
	}

	/****************************************GRID CUSTOMIZATION ******************/
	private JPAGrid<T> buildGrid(){
		final JPAGrid<T> grid = new JPAGridBuilder<T>(){
			@Override
			protected void customFilters(JPAGrid<T> grid, EntityContainerFacade<T> container) {
				CompleteGrid.this.gridCustomFilters(grid, container);
			}

			@Override
			protected void customizeRenderer(Grid.Column column) {
				CompleteGrid.this.customizeRenderer(column);
			}
		}
				.container(entityContainer)
				.entityFieldGroup(entityFieldGroupFactory.buildNewEntityFieldGroup())
				.editAllowed(isEditAllowed())
				.additionalPropertyResolvers(getGridPropertyResolvers())
				.build();

		if(isDeleteAllowed()) {
			grid.addSelectionListener(p -> {
					if (footerButtons.getDeleteButton() != null) {
						if (grid.getSelectedRows() != null && grid.getSelectedRows().size() == 1) {
							footerButtons.getDeleteButton().setEnabled(true);
						} else {
							footerButtons.getDeleteButton().setEnabled(false);
						}
					}
			});
		}

		return grid;
	}

	protected void customizeRenderer(Grid.Column column) {

	}


	private EntityFieldGroupFactory buildEntityFieldGroupFactory() {
		EntityFieldGroupFactory<T> entityFieldGroupFactory = new EntityFieldGroupFactory<T>(getType(), CompleteGrid.this.getFormFields()) {
			@Override
			public T newEntity() {
				return CompleteGrid.this.newEntity();
			}
		};
		entityFieldGroupFactory.setFilterableFields(getFilterFields());
		entityFieldGroupFactory.setGridFields(getGridFields());
		entityFieldGroupFactory.addValidator(t-> CompleteGrid.this.validate(t));
		entityFieldGroupFactory.setCommitHandlers(commitHandlers);
		return entityFieldGroupFactory;
	}

	private Class<T> getType() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/** CONFIGURATION OF THE CRUD FORM AND GRID OPERATIONS **/
	/**
	 * Override this to validate a full bean before saving it, when it has all properties set.
	 */
	protected void validate(T t) throws Validator.InvalidValueException{}

	/**
	 * Creates a new entity of the type.
	 */
	protected abstract T newEntity();

	/**
	 * A collection of all the fields which will be present in the form for saving.
	 */
	protected abstract FieldDefinitionContainer getFormFields();

	/**
	 * A collection of all the fields present in the grid view.
	 */
	protected Set<String> getGridFields() {
		return gridFields;
	}

	/**
	 * A collection of the fields which can be filtered within a search on the grid.
	 */
	protected Set<String> getFilterFields() {
		return filterFields;
	}

	/**
	 * Adds custom validation for a bean. Similar with validate method.
	 */
	public void addValidator(ValidationEvent<T> validationEvent) {
		entityFieldGroupFactory.addValidator(validationEvent);
	}

	/**
	 * If true, the form will be open in a popup window. If false, will use a new grid line.
	 */
	protected boolean usePopupForm(){
		return usePopupForm;
	}

	/**
	 * Special customization over each field, like additional validation before setter of the entity is invoked.
	 */
	protected Field customizeFormField(String fieldId, Field field) {
		return field;
	}

	/**
	 * The title of the window form.
	 */
	protected String getFormLabel() {
		return formLabel;
	}

	/**
	 * If true, inline editing of the grid is enabled. Do not see a reason why this should be true in our business logic,
	 * as we only add, never edit entities.
	 */
	protected boolean isEditAllowed() {
		return inlineEditAllowed;
	}

	/**
	 * If true, button for exporting is present. Default export type is CSV.
	 */
	protected boolean isExportAllowed(){
		return exportAllowed;
	}


	/**
	 * Adds a new column, based on the existing ones to the grid.
	 */
	protected List<PropertyResolver> getGridPropertyResolvers(){
		return gridPropertyResolvers;
	}

	/**
	 * Works just with Eager fetching container, not the pagination, as it is a view filter, not database
	 * backed.
	 * <pre>
	 * {@code
		@Override
		protected void gridCustomFilters(JPAGrid<Contact> grid, final EagerEntityContainerFacade<Contact> container){
			final Container.Filter dead = new Container.Filter() {
				@Override
				public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
					return !(Boolean) item.getItemProperty("alive").getValue();
				}

				@Override
				public boolean appliesToProperty(Object propertyId) {
					return propertyId.equals("alive");
				}
			};
			CheckBox deadOnly = new CheckBox("dead only");
			deadOnly.addValueChangeListener(new Property.ValueChangeListener() {
				@Override
				public void valueChange(Property.ValueChangeEvent event) {
					if ((Boolean) event.getProperty().getValue()) {
						container.addContainerFilter(dead);
					} else {
						container.removeContainerFilter(dead);
					}
				}
			});
			grid.getFilteringHeader().getCell("alive").setComponent(deadOnly);
		}
	 * </pre>
	 */
	protected void gridCustomFilters(JPAGrid<T> grid, EntityContainerFacade<T> container) {	}

	/**
	 * Builds the container of this component. Uses EntityContainerFacade interface for the
	 * generic operations done over the entities.
	 * @see
	 */
	protected EntityContainerFacade<T> buildEntityContainerFacade(){
		if(hasPagination() || firstPageOnly()){
			return new PagingEntityContainer<T>(getType(),
			                                   getContainerDelegate(),
			                                   getEntityEqualsDefinition(),
											   false,
			                                   getPageSize());
		}else{
			return new EagerEntityContainerFacade<T>(getType(),
			                                        getContainerDelegate(),
			                                        getEntityEqualsDefinition(),
													false);
		}
	}

	protected abstract ContainerDelegate getContainerDelegate();

	/**
	 * If the user logged in has rights for writing new/edit/stop/import for the entity.
	 */
	public boolean hasWriteRights() {
		return writeAccess;
	}

	/**
	 * If delete button should be allowed on the view (Deletes entities from the database).
	 */
	public boolean isDeleteAllowed() {
		return deleteAllowed;
	}

	/**
	 * Generic Form save and cancel listeners.
	 */
	public void addActionListener(FormEvent formEvent) {
		formEventList.add(formEvent);
	}

	/**
	 * Generic commit handlers, before and after the entity was commited to database.
	 */
	public void addCommitHandler(FieldGroup.CommitHandler commitHandler){
		commitHandlers.add(commitHandler);
	}

	/**
	 * If the stop button is present on the grid.
	 * @return
	 */
	public boolean isStopAllowed(){
		return stopAllowed;
	}

	public void addDeleteListener(DeleteListener<T> deleteListener){
		if(listeners==null){
			listeners = new ArrayList<>();
		}
		listeners.add(deleteListener);
	}

	protected PersistEntityEvent getPersistEntityEvent(){
		return persistEntityEvent;
	}

	protected boolean isAddAllowed(){
		return addAllowed;
	}

	protected int getPageSize(){
		return pageSize;
	}

	protected boolean hasPagination(){
		return hasPagination;
	}

	protected boolean firstPageOnly(){
		return firstPageOnly;
	}

	/**
	 * Return a new implementation if add and import of an entity should stop or merge other.
	 */
	protected EntityEqualsDefinition getEntityEqualsDefinition(){
		return entityEqualsDefinition;
	}

	public void setWriteAccess(boolean writeAccess) {
		this.writeAccess = writeAccess;
	}

	public void setFormEventList(List<FormEvent> formEventList) {
		this.formEventList = formEventList;
	}

	public void setListeners(List<DeleteListener<T>> listeners) {
		this.listeners = listeners;
	}

	public void setCommitHandlers(List<FieldGroup.CommitHandler> commitHandlers) {
		this.commitHandlers = commitHandlers;
	}

	public void setEntityEqualsDefinition(EntityEqualsDefinition entityEqualsDefinition) {
		this.entityEqualsDefinition = entityEqualsDefinition;
	}

	public void setFirstPageOnly(boolean firstPageOnly) {
		this.firstPageOnly = firstPageOnly;
	}

	public void setHasPagination(boolean hasPagination) {
		this.hasPagination = hasPagination;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setAddAllowed(boolean addAllowed) {
		this.addAllowed = addAllowed;
	}

	public void setStopAllowed(boolean stopAllowed) {
		this.stopAllowed = stopAllowed;
	}

	public void setDeleteAllowed(boolean deleteAllowed) {
		this.deleteAllowed = deleteAllowed;
	}


	public void setExportAllowed(boolean exportAllowed) {
		this.exportAllowed = exportAllowed;
	}

	public void setInlineEditAllowed(boolean inlineEditAllowed) {
		this.inlineEditAllowed = inlineEditAllowed;
	}

	public void setPersistEntityEvent(PersistEntityEvent persistEntityEvent) {
		this.persistEntityEvent = persistEntityEvent;
	}

	public void setGridPropertyResolvers(List<PropertyResolver> gridPropertyResolvers) {
		this.gridPropertyResolvers = gridPropertyResolvers;
	}

	public void setFormLabel(String formLabel) {
		this.formLabel = formLabel;
	}

	public void setFilterFields(Set<String> filterFields) {
		this.filterFields = filterFields;
	}

	public void setGridFields(Set<String> gridFields) {
		this.gridFields = gridFields;
	}
}
