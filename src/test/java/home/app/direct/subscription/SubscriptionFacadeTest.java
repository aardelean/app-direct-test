package home.app.direct.subscription;

import home.app.direct.subscription.dto.Subscription;
import home.app.direct.transport.EventType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;

public class SubscriptionFacadeTest {
    @Mock
    private SubscriptionRepository repository;

    @InjectMocks
    private SubscriptionFacade endpoint;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveSubscription() throws JAXBException, IOException {
        ArgumentCaptor<Subscription> arg = ArgumentCaptor.forClass(Subscription.class);
        JAXBContext jaxbContext = JAXBContext.newInstance(EventType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        EventType eventType = (EventType)unmarshaller.unmarshal(new ClassPathResource("dummyOrder.xml").getInputStream());

        endpoint.create(eventType);
        Mockito.verify(repository).save(arg.capture());
        Subscription subscription = arg.getValue();
        Assert.assertEquals("DummyCreatorFirst", subscription.getUser().getFirstName());
    }
}
