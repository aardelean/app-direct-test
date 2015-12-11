package home.app.direct.orders.subscription;

import home.app.direct.orders.subscription.entity.Subscription;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SubscriptionRepository extends PagingAndSortingRepository<Subscription, Long> {
    List<Subscription> findByIdentifier(String identifier);
}
