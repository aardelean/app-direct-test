package home.app.direct.subscription.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class SubscriptionOrder {
    @Id
    @GeneratedValue
    private Long id;
    protected String editionCode;
    protected String pricingDuration;
}
