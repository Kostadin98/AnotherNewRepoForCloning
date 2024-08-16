package softuni.bg.finalPJ.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import softuni.bg.finalPJ.models.DTOs.UserRegistrationDTO;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.models.entities.UserRoleEntity;
import softuni.bg.finalPJ.models.enums.UserRoleEnum;
import softuni.bg.finalPJ.repositories.UserRepository;
import softuni.bg.finalPJ.service.UserRoleService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleService userRoleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        UserEntity user = new UserEntity();
        userService.save(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegister_UserAlreadyExists() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setEmail("test@example.com");
        userRegistrationDTO.setFirstName("Test");
        userRegistrationDTO.setLastName("User");
        userRegistrationDTO.setCompanyName("TestCompany");
        userRegistrationDTO.setPassword("password");
        userRegistrationDTO.setConfirmPassword("password");

        when(userRepository.findUserEntityByEmail(anyString())).thenReturn(Optional.of(new UserEntity()));

        boolean result = userService.register(userRegistrationDTO);

        assertFalse(result);
    }

    @Test
    void testRegister_PasswordsDoNotMatch() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setEmail("test@example.com");
        userRegistrationDTO.setFirstName("Test");
        userRegistrationDTO.setLastName("User");
        userRegistrationDTO.setCompanyName("TestCompany");
        userRegistrationDTO.setPassword("password");
        userRegistrationDTO.setConfirmPassword("differentPassword");

        when(userRepository.findUserEntityByEmail(anyString())).thenReturn(Optional.empty());

        boolean result = userService.register(userRegistrationDTO);

        assertFalse(result);
    }

    @Test
    void testRegister_Successful() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setEmail("test@example.com");
        userRegistrationDTO.setFirstName("Test");
        userRegistrationDTO.setLastName("User");
        userRegistrationDTO.setCompanyName("TestCompany");
        userRegistrationDTO.setPassword("password");
        userRegistrationDTO.setConfirmPassword("password");

        UserRoleEntity role = new UserRoleEntity();
        role.setRole(UserRoleEnum.NORMAL);

        when(userRepository.findUserEntityByEmail(anyString())).thenReturn(Optional.empty());
        when(userRoleService.findUserRoleEntityByRole(any(UserRoleEnum.class))).thenReturn(role);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        boolean result = userService.register(userRegistrationDTO);

        assertTrue(result);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testFindById_UserFound() {
        UserEntity user = new UserEntity();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserEntity result = userService.findById(1L);

        assertEquals(user, result);
    }

    @Test
    void testFindById_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findById(1L));
    }

    @Test
    void testFindUserByEmail_UserFound() {
        UserEntity user = new UserEntity();
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        UserEntity result = userService.findUserByEmail("test@example.com");

        assertEquals(user, result);
    }

    @Test
    void testFindUserByEmail_UserNotFound() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findUserByEmail("test@example.com"));
    }

    @Test
    void testUpdateFirstName() {
        UserEntity user = new UserEntity();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.updateFirstName(1L, "NewFirstName");

        assertEquals("NewFirstName", user.getFirstName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateLastName() {
        UserEntity user = new UserEntity();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.updateLastName(1L, "NewLastName");

        assertEquals("NewLastName", user.getLastName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateCompanyName() {
        UserEntity user = new UserEntity();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.updateCompanyName(1L, "NewCompanyName");

        assertEquals("NewCompanyName", user.getCompanyName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdatePassword() {
        UserEntity user = new UserEntity();
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.updatePassword(user, "newPassword");

        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCheckIfNewAndCurrentPasswordMatches() {
        UserEntity user = new UserEntity();
        user.setPassword("encodedPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        boolean result = userService.checkIfNewAndCurrentPasswordMatches(user, "newPassword");

        assertTrue(result);
    }

    @Test
    void testCheckIfPasswordAndConfirmPasswordMatches() {
        boolean result = userService.checkIfPasswordAndConfirmPasswordMatches("password", "password");

        assertTrue(result);
    }

    @Test
    void testIsAdmin_UserIsAdmin() {
        UserEntity user = new UserEntity();
        UserRoleEntity adminRole = new UserRoleEntity().setRole(UserRoleEnum.ADMIN);
        user.setRoles(List.of(adminRole));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        boolean result = userService.isAdmin(1L);

        assertTrue(result);
    }

    @Test
    void testIsAdmin_UserIsNotAdmin() {
        UserEntity user = new UserEntity();
        UserRoleEntity normalRole = new UserRoleEntity().setRole(UserRoleEnum.NORMAL);
        user.setRoles(List.of(normalRole));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        boolean result = userService.isAdmin(1L);

        assertFalse(result);
    }

    @Test
    void testIsProfileOwner_True() {
        UserEntity user = new UserEntity().setEmail("test@example.com");
        when(authentication.getName()).thenReturn("test@example.com");

        boolean result = userService.isProfileOwner(authentication, user);

        assertTrue(result);
    }

    @Test
    void testIsProfileOwner_False() {
        UserEntity user = new UserEntity().setEmail("test@example.com");
        when(authentication.getName()).thenReturn("different@example.com");

        boolean result = userService.isProfileOwner(authentication, user);

        assertFalse(result);
    }

    @Test
    void testCheckIfUserIsAuthorized_Authorized() {
        UserEntity user = new UserEntity().setEmail("test@example.com");
        when(authentication.getName()).thenReturn("test@example.com");

        assertDoesNotThrow(() -> userService.checkIfUserIsAuthorized(user, authentication));
    }

    @Test
    void testCheckIfUserIsAuthorized_NotAuthorized() {
        UserEntity user = new UserEntity().setEmail("test@example.com");
        when(authentication.getName()).thenReturn("different@example.com");

        assertThrows(RuntimeException.class, () -> userService.checkIfUserIsAuthorized(user, authentication));
    }

    // Additional tests for methods like findByCategoriesId, searchUsers, etc., can be added here as needed.
}
