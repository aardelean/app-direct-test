# app-direct-test
Tehnologies used: 
  - build tool : gradle
  - servlet container: undertow
  - presentation layer: vaadin, html
  - cloud provider: cloud foundry, pivotal
  - database: h2 in memory
  - security: spring security, openid4java
  - code technologies: spring boot, spring rest, jpa, hibernate, lombok, jaxb.

Usage:

Configuration:the port/database connection/etc..  in application.properties.

To build : gradle build. It will result in a fat jar under build/libs/app-direct-1.0.jar (requires gradle instalation)

To start localhost : java -jar build/libs/app-direct-1.0.jar

To deploy on cloud foundry: cf push (requires cloud foundry client and account)

Already deployed at http://app-direct-subscription.cfapps.io

OpenId integration with app direct:
 - add http://app-direct-subscription.cfapps.io/login-open-id?openid_identifier={openid}
 for checking the default view
 - since it is a h2 based application, if the user is not found, it will create a default user for allowing
 view operation on /check

At the url http://app-direct-subscription.cfapps.io/check is the vaadin presentation of all subscriptions or users ordered
since last deploy.

At http://app-direct-subscription.cfapps.io/api/subscriptions can be verified via rest the same orders.
At http://app-direct-subscription.cfapps.io/api/customers can be verified via rest the same customers.
Hal browser enabled at http://app-direct-subscription.cfapps.io/api/

Operations supported so far: 
  - subscription order at url http://app-direct-subscription.cfapps.io/subscription/create?eventurl={eventUrl}
  - subscription change at url http://app-direct-subscription.cfapps.io/subscription/change?eventurl={eventUrl}
  - subscription cancel at url http://app-direct-subscription.cfapps.io/subscription/cancel?eventurl={eventUrl}
  - customer assign at url http://app-direct-subscription.cfapps.io/customer/assign?eventurl={eventUrl}
  - customer unassign at url http://app-direct-subscription.cfapps.io/subscription/unassign?eventurl={eventUrl}

