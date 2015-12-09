package home.app.direct.users;

import home.app.direct.common.dto.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByOpenId(String openId);
    User findByEmail(String email);
}
