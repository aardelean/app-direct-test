package home.app.direct.vaadin.components.view.grid.fields;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import home.app.direct.vaadin.components.DateFormatter;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Date;

public class FieldFactory {
	public final static Date END_DATE = new DateTime().withYear(2099).withMonthOfYear(12).withDayOfMonth(31).toDate();
	public static ComboBox productCodeField() {
		ComboBox field = new ComboBox("Product Code", Arrays.asList("KRAT", "KRAM", "KAUF", "KAUM", "KRAI", "CCL", "CDC", "KDCK"));
		return field;
	}

	public static DateField validFromField(DateField field) {
		setDateFormatField(field);
		field.setValue(nextMidnight());
		return field;
	}

	public static DateField validToField(DateField field) {
		setDateFormatField(field);
		field.setValue(END_DATE);
		field.setReadOnly(true);
		return field;
	}

	private static void setDateFormatField(DateField field) {
		field.setDateFormat(DateFormatter.DEFAULT_DATE_FORMATTER);
	}

	public static Date nextMidnight(){
		return new DateTime().plusDays(1).withHourOfDay(0).withMinuteOfHour(1).withSecondOfMinute(0).toDate();
	}
}
