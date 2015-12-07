package home.app.direct.subscription;

import home.app.direct.subscription.dto.Subscription;
import home.app.direct.transport.Result;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBException;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/subscription")
public class SubscriptionEndpoint {

    private static Logger logger = LoggerFactory.getLogger(SubscriptionEndpoint.class);

    @Value("${oauth.key}")
    private String OAUTH_KEY;

    @Value("${oauth.secret}")
    private String OAUTH_SECRET;

    @Autowired
    private SubscriptionFacade subscriptionFacade;


    @RequestMapping(path = "/create", method = RequestMethod.GET, produces = "application/xml")
    public Result createSubscription(@RequestParam(name = "eventurl")String subscriptionUrl) throws JAXBException {
        logger.info("subscription added, checkout : "+subscriptionUrl);
        OAuthConsumer consumer = new DefaultOAuthConsumer(OAUTH_KEY, OAUTH_SECRET);
        URL url = null;
        try {
            url = new URL(subscriptionUrl);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            logger.info("opened connection at: "+subscriptionUrl);
            consumer.sign(request);
            logger.info("oauth signed request! "+request.getHeaderFields().keySet());
            request.connect();
            logger.info("successfully connected and got a response!");
            Subscription subscription = subscriptionFacade.create(request.getInputStream());
            //TODO: return a new identifier, not the id to match with the subscription at appdirect.
            return buildResult(true, "added a new subscription successfully", subscription.getId().toString());
        } catch (Exception e) {
            logger.error("error while getting subscription ", e);
            return buildResult(false,e.getMessage(),null);
        }
    }
    private Result buildResult(boolean success, String message, String accountIdentifier){
        Result result = new Result();
        result.setSuccess(success);
        result.setAccountIdentifier(accountIdentifier);
        result.setMessage(message);
        return result;
    }
}
