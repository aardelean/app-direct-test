package home.app.direct.vaadin.components.view.form;

public interface FormEvent {
	public void afterSaving(JPAForm form, Object entity);
	public void afterCanceling(JPAForm form);
}
