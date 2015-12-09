package home.app.direct.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(exclude={"id", "customers"})
public class SubscriptionOrder {
    @Id
    @GeneratedValue
    private Long id;
    private String editionCode;
    private String pricingDuration;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> customers;
}
