package home.app.direct.orders.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SubscriptionController {

    @Autowired
    private SubscriptionRepository repository;

    @RequestMapping(path = "/subscriptions")
    public String subscriptions(Model model){
        model.addAttribute("subscriptions", repository.findAll());
        return "check";
    }
}
