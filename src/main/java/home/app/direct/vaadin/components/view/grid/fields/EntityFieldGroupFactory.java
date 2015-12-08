package home.app.direct.vaadin.components.view.grid.fields;


import com.vaadin.data.fieldgroup.FieldGroup;
import home.app.direct.vaadin.components.view.form.ValidationEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class EntityFieldGroupFactory<T> {

	private Class<T> clazz;
	private final FieldDefinitionContainer fieldDefinitionContainer;
	private List<ValidationEvent<T>> validationEvents = new ArrayList<>();
	private Set<String> gridFields;
	private Set<String> filterableFields;
	private List<FieldGroup.CommitHandler> commitHandlers;

	public EntityFieldGroupFactory(Class<T> clazz, FieldDefinitionContainer fieldDefinitionContainer) {
		this.fieldDefinitionContainer = fieldDefinitionContainer;
		this.clazz = clazz;
	}
	public EntityFieldGroup<T> buildNewEntityFieldGroup(){
		return buildNewEntityFieldGroup(false);
	}

	public EntityFieldGroup<T> buildNewEntityFieldGroup(boolean isImport){
		EntityFieldGroup<T> entityFieldGroup= new EntityFieldGroup<T>(clazz, isImport) {
			@Override
			protected T newEntity() {
				return EntityFieldGroupFactory.this.newEntity();
			}

			@Override
			public FieldDefinitionContainer getFormFields() {
				return fieldDefinitionContainer;
			}

			@Override
			public Set<String> getGridFields() {
				Set<String> resultSet = EntityFieldGroupFactory.this.getGridFields();
				if(resultSet==null){
					resultSet = super.getGridFields();
				}
				return resultSet;
			}

			@Override
			public Set<String> getFilterFields() {
				Set<String> resultSet = EntityFieldGroupFactory.this.getFilterFields();
				if(resultSet==null){
					resultSet = super.getFilterFields();
				}
				return resultSet;
			}

		};
		if(validationEvents!=null && !validationEvents.isEmpty()){
			for(ValidationEvent validator : validationEvents){
				entityFieldGroup.addValidator(validator);
			}
		}
		if(commitHandlers!=null && !commitHandlers.isEmpty()){
			for(FieldGroup.CommitHandler commitHandler : commitHandlers){
				entityFieldGroup.addCommitHandler(commitHandler);
			}
		}
		return entityFieldGroup;
	}

	protected Set<String> getFilterFields(){
		return filterableFields;
	}

	public Set<String> getGridFields(){
		return gridFields;
	}

	public FieldDefinitionContainer getFormFields(){
		return fieldDefinitionContainer;
	}
	public abstract T newEntity();

	public void addValidator(ValidationEvent<T> validationEvent){
		validationEvents.add(validationEvent);
	}

	public void setGridFields(Set<String> gridFields) {
		this.gridFields = gridFields;
	}

	public void setFilterableFields(Set<String> filterableFields) {
		this.filterableFields = filterableFields;
	}

	public void setCommitHandlers(List<FieldGroup.CommitHandler> commitHandlers) {
		this.commitHandlers = commitHandlers;
	}
}
