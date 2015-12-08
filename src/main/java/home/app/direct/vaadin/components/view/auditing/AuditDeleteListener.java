package home.app.direct.vaadin.components.view.auditing;

import home.app.direct.vaadin.components.view.buttons.DeleteListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditDeleteListener<T> implements DeleteListener<T> {

	private static final Logger logger = LoggerFactory.getLogger(AuditCommitEvent.class);
	private String username;

	public AuditDeleteListener(String username) {
		this.username = username;
	}

	@Override
	public void beforeDelete(T entity) {

	}

	@Override
	public void afterDelete(T entity) {
		logger.info("USER: " +
		            username +
		            " delete : " +
		            entity.getClass().getSimpleName() +
		            " : " +
		            entity.toString());
	}
}
