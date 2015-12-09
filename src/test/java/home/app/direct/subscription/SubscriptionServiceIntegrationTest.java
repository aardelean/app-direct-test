package home.app.direct.subscription;

import home.app.direct.Starter;
import home.app.direct.common.dto.Subscription;
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
import java.io.InputStream;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Starter.class)
@WebAppConfiguration
public class SubscriptionServiceIntegrationTest {
    @Autowired
    private SubscriptionRepository repository;

    @Autowired
    private SubscriptionService classUnderTest;

    @Test
    public void saveSubscriptionInDB() throws JAXBException, IOException {
        long originalCount = repository.count();
        classUnderTest.create(Optional.of(transform(new ClassPathResource("dummyOrder.xml").getInputStream())));
        long afterSaveCount = repository.count();
        Assert.assertEquals(originalCount + 1 , afterSaveCount);
        Subscription inserted = repository.findAll(new Sort(Sort.Direction.DESC, "id")).iterator().next();
        Assert.assertEquals("DummyCreatorFirst", inserted.getUser().getFirstName());
    }

    public static EventType transform(InputStream representation) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(EventType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (EventType)unmarshaller.unmarshal(representation);
    }

}
