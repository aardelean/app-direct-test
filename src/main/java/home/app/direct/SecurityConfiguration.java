package home.app.direct;

import home.app.direct.security.OpenIdServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                //allow some rest operations
            .authorizeRequests().antMatchers("/subscription", "/customer", "/api/subscriptions")
                //user-friendly graph is not allowed non authenticated
            .permitAll().antMatchers("/check").authenticated()
                //openid configuration
            .and().openidLogin().loginProcessingUrl("/login-open-id")
                .attributeExchange("http://schema.openid.net/contact/email")
                .and().authenticationUserDetailsService(getUserDetailsService())
                .loginPage("/open-id-form")
                //default form to login
            .defaultSuccessUrl("/check")
            .and()
            .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
            .permitAll();
    }

    @Bean
    public AuthenticationUserDetailsService<org.springframework.security.openid.OpenIDAuthenticationToken> getUserDetailsService() {
        return new OpenIdServiceImpl();
    }
}