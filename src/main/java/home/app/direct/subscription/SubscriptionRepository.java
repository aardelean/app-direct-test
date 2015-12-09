package home.app.direct.subscription;

import home.app.direct.common.dto.Subscription;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SubscriptionRepository extends PagingAndSortingRepository<Subscription, Long> {
    List<Subscription> findByIdentifier(String identifier);
}
