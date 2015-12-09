package home.app.direct.users.view;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import home.app.direct.common.dto.User;
import home.app.direct.vaadin.components.container.ContainerDelegate;
import home.app.direct.vaadin.components.view.CompleteGrid;
import home.app.direct.vaadin.components.view.grid.fields.FieldDefinitionContainer;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class UsersGrid extends CompleteGrid<User> {

    @Autowired
    private ContainerDelegate containerDelegate;

    public UsersGrid(){
        setDescription("Subscriptions Orders detected");
        setAddAllowed(false);
        setInlineEditAllowed(false);
        setDeleteAllowed(false);
        setHasPagination(true);
        setPageSize(10);
    }
    @Override
    protected User newEntity() {
        return new User();
    }

    @Override
    protected FieldDefinitionContainer getFormFields() {
        return new FieldDefinitionContainer()
                .addFieldDefinition("First Name", "firstName")
                .addFieldDefinition("Last Name", "lastName")
                .addFieldDefinition("Email", "email");
    }

    @Override
    protected ContainerDelegate getContainerDelegate() {
        return containerDelegate;
    }
}
