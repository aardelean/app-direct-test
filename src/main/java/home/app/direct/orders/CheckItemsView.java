package home.app.direct.orders;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import home.app.direct.orders.subscription.view.SubscriptionGrid;
import home.app.direct.orders.users.view.UsersGrid;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@SpringUI(path = "/check")
@Theme(value = "chameleon")
public class CheckItemsView extends UI{

    @Autowired
    private SubscriptionGrid subscriptionGrid;
    @Autowired
    private UsersGrid usersGrid;

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout verticalLayout = new VerticalLayout();
        Panel panel = new Panel();
//        panel.setWidth(975, Unit.PIXELS);
        MenuBar mainMenu = new MenuBar();
        mainMenu.setSizeFull();
        mainMenu.addItem("subscriptions", p-> {
            panel.setContent(subscriptionGrid.start());
        });
        mainMenu.addItem("users", p-> {
            panel.setContent(usersGrid.start());
        });
        verticalLayout.addComponent(mainMenu);
        verticalLayout.addComponent(panel);
        setContent(verticalLayout);
    }

}
