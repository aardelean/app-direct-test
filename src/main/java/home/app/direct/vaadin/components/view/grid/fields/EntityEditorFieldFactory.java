package home.app.direct.vaadin.components.view.grid.fields;

import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.ui.Field;

import java.util.Date;

public class EntityEditorFieldFactory extends DefaultFieldGroupFieldFactory {

	@Override
	public <T extends Field> T createField(Class<?> type, Class<T> fieldType) {
		T resultField =  super.createField(type, fieldType);
		if(Date.class.isAssignableFrom(type)){
			resultField.setValue(new Date());
		}
		return resultField;
	}

}
