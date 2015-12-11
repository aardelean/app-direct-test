package home.app.direct.orders.subscription.view;

import com.google.common.collect.ImmutableSet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import home.app.direct.orders.SimpleGrid;
import home.app.direct.orders.subscription.entity.Subscription;
import home.app.direct.orders.subscription.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

@SpringComponent
@UIScope
public class SubscriptionGrid extends SimpleGrid<Subscription> {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public SubscriptionGrid() {
        super("Subscriptions", Subscription.class);
    }

    @Override
    protected CrudRepository<Subscription, ?> getRepository() {
        return subscriptionRepository;
    }

    @Override
    protected Set<String> getColumnNames() {
        return ImmutableSet.of(       "identifier",
                                      "subscriptionOrder.pricingDuration",
                                      "subscriptionOrder.editionCode",
                                      "subscriptionStatus",
                                      "user.email",
                                      "user.firstName",
                                      "user.lastName",
                                      "user.language",
                                      "user.openId",
                                      "user.uuid",
                                      "company.country",
                                      "company.email",
                                      "company.name",
                                      "company.uuid",
                                      "company.website");
    }
}
