package home.app.direct.subscription;

import home.app.direct.vaadin.components.container.AbstractContainerDelegate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class SubscriptionContainer extends AbstractContainerDelegate {

    @PersistenceContext
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
