package home.app.direct.vaadin.components.view.grid.fields;

import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import home.app.direct.vaadin.components.DateFormatter;
import org.joda.time.DateTime;

import java.util.Date;

public class EditorFieldGroup extends FieldGroup implements DateFormatter {
    private String endValidityColumnName;
    public EditorFieldGroup(FieldGroupFieldFactory factory, String endValidityColumnName) {
        this.endValidityColumnName = endValidityColumnName;
        this.setFieldFactory(factory);
    }

    protected Class<?> getPropertyType(Container.Indexed container, Object propertyId) throws BindException {
        return this.getItemDataSource() == null?container.getType(propertyId):super.getPropertyType(propertyId);
    }

    @Override
    public void bind(Field<?> field, Object propertyId) throws BindException {
        super.bind(field, propertyId);
        Date stopDate = new DateTime().withMillisOfDay(0).toDate();
        if(propertyId.equals(endValidityColumnName)){
            DateField dateField = (DateField)field;
            dateField.setDateFormat(DEFAULT_DATE_FORMATTER);
            dateField.setValue(stopDate);
            dateField.setRangeStart(stopDate);
        }else{
            field.setReadOnly(true);
        }
    }
}
