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
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;


@Component
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository repository;

    public Subscription create(InputStream inputStream) throws JAXBException {
        EventType eventType = transform(inputStream);
        Subscription subscription = new Subscription();
        subscription.setIdentifier(UUID.randomUUID().toString());
        subscription.setUser(buildCreator(eventType));
        subscription.setCompany(buildCompany(eventType));
        subscription.setSubscriptionOrder(buildOrder(eventType));
        repository.save(subscription);
        return subscription;

    }

    public Subscription change(InputStream inputStream) throws JAXBException {
        EventType eventType = transform(inputStream);
        String subscriptionIdentifier = eventType.getPayload().getAccount().getAccountIdentifier();
        List<Subscription> subscriptions = repository.findByIdentifier(subscriptionIdentifier);
        assert subscriptions.size() == 1;
        Subscription subscription = subscriptions.iterator().next();
        subscription.getSubscriptionOrder().setEditionCode(eventType.getPayload().getOrder().getEditionCode());
        subscription.getSubscriptionOrder().setPricingDuration(eventType.getPayload().getOrder().getPricingDuration());
        subscription = repository.save(subscription);
        return subscription;
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

    public static EventType transform(InputStream representation) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(EventType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (EventType)unmarshaller.unmarshal(representation);
    }
}
