package home.app.direct.security;

import home.app.direct.orders.UserDetailsImpl;
import home.app.direct.orders.users.UserRepository;
import home.app.direct.orders.users.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class OpenIdServiceImpl implements UserDetailsService, AuthenticationUserDetailsService<OpenIDAuthenticationToken> {
    @Value("${spring.user.name}")
    private String username;
    @Value("${spring.user.password}")
    private String password;

    @Autowired
    private UserRepository userRepository;

    /**
     * This would be useful if we would login via a form. Not used with openId.
     * @param username
     * @return
     * @throws UsernameNotFoundException
     * @throws DataAccessException
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException{
        if(username.equals(username)) {
            return defaultUserDetails();
        }else{
            return null;
        }
    }

    /**
     * Depending on the business logic, the user could be loaded here from database, update, etc...
     * For the test, just compose one user and return. Because here H2 is used, the users are lost
     * between restarts, unpredictable results in this case. For that the default user is loaded.
     * @param token
     * @return
     * @throws UsernameNotFoundException
     */

    @Override
    public UserDetails loadUserDetails(OpenIDAuthenticationToken token) throws UsernameNotFoundException {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByOpenId(token.getIdentityUrl()));
        return userOptional.map(p->userDetails(p)).orElse(defaultUserDetails());
    }

    private UserDetails defaultUserDetails(){
        Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = UserDetailsImpl.builder()
                                            .accountNonExpired(true)
                                            .accountNonLocked(true)
                                            .credentialsNonExpired(true)
                                            .enabled(true)
                                            .authorities(authorities)
                                            .username(username)
                                            .password(password)
                                            .build();
        return userDetails;
    }

    private UserDetails userDetails(User user){
        Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = UserDetailsImpl.builder()
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .authorities(authorities)
                .username(user.getFirstName())
                .password(user.getEmail())
                .build();
        return userDetails;
    }
}
