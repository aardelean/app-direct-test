package home.app.direct.vaadin.components.view.window;

import com.vaadin.ui.Window;
import home.app.direct.vaadin.components.view.form.FormEvent;
import home.app.direct.vaadin.components.view.form.JPAForm;

public class WindowCloser implements FormEvent {
	private Window window;
	private JPAForm form;

	public WindowCloser(Window window, JPAForm form) {
		this.window = window;
		this.form = form;
	}

	@Override
	public void afterSaving(JPAForm form, Object entity) {
		window.close();
	}

	@Override
	public void afterCanceling(JPAForm form) {
		form.getEntityFieldGroup().discard();
		window.close();
	}

}
