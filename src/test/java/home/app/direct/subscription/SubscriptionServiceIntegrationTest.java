package home.app.direct.subscription;

import home.app.direct.Starter;
import home.app.direct.subscription.dto.Subscription;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.xml.bind.JAXBException;
import java.io.IOException;

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

        classUnderTest.create(new ClassPathResource("dummyOrder.xml").getInputStream());
        long afterSaveCount = repository.count();
        Assert.assertEquals(originalCount + 1 , afterSaveCount);
        Subscription inserted = repository.findAll(new Sort(Sort.Direction.DESC, "id")).iterator().next();
        Assert.assertEquals("DummyCreatorFirst", inserted.getUser().getFirstName());
    }

}
