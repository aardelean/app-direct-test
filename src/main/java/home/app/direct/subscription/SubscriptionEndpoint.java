package home.app.direct.subscription;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/appdirect")
public class SubscriptionEndpoint {

    @RequestMapping(method = RequestMethod.GET)
    public String createSubscription(@RequestParam(name = "eventurl")String subscriptionUrl){
        OAuthConsumer consumer = new DefaultOAuthConsumer("Dummy", "secret");
        URL url = null;
        try {
            url = new URL("https://www.appdirect.com/AppDirect/rest/api/events/dummyChange");

        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        consumer.sign(request);
        request.connect();
        } catch (OAuthExpectationFailedException | IOException |
                OAuthMessageSignerException | OAuthCommunicationException e) {
            e.printStackTrace();
        }
        return "ok!";
    }
}
