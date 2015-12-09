package home.app.direct.subscription;

import home.app.direct.common.Validator;
import home.app.direct.users.UserService;
import home.app.direct.common.dto.*;
import home.app.direct.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.Optional;


@Component
public class SubscriptionService {
    private static Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    @Autowired
    private SubscriptionRepository repository;

    @Autowired
    private UserService customerService;

    @Autowired
    private Validator validator;

    public ProcessResult create(Optional<EventType> eventTypeOptional) throws JAXBException {
        Optional<ProcessResult> resultOptional = Optional.empty();
        try {
            resultOptional = validator.checkEventType(eventTypeOptional);
            if (resultOptional.isPresent()) return resultOptional.get();

            EventType eventType = eventTypeOptional.get();
            Subscription subscription = buildSubscription(eventType);
            repository.save(subscription);
            resultOptional = Optional.of(new ProcessResult(subscription.getIdentifier()));
        }catch (Exception e){
            logger.error("error while saving order: ", e);
        }finally {
            return resultOptional.orElse(new ProcessResult(EventErrorStatus.UNKNOWN_ERROR));
        }
    }

    public ProcessResult change(Optional<EventType> eventTypeOptional) throws JAXBException {
        Optional<ProcessResult> resultOptional = Optional.empty();
        try {
            resultOptional = validator.checkEventType(eventTypeOptional);
            if (resultOptional.isPresent()) return resultOptional.get();

            Optional<Subscription> subscriptionOptional = getSubscription(eventTypeOptional);
            resultOptional = validator.checkSubscription(subscriptionOptional);
            if(resultOptional.isPresent()) return resultOptional.get();

            Subscription subscription = subscriptionOptional.get();
            OrderType order = eventTypeOptional.get().getPayload().getOrder();
            subscription.getSubscriptionOrder().setEditionCode(order.getEditionCode());
            subscription.getSubscriptionOrder().setPricingDuration(order.getPricingDuration());
            repository.save(subscription);
            resultOptional = Optional.of(new ProcessResult(subscription.getIdentifier()));
        }catch (Exception e){
            logger.error("error while changing order: ", e);
        }finally {
            return resultOptional.orElse(new ProcessResult(EventErrorStatus.UNKNOWN_ERROR));
        }
    }

    public ProcessResult cancel(Optional<EventType> eventTypeOptional) throws JAXBException {
        Optional<ProcessResult> resultOptional = Optional.empty();
        try {
            resultOptional = validator.checkEventType(eventTypeOptional);
            if (resultOptional.isPresent()) return resultOptional.get();

            Optional<Subscription> subscriptionOptional = getSubscription(eventTypeOptional);
            resultOptional = validator.checkSubscription(subscriptionOptional);
            if(resultOptional.isPresent()) return resultOptional.get();

            Subscription subscription = subscriptionOptional.get();
            subscription.setSubscriptionStatus(SubscriptionStatus.CANCELLED);
            repository.save(subscription);
            resultOptional = Optional.of(new ProcessResult(subscription.getIdentifier()));
        }catch (Exception e){
            logger.error("error while canceling order: ", e);
        }finally {
            return resultOptional.orElse(new ProcessResult(EventErrorStatus.UNKNOWN_ERROR));
        }
    }

    private Subscription buildSubscription(EventType eventType) {
        Subscription subscription = new Subscription();
//        subscription.setIdentifier(UUID.randomUUID().toString());
        subscription.setIdentifier("dummy-account");
        subscription.setUser(customerService.convertUser(eventType.getCreator()));
        subscription.setCompany(buildCompany(eventType));
        subscription.setSubscriptionOrder(buildOrder(eventType));
        subscription.setSubscriptionStatus(SubscriptionStatus.VALID);
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

    public Optional<Subscription> getSubscription(Optional<EventType>eventType){
        String subscriptionIdentifier = eventType.get().getPayload().getAccount().getAccountIdentifier();
        List<Subscription> subscriptions = repository.findByIdentifier(subscriptionIdentifier);
        if (subscriptions.size() == 1){
            return Optional.of(subscriptions.get(0));
        }
        return Optional.empty();
    }
}
