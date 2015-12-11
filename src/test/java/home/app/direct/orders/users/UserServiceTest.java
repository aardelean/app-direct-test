package home.app.direct.orders.users;

import home.app.direct.orders.Validator;
import home.app.direct.orders.subscription.entity.Subscription;
import home.app.direct.orders.subscription.entity.SubscriptionOrder;
import home.app.direct.orders.subscription.SubscriptionRepository;
import home.app.direct.orders.subscription.SubscriptionService;
import home.app.direct.orders.subscription.SubscriptionServiceIntegrationTest;
import home.app.direct.orders.users.entity.User;
import home.app.direct.transport.EventType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private UserService classUnderTest;

    @Mock
    private SubscriptionService subscriptionService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(validator.checkAlreadyAssigned(Mockito.any(User.class), Mockito.anyList())).thenReturn(Optional.empty());
        Mockito.when(validator.checkEventType(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(validator.checkUnassignedUser(Mockito.any(User.class), Mockito.anyList())).thenReturn(Optional.empty());
        Mockito.when(validator.checkSubscription(Mockito.any())).thenReturn(Optional.empty());
    }

    @Test
    public void assignUser() throws JAXBException, IOException {
        Optional<EventType> eventTypeOptional = Optional.of(
                SubscriptionServiceIntegrationTest.transform(
                        new ClassPathResource("dummyAssign.xml").getInputStream()
                ));

        Subscription subscription = buildTestSubscription();
        User user = new User();
        user.setSubscriptionOrders(new ArrayList<>());
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(subscriptionService.getSubscription(Mockito.any())).thenReturn(Optional.of(subscription));

        classUnderTest.assignCustomer(eventTypeOptional);
        userRepository.save(user);
        subscriptionRepository.save(subscription);
        Assert.assertEquals(user, subscription.getSubscriptionOrder().getCustomers().get(0));
    }

    @Test
    public void unassignUser() throws JAXBException, IOException {
        Optional<EventType> eventTypeOptional = Optional.of(
                SubscriptionServiceIntegrationTest.transform(
                        new ClassPathResource("dummyUnassign.xml").getInputStream()
                ));
        Subscription subscription = buildTestSubscription();
        User user = new User();
        user.setSubscriptionOrders(new ArrayList<>());
        subscription.getSubscriptionOrder().getCustomers().add(user);
        user.getSubscriptionOrders().add(subscription.getSubscriptionOrder());
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(subscriptionService.getSubscription(Mockito.any())).thenReturn(Optional.of(subscription));

        classUnderTest.unassignCustomer(eventTypeOptional);
        userRepository.delete(user);
        subscriptionRepository.save(subscription);
        Assert.assertTrue(subscription.getSubscriptionOrder().getCustomers().isEmpty());
        Assert.assertTrue(user.getSubscriptionOrders().isEmpty());
    }

    private Subscription buildTestSubscription() {
        Subscription subscription = new Subscription();
        subscription.setSubscriptionOrder(new SubscriptionOrder());
        subscription.getSubscriptionOrder().setCustomers(new ArrayList<>());
        subscription.setIdentifier("dummy-account");
        return subscription;
    }

}
