package home.app.direct.vaadin.components.view.grid;

import com.vaadin.data.util.converter.StringToDateConverter;
import home.app.direct.vaadin.components.DateFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateConverter extends StringToDateConverter implements DateFormatter {
	@Override
	protected DateFormat getFormat(Locale locale) {
		return new SimpleDateFormat(DEFAULT_DATE_FORMATTER);
	}
}
