package home.app.direct.subscription;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@SpringUI(path = "/check")
@Theme(value = "chameleon")
public class SubscriptionView extends UI{

    @Autowired
    private SubscriptionGrid grid;

    @Override
    protected void init(VaadinRequest request) {
        setContent(grid.start(false, "anonym"));
    }
}
