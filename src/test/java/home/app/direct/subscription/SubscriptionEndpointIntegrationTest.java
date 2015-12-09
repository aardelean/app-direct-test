package home.app.direct.subscription;

import home.app.direct.Starter;
import home.app.direct.common.dto.Subscription;
import home.app.direct.transport.Result;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Starter.class)
@WebAppConfiguration
public class SubscriptionEndpointIntegrationTest {
    @Autowired
    private SubscriptionEndpoint classUnderTest;

    @Autowired
    private SubscriptionRepository repository;

    @Test
    public void test() throws JAXBException, IOException {
        long originalCount = repository.count();

        String testSubscriptionUrl = "https://www.appdirect.com/api/integration/v1/events/dummyOrder";
        Result result = classUnderTest.createSubscription(testSubscriptionUrl);

        Assert.assertTrue( "could not create subscription in integration test, check internet connection! ", result.isSuccess());
        long afterSaveCount = repository.count();
        Assert.assertEquals(originalCount + 1 , afterSaveCount);
        Subscription inserted = repository.findByIdentifier(result.getAccountIdentifier()).get(0);
        Assert.assertEquals("DummyCreatorFirst", inserted.getUser().getFirstName());
    }
}
