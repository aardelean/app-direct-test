package home.app.direct.subscription.dto;

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
}
