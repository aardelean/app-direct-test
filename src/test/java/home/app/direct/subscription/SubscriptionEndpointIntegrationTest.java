package home.app.direct.subscription;

import home.app.direct.Starter;
import home.app.direct.subscription.dto.Subscription;
import home.app.direct.transport.EventType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Starter.class)
@WebAppConfiguration
public class SubscriptionEndpointIntegrationTest {
    @Autowired
    private SubscriptionRepository repository;

    @Autowired
    private SubscriptionFacade endpoint;

    @Test
    public void saveSubscriptionInDB() throws JAXBException, IOException {
        long originalCount = repository.count();
        JAXBContext jaxbContext = JAXBContext.newInstance(EventType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        EventType eventType = (EventType)unmarshaller.unmarshal(new ClassPathResource("dummyOrder.xml").getInputStream());

        endpoint.create(eventType);
        long afterSaveCount = repository.count();
        Assert.assertEquals(originalCount + 1 , afterSaveCount);
        Subscription inserted = repository.findAll(new Sort(Sort.Direction.DESC, "id")).iterator().next();
        Assert.assertEquals("DummyCreatorFirst", inserted.getUser().getFirstName());
    }
}
