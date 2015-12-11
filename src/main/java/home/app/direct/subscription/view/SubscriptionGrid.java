package home.app.direct.subscription.view;

import com.google.common.collect.ImmutableSet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import home.app.direct.common.SimpleGrid;
import home.app.direct.common.dto.Subscription;
import home.app.direct.subscription.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

@SpringComponent
@UIScope
public class SubscriptionGrid extends SimpleGrid<Subscription> {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public SubscriptionGrid(String title, Class clazz) {
        super(title, clazz);
    }

    @Override
    protected CrudRepository<Subscription, ?> getRepository() {
        return subscriptionRepository;
    }

    @Override
    protected Set<String> getColumnNames() {
        return ImmutableSet.of("Identifier",
                                      "identifier",
                                      "Order Pricing",
                                      "subscriptionOrder.pricingDuration",
                                      "Order Edition",
                                      "subscriptionOrder.editionCode",
                                      "Order Status",
                                      "subscriptionStatus",
                                      "Creator Email",
                                      "user.email",
                                      "Creator FirstName",
                                      "user.firstName",
                                      "Creator LastName",
                                      "user.lastName",
                                      "Creator Language",
                                      "user.language",
                                      "Creator OpenId",
                                      "user.openId",
                                      "Creator UUID",
                                      "user.uuid",
                                      "Company Country",
                                      "company.country",
                                      "Company Email",
                                      "company.email",
                                      "Company Name",
                                      "company.name",
                                      "Company UUID",
                                      "company.uuid",
                                      "Company Website",
                                      "company.website");
    }
}
