package home.app.direct.subscription;

import home.app.direct.subscription.dto.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.websocket.server.PathParam;

/**
 * Created by alex on 9/19/2015.
 */
@Controller
@RequestMapping("/view")
public class ThymeleafController {

    @Autowired
    private SubscriptionRepository repository;


    @RequestMapping(value = "/{filename}", method = RequestMethod.GET)
    public String getThymeleafTemplate(@PathParam("filename")String filename, Model model){
        Iterable<Subscription> subscriptions = repository.findAll();
        model.addAttribute("subscriptions", subscriptions);
        return filename;
    }


}
