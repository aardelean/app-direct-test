package home.app.direct.subscription;

import home.app.direct.subscription.dto.Company;
import home.app.direct.subscription.dto.Subscription;
import home.app.direct.subscription.dto.SubscriptionOrder;
import home.app.direct.subscription.dto.User;
import home.app.direct.transport.CompanyType;
import home.app.direct.transport.CreatorType;
import home.app.direct.transport.EventType;
import home.app.direct.transport.OrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * handles subscriptions based on an unmarshalled eventType.
 * Can also be called directly via rest (mostly as test feature,
 * should not be released in production like that).
 */
@RestController
@RequestMapping("/subscription")
public class SubscriptionFacade {
    @Autowired
    private SubscriptionRepository repository;

    @RequestMapping(method = RequestMethod.POST)
    public void create(EventType eventType){
        Subscription subscription = new Subscription();
        subscription.setUser(buildCreator(eventType));
        subscription.setCompany(buildCompany(eventType));
        subscription.setSubscriptionOrder(buildOrder(eventType));
        repository.save(subscription);
    }

    private SubscriptionOrder buildOrder(EventType eventType) {
        SubscriptionOrder subscriptionOrder = new SubscriptionOrder();
        OrderType transport = eventType.getPayload().getOrder();
        subscriptionOrder.setEditionCode(transport.getEditionCode());
        subscriptionOrder.setPricingDuration(transport.getPricingDuration());
        return subscriptionOrder;
    }

    private Company buildCompany(EventType eventType) {
        Company company = new Company();
        CompanyType transport = eventType.getPayload().getCompany();
        company.setUuid(transport.getUuid());
        company.setEmail(transport.getEmail());
        company.setCountry(transport.getCountry());
        company.setName(transport.getName());
        company.setPhoneNumber(transport.getPhoneNumber());
        company.setWebsite(transport.getWebsite());
        return company;
    }

    private User buildCreator(EventType eventType) {
        User creator = new User();
        CreatorType transport = eventType.getCreator();
        creator.setEmail(transport.getEmail());
        creator.setFirstName(transport.getFirstName());
        creator.setLanguage(transport.getLanguage());
        creator.setLastName(transport.getLastName());
        creator.setOpenId(transport.getOpenId());
        creator.setUuid(transport.getUuid());
        return creator;
    }
}
