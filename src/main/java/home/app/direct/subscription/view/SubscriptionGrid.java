package home.app.direct.subscription.view;

import com.google.common.collect.ImmutableSet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import home.app.direct.common.dto.Subscription;
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
        setInlineEditAllowed(false);
        setFilterFields(ImmutableSet.of("identifier"));
        setDeleteAllowed(false);
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
                .addFieldDefinition("Identifier", "identifier")
                .addFieldDefinition("Order Pricing", "subscriptionOrder.pricingDuration")
                .addFieldDefinition("Order Edition", "subscriptionOrder.editionCode")
                .addFieldDefinition("Order Status", "subscriptionStatus")
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
                .addFieldDefinition("Company Website", "company.website");
    }

    @Override
    protected ContainerDelegate getContainerDelegate() {
        return containerDelegate;
    }
}
