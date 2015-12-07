package home.app.direct.subscription;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import home.app.direct.subscription.dto.Subscription;
import home.app.direct.vaadin.components.view.grid.SimpleGrid;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@SpringUI(path = "/check")
@Theme(value = "chameleon")
public class SubscriptionView extends UI{

    @Autowired
    private SimpleGrid<Subscription> grid;


    @Override
    protected void init(VaadinRequest request) {
        setContent(grid);
    }
}
