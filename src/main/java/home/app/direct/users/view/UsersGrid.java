package home.app.direct.users.view;

import com.google.common.collect.ImmutableSet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import home.app.direct.common.SimpleGrid;
import home.app.direct.common.dto.User;
import home.app.direct.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

@SpringComponent
@UIScope
public class UsersGrid extends SimpleGrid<User>{

    @Autowired
    private UserRepository repository;

    public UsersGrid(String title, Class clazz) {
        super(title, clazz);
    }

    @Override
    protected CrudRepository<User, ?> getRepository() {
        return repository;
    }

    @Override
    protected Set<String> getColumnNames() {
        return ImmutableSet.of("firstName", "lastName", "email");
    }
}
