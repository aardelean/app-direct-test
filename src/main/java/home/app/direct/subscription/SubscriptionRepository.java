package home.app.direct.subscription;

import home.app.direct.subscription.dto.Subscription;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SubscriptionRepository extends PagingAndSortingRepository<Subscription, Long> {
}
