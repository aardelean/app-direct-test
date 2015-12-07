package home.app.direct.subscription;

import com.google.common.collect.ImmutableSet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import home.app.direct.subscription.dto.Subscription;
import home.app.direct.vaadin.components.container.ContainerDelegate;
import home.app.direct.vaadin.components.view.CompleteGrid;
import home.app.direct.vaadin.components.view.grid.fields.FieldDefinitionContainer;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class SubscriptionGrid extends CompleteGrid<Subscription> {

    @Autowired
    private ContainerDelegate containerDelegate;

    public SubscriptionGrid(){
        setDescription("Subscriptions Orders detected");
        setAddAllowed(false);
        setDeleteAllowed(false);
        setFilterFields(ImmutableSet.of("user.email", "user.firstName", "user.lastName", "company.name"));
        setHasPagination(true);
        setPageSize(10);
    }
    @Override
    protected Subscription newEntity() {
        return new Subscription();
    }

    @Override
    protected FieldDefinitionContainer getFormFields() {
        return new FieldDefinitionContainer()
                .addFieldDefinition("Creator Email", "user.email")
                .addFieldDefinition("Creator FirstName", "user.firstName")
                .addFieldDefinition("Creator LastName", "user.lastName")
                .addFieldDefinition("Creator Language", "user.language")
                .addFieldDefinition("Creator OpenId", "user.openId")
                .addFieldDefinition("Creator UUID", "user.uuid")
                .addFieldDefinition("Company Country", "company.country")
                .addFieldDefinition("Company Email", "company.email")
                .addFieldDefinition("Company Name", "company.name")
                .addFieldDefinition("Company UUID", "company.uuid")
                .addFieldDefinition("Company Website", "company.website")
                .addFieldDefinition("Order Pricing", "subscriptionOrder.pricingDuration")
                .addFieldDefinition("Order Edition", "subscriptionOrder.editionCode");
    }

    @Override
    protected ContainerDelegate getContainerDelegate() {
        return containerDelegate;
    }
}
