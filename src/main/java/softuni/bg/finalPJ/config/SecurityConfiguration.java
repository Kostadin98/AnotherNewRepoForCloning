package softuni.bg.finalPJ.config;

import softuni.bg.finalPJ.repositories.UserRepository;
import softuni.bg.finalPJ.service.ApplicationUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //defines which pages will be authorized

        http.authorizeHttpRequests(
                        // Define which urls are visible by users
                auth -> auth
                        // All static resources which are situated in js/css/images are available for anyone
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // Allow anyone to see home page, register page, login page
                        .requestMatchers("/", "/users/login", "/users/register",
                                "/about", "/users/login-error", "/contact", "/profile/**", "/search"
                        , "/profile/*/gallery", "/profile/*/contactForm", "/profile/*/submitContactForm").permitAll()
                        //all other requests are authenticated
                        .anyRequest().authenticated())
                .formLogin(
                        fromLogin ->{
                            fromLogin
                                    // redirect here when we can access something which is not allowed
                                    //also this is the page where we preform login.
                                    .loginPage("/users/login")
                                    // the names of input fields (in our case auth-login.hmtl)
                                    .usernameParameter("email")
                                    .passwordParameter("password")
                                    .defaultSuccessUrl("/home", true)
                                    .failureForwardUrl("/users/login-error");
                        }
                ).logout(
                        logout -> {
                            logout.logoutUrl("/users/logout")
                                    .logoutSuccessUrl("/")
                                    .invalidateHttpSession(true);
                        }
                ).rememberMe(
                        rememberMe ->
                                rememberMe
                                        .key("rememberMeKey")
                                        .rememberMeParameter("rememberme")
                                        .rememberMeCookieName("rememberme"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository ){
        return new ApplicationUserDetailsService(userRepository);
    }
}
