package home.app.direct.orders.users.entity;

import home.app.direct.orders.subscription.entity.SubscriptionOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(exclude={"id", "subscriptionOrders"})
public class User {
    @Id
    @GeneratedValue
    private Long id;
    protected String email;
    protected String firstName;
    protected String language;
    protected String lastName;
    protected String openId;
    protected String uuid;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<SubscriptionOrder> subscriptionOrders;



}
