package softuni.bg.finalPJ.service;

import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.models.entities.UserRoleEntity;
import softuni.bg.finalPJ.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public ApplicationUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findUserEntityByEmail(email)
                .map(ApplicationUserDetailsService::map).orElseThrow(
                        () -> new UsernameNotFoundException("User with name " + email + " not exist."));
    }

    private static UserDetails map(UserEntity userEntity){
       UserDetails userDetails = User
               .withUsername(userEntity.getEmail())
               .password(userEntity.getPassword())
               .authorities(userEntity.getRoles()
                       .stream()
                       .map(ApplicationUserDetailsService::mapRole).toList())
               .build();


       return userDetails;
    }

    private static GrantedAuthority mapRole(UserRoleEntity userRoleEntity){

        return new SimpleGrantedAuthority(
                "ROLE_" + userRoleEntity.getRole().name());
    }
}

