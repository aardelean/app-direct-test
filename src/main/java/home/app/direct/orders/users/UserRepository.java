package home.app.direct.orders.users;

import home.app.direct.orders.users.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByOpenId(String openId);
    User findByEmail(String email);
}
