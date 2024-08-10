package softuni.bg.finalPJ.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import softuni.bg.finalPJ.models.DTOs.UserRegistrationDTO;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.models.entities.UserRoleEntity;
import softuni.bg.finalPJ.models.enums.UserRoleEnum;
import softuni.bg.finalPJ.repositories.UserRepository;
import softuni.bg.finalPJ.repositories.UserRoleRepository;
import softuni.bg.finalPJ.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public boolean register(UserRegistrationDTO userRegistrationDTO) {

        boolean userAlreadyExists = userRepository.findUserEntityByEmail(userRegistrationDTO.getEmail()).isPresent();
        boolean passwordMatches = userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPassword());

        if (userAlreadyExists || !passwordMatches) {
            return false;
        }

        UserRoleEntity roleToSet = userRoleRepository.findUserRoleEntityByRole(UserRoleEnum.NORMAL).get();

        UserEntity userToSave = new UserEntity();
        userToSave.setEmail(userRegistrationDTO.getEmail());
        userToSave.setFirstName(userRegistrationDTO.getFirstName());
        userToSave.setLastName(userRegistrationDTO.getLastName());
        userToSave.setCompanyName(userRegistrationDTO.getCompanyName());
        userToSave.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        userToSave.addRole(roleToSet);

        userRepository.save(userToSave);
        return true;
    }

    @Override
    public List<UserEntity> findByCategoriesId(Long categoryId) {

        return userRepository.findByCategoriesId(categoryId);
    }

    // Logic for "search" page
    @Override
    public List<UserEntity> searchUsers(String query, Long categoryId) {
        if (categoryId != null) {
            return userRepository.findByCategoriesId(categoryId);
        } else {
            return userRepository.findByCompanyNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    query, query, query, query);
        }
    }

    @Override
    public List<UserEntity> findAllUsers() {
        List<UserEntity> all = userRepository.findAll();

        return all;
    }

    @Override
    public UserEntity findById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void updateFirstName(Long id, String firstName) {

        UserEntity user = userRepository.findById(id).get();
        user.setFirstName(firstName);
        userRepository.save(user);
    }

    @Override
    public void updateLastName(Long id, String lastName) {

        UserEntity user = userRepository.findById(id).get();
        user.setLastName(lastName);
        userRepository.save(user);
    }

    @Override
    public void updateCompanyName(Long id, String companyName) {

        UserEntity user = findById(id);
        user.setCompanyName(companyName);
        save(user);

    }

    @Override
    public void updatePassword(UserEntity user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfNewAndCurrentPasswordMatches(UserEntity user, String passwordToMatch){
       return passwordEncoder.matches(passwordToMatch, user.getPassword());
    }

    @Override
    public boolean checkIfPasswordAndConfirmPasswordMatches(String password, String confirmPassword) {

        return password.equals(confirmPassword);
    }

}
