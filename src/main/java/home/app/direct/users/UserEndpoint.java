package home.app.direct.users;

import home.app.direct.common.AppDirectFacade;
import home.app.direct.subscription.SubscriptionEndpoint;
import home.app.direct.common.dto.ProcessResult;
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
@RequestMapping("/customer")
public class UserEndpoint {
    private static Logger logger = LoggerFactory.getLogger(SubscriptionEndpoint.class);

    @Autowired
    private AppDirectFacade eventFacade;

    @Autowired
    private UserService customerService;

    @RequestMapping(path = "/assign", method = RequestMethod.GET, produces = "application/xml")
    public Result assignSubscription(@RequestParam(name = "eventurl")String url) throws JAXBException {
        logger.info("user assigned, checkout : " + url);
        ProcessResult processResult = customerService.assignCustomer(eventFacade.fetchEvent(url));
        return eventFacade.buildResponse(processResult);
    }

    @RequestMapping(path = "/unassign", method = RequestMethod.GET, produces = "application/xml")
    public Result unassignSubscription(@RequestParam(name = "eventurl")String url) throws JAXBException {
        logger.info("user assigned, checkout : " + url);
        ProcessResult processResult = customerService.unassignCustomer(eventFacade.fetchEvent(url));
        return eventFacade.buildResponse(processResult);
    }

    @RequestMapping(path = "/assign/test", method = RequestMethod.GET, produces = "application/xml")
    public Result assignTestSubscription() throws JAXBException {
        ProcessResult processResult = customerService.assignCustomer(
                eventFacade.fetchEvent("https://www.appdirect.com/api/integration/v1/events/dummyAssign")
        );
        return eventFacade.buildResponse(processResult);
    }

    @RequestMapping(path = "/unassign/test", method = RequestMethod.GET, produces = "application/xml")
    public Result unassignTestSubscription() throws JAXBException {
        ProcessResult processResult = customerService.unassignCustomer(
                eventFacade.fetchEvent("https://www.appdirect.com/api/integration/v1/events/dummyUnassign")
        );
        return eventFacade.buildResponse(processResult);
    }

}

