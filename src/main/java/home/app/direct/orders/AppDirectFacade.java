package home.app.direct.orders;

import home.app.direct.orders.subscription.EventErrorStatus;
import home.app.direct.orders.subscription.ProcessResult;
import home.app.direct.transport.EventType;
import home.app.direct.transport.Result;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Component
public class AppDirectFacade {

    private static Logger logger = LoggerFactory.getLogger(AppDirectFacade.class);
    @Value("${oauth.key}")
    private String OAUTH_KEY;

    @Value("${oauth.secret}")
    private String OAUTH_SECRET;

    public Optional<EventType> fetchEvent(String subscriptionUrl) {
        HttpURLConnection request = null;
        try {
            URL url = new URL(subscriptionUrl);
            OAuthConsumer consumer = new DefaultOAuthConsumer(OAUTH_KEY, OAUTH_SECRET);
            request = (HttpURLConnection) url.openConnection();
            logger.info("opened connection at: "+subscriptionUrl);
            consumer.sign(request);
            logger.info("oauth signed request! "+request.getHeaderFields().keySet());
            request.connect();
            logger.info("successfully connected and got a response!");
            return Optional.of(transform(request.getInputStream()));
        } catch (Exception e) {
            logger.error("error while getting subscription ", e);
            return Optional.empty();
        }finally {
            if(request!=null){
                request.disconnect();
            }
        }
    }

    private EventType transform(InputStream representation) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(EventType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (EventType)unmarshaller.unmarshal(representation);
    }

    public Result buildResponse(ProcessResult processResult){
        Result result = new Result();
        if (processResult.getIdentifier() != null) {
            result.setSuccess(true);
            result.setMessage("subscription operation ended successfully");
            result.setAccountIdentifier(processResult.getIdentifier());
        }else {
            result.setSuccess(false);
            result.setMessage("error on server during operation on subscription");
            if(processResult.getEventStatus()!=null) {
                result.setErrorCode(processResult.getEventStatus().name());
            }else {
                result.setErrorCode(EventErrorStatus.UNKNOWN_ERROR.name());
            }
        }
        return result;
    }
}
