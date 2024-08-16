package softuni.bg.finalPJ.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.UserRepository;


import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationUserDetailsServiceTest {

    private ApplicationUserDetailsService serviceToTest;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp(){
        serviceToTest = new ApplicationUserDetailsService(mockUserRepository);
    }

    @Test
    void testUserNotFound(){
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername("test@softuni.bg"));
    }

    @Test
    void testUserFoundException(){

        UserEntity testUserEntity = createTestUser();
        when(mockUserRepository.findUserEntityByEmail(testUserEntity.getEmail()))
                .thenReturn(Optional.of(testUserEntity));


        UserDetails userDetails = serviceToTest.loadUserByUsername(testUserEntity.getEmail());

        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(testUserEntity.getEmail(), userDetails.getUsername());

        Assertions.assertEquals(testUserEntity.getPassword(), userDetails.getPassword());

    }

    private static UserEntity createTestUser(){

        return new UserEntity()
                .setFirstName("fistName")
                .setLastName("lastName")
                .setEmail("test@softuni.bg")
                .setPassword("12345");
    }
}