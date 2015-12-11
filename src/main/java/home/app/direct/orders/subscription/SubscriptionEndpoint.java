package home.app.direct.orders.subscription;

import home.app.direct.orders.AppDirectFacade;
import home.app.direct.transport.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBException;

@RestController
@RequestMapping("/subscription")
public class SubscriptionEndpoint {
    private static Logger logger = LoggerFactory.getLogger(SubscriptionEndpoint.class);

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private AppDirectFacade eventFacade;

    @RequestMapping(path = "/create", method = RequestMethod.GET, produces = "application/xml")
    public Result createSubscription(@RequestParam(name = "eventurl")String subscriptionUrl) throws JAXBException {
        logger.info("subscription added, checkout : "+subscriptionUrl);
        ProcessResult processResult = subscriptionService.create(eventFacade.fetchEvent(subscriptionUrl));
        return eventFacade.buildResponse(processResult);
    }

    @RequestMapping(path = "/change", method = RequestMethod.GET, produces = "application/xml")
    public Result changeSubscription(@RequestParam(name = "eventurl")String subscriptionUrl) throws JAXBException {
        logger.info("subscription changed, checkout : "+subscriptionUrl);
        ProcessResult processResult = subscriptionService.change(eventFacade.fetchEvent(subscriptionUrl));
        return eventFacade.buildResponse(processResult);
    }

    @RequestMapping(path = "/cancel", method = RequestMethod.GET, produces = "application/xml")
    public Result cancelSubscription(@RequestParam(name = "eventurl")String subscriptionUrl) throws JAXBException {
        logger.info("subscription cancelled, checkout : "+subscriptionUrl);
        ProcessResult processResult = subscriptionService.cancel(eventFacade.fetchEvent(subscriptionUrl));
        return eventFacade.buildResponse(processResult);
    }

    @RequestMapping(path = "/create/test", method = RequestMethod.GET, produces = "application/json")
    public Result createTestSubscription() throws JAXBException {
        Result result = createSubscription("https://www.appdirect.com/api/integration/v1/events/dummyOrder");
        return result;
    }

    @RequestMapping(path = "/change/test", method = RequestMethod.GET, produces = "application/json")
    public Result changeTestSubscription() throws JAXBException {
        return changeSubscription("https://www.appdirect.com/api/integration/v1/events/dummyChange");
    }

    @RequestMapping(path = "/cancel/test", method = RequestMethod.GET, produces = "application/json")
    public Result cancelTestSubscription() throws JAXBException {
        return cancelSubscription("https://www.appdirect.com/api/integration/v1/events/dummyCancel");
    }
}
