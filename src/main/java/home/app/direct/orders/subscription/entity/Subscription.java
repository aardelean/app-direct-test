package home.app.direct.orders.subscription.entity;

import home.app.direct.orders.users.entity.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Subscription {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    private Company company;

    @ManyToOne(cascade = CascadeType.ALL)
    private SubscriptionOrder subscriptionOrder;

    private String identifier;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

}
