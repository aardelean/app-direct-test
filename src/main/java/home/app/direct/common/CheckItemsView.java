package home.app.direct.common;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import home.app.direct.subscription.view.SubscriptionGrid;
import home.app.direct.users.view.UsersGrid;
import home.app.direct.vaadin.components.view.CompleteGrid;
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
        panel.setWidth(975, Unit.PIXELS);
        MenuBar mainMenu = new MenuBar();
        addMenuItem("subscriptions", mainMenu, panel, subscriptionGrid);
        addMenuItem("users", mainMenu, panel, usersGrid);
        verticalLayout.addComponent(mainMenu);
        verticalLayout.addComponent(panel);
        setContent(verticalLayout);
    }

    private void addMenuItem(String label, MenuBar mainMenu,
                             final Panel parent,final CompleteGrid grid){
        mainMenu.addItem(label, p-> {
            parent.setContent(grid.start(false, "me"));
        });
    }
}
