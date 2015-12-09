package home.app.direct.users;

import home.app.direct.common.Validator;
import home.app.direct.common.dto.EventErrorStatus;
import home.app.direct.common.dto.ProcessResult;
import home.app.direct.common.dto.Subscription;
import home.app.direct.common.dto.User;
import home.app.direct.subscription.SubscriptionRepository;
import home.app.direct.subscription.SubscriptionService;
import home.app.direct.transport.EventType;
import home.app.direct.transport.PayloadType;
import home.app.direct.transport.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private Validator validator;

    public ProcessResult assignCustomer(Optional<EventType> eventTypeOptional){
        Optional<ProcessResult> resultOptional = Optional.empty();
        try {
            resultOptional = validator.checkEventType(eventTypeOptional);
            if(resultOptional.isPresent()) return resultOptional.get();
            PayloadType payload = eventTypeOptional.get().getPayload();

            Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(payload.getUser().getEmail()));
            if(!userOptional.isPresent()) {
                userOptional = Optional.of(convertUser(payload.getUser()));
                userRepository.save(userOptional.get());
                userOptional.get().setSubscriptionOrders(new ArrayList<>());
            }else{
                userRepository.save(userOptional.get());
            }
            User user = userOptional.get();

            Optional<Subscription> subscriptionOptional = subscriptionService.getSubscription(eventTypeOptional);
            resultOptional = validator.checkSubscription(subscriptionOptional);
            if(resultOptional.isPresent()) return resultOptional.get();
            Subscription subscription = subscriptionOptional.get();

            List<User> users = subscription.getSubscriptionOrder().getCustomers();
            resultOptional = validator.checkAlreadyAssigned(user, users);
            if(resultOptional.isPresent()) return resultOptional.get();

            user.getSubscriptionOrders().add(subscription.getSubscriptionOrder());
            users.add(user);
            resultOptional = Optional.of(new ProcessResult(subscription.getIdentifier()));
            subscriptionRepository.save(subscription);
            userRepository.save(user);
        }catch (Exception e){
            logger.error("error while unassigning user: ", e);
        }finally {
            return resultOptional.orElse(new ProcessResult(EventErrorStatus.UNKNOWN_ERROR));
        }
    }

    public ProcessResult unassignCustomer(Optional<EventType> eventTypeOptional){
        Optional<ProcessResult> resultOptional = Optional.empty();
        try {
            resultOptional = validator.checkEventType(eventTypeOptional);
            if(resultOptional.isPresent()) return resultOptional.get();
            PayloadType payload = eventTypeOptional.get().getPayload();

            Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(payload.getUser().getEmail()));
            if(!userOptional.isPresent()) return new ProcessResult(EventErrorStatus.ACCOUNT_NOT_FOUND);
            User user = userOptional.get();

            Optional<Subscription> subscriptionOptional = subscriptionService.getSubscription(eventTypeOptional);
            resultOptional = validator.checkSubscription(subscriptionOptional);
            if(resultOptional.isPresent()) return resultOptional.get();
            Subscription subscription = subscriptionOptional.get();

            List<User> users = subscription.getSubscriptionOrder().getCustomers();
            resultOptional = validator.checkUnassignedUser(user, users);
            if(resultOptional.isPresent()) return resultOptional.get();

            user.getSubscriptionOrders().remove(subscription.getSubscriptionOrder());
            subscription.getSubscriptionOrder().getCustomers().remove(user);
            resultOptional = Optional.of(new ProcessResult(subscription.getIdentifier()));
            subscriptionRepository.save(subscription);
            userRepository.delete(user);
        }catch (Exception e){
            logger.error("error while unassigning user: ", e);
        }finally {
            return resultOptional.orElse(new ProcessResult(EventErrorStatus.UNKNOWN_ERROR));
        }
    }

    public User convertUser(UserType transport) {
        User creator = new User();
        creator.setEmail(transport.getEmail());
        creator.setFirstName(transport.getFirstName());
        creator.setLanguage(transport.getLanguage());
        creator.setLastName(transport.getLastName());
        creator.setOpenId(transport.getOpenId());
        creator.setUuid(transport.getUuid());
        return creator;
    }
}
