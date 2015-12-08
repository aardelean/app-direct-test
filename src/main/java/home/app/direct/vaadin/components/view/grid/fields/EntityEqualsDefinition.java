package home.app.direct.vaadin.components.view.grid.fields;

public interface EntityEqualsDefinition<T> {

	boolean match(T existingEntity, T newEntity);

	T mergeNewValuesIntoExistingEntities(T existingEntity, T newEntity);

	boolean keepOldEntity();

	boolean persistNewEntityAfterMerge();
}
