package home.app.direct.orders.subscription;

import home.app.direct.orders.Validator;
import home.app.direct.orders.subscription.entity.Subscription;
import home.app.direct.orders.subscription.entity.SubscriptionOrder;
import home.app.direct.orders.users.entity.User;
import home.app.direct.orders.users.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubscriptionServiceTest {
    @Mock
    private SubscriptionRepository repository;

    @Mock
    private Validator validator;

    @Mock
    private UserService userService;

    @InjectMocks
    private SubscriptionService classUnderTest;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(validator.checkAlreadyAssigned(Mockito.any(User.class), Mockito.anyList())).thenReturn(Optional.empty());
        Mockito.when(validator.checkEventType(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(validator.checkSubscription(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(userService.convertUser(Mockito.any())).thenReturn(new User());
    }

    @Test
    public void persistSubscription() throws JAXBException, IOException {
        ArgumentCaptor<Subscription> arg = ArgumentCaptor.forClass(Subscription.class);

        classUnderTest.create(Optional.of(
                SubscriptionServiceIntegrationTest.transform(
                        new ClassPathResource("dummyOrder.xml").getInputStream()
                )));
        Mockito.verify(repository).save(arg.capture());
        Subscription subscription = arg.getValue();
        Assert.assertEquals("BASIC", subscription.getSubscriptionOrder().getEditionCode());
    }

    @Test
    public void changeSubscription() throws JAXBException, IOException {
        ArgumentCaptor<Subscription> arg = ArgumentCaptor.forClass(Subscription.class);
        List<Subscription> subscriptions = new ArrayList<>();
        Subscription subscription = new Subscription();
        subscription.setSubscriptionOrder(new SubscriptionOrder());
        subscriptions.add(subscription);
        Mockito.when(repository.findByIdentifier(Mockito.anyString())).thenReturn(subscriptions);

        classUnderTest.change(Optional.of(
                SubscriptionServiceIntegrationTest.transform(
                        new ClassPathResource("dummyChange.xml").getInputStream()
                )));

        Mockito.verify(repository).save(arg.capture());
        subscription = arg.getValue();
        Assert.assertEquals("PREMIUM", subscription.getSubscriptionOrder().getEditionCode());
    }

    @Test
    public void cancelSubscription() throws JAXBException, IOException {
        ArgumentCaptor<Subscription> arg = ArgumentCaptor.forClass(Subscription.class);
        List<Subscription> subscriptions = new ArrayList<>();
        Subscription subscription = new Subscription();
        subscriptions.add(subscription);
        Mockito.when(repository.findByIdentifier(Mockito.anyString())).thenReturn(subscriptions);

        classUnderTest.cancel(Optional.of(
                SubscriptionServiceIntegrationTest.transform(
                        new ClassPathResource("dummyCancel.xml").getInputStream()
                )));

        Mockito.verify(repository).save(arg.capture());
        subscription = arg.getValue();
        Assert.assertEquals("CANCELLED", subscription.getSubscriptionStatus().name());
    }
}
