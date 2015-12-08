package home.app.direct.subscription;

import home.app.direct.subscription.dto.Subscription;
import home.app.direct.subscription.dto.SubscriptionOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionServiceTest {
    @Mock
    private SubscriptionRepository repository;

    @InjectMocks
    private SubscriptionService endpoint;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void persistSubscription() throws JAXBException, IOException {
        ArgumentCaptor<Subscription> arg = ArgumentCaptor.forClass(Subscription.class);

        endpoint.create(new ClassPathResource("dummyOrder.xml").getInputStream());
        Mockito.verify(repository).save(arg.capture());
        Subscription subscription = arg.getValue();
        Assert.assertEquals("DummyCreatorFirst", subscription.getUser().getFirstName());
    }

    @Test
    public void changeSubscription() throws JAXBException, IOException {
        ArgumentCaptor<Subscription> arg = ArgumentCaptor.forClass(Subscription.class);
        List<Subscription> subscriptions = new ArrayList<>();
        Subscription subscription = new Subscription();
        subscription.setSubscriptionOrder(new SubscriptionOrder());
        subscriptions.add(subscription);
        Mockito.when(repository.findByIdentifier(Mockito.anyString())).thenReturn(subscriptions);

        endpoint.change(new ClassPathResource("dummyChange.xml").getInputStream());

        Mockito.verify(repository).save(arg.capture());
        subscription = arg.getValue();
        Assert.assertEquals("PREMIUM", subscription.getSubscriptionOrder().getEditionCode());
    }


}
