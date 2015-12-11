package home.app.direct.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException
    {
        System.out.println("loginin in");
        UserDetails user = new User(username, "password", true, true, true, true, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        return user;
    }
}
