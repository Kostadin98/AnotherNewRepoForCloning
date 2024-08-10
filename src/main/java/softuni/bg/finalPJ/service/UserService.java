package softuni.bg.finalPJ.service;

import softuni.bg.finalPJ.models.DTOs.UserRegistrationDTO;
import softuni.bg.finalPJ.models.entities.UserEntity;

import java.util.List;

public interface UserService {

    void save(UserEntity user);

    boolean register(UserRegistrationDTO userRegistrationDTO);

    List<UserEntity> findByCategoriesId(Long categoryId);

    List<UserEntity> searchUsers(String query,  Long categoryId);

    List<UserEntity> findAllUsers();

    UserEntity findById(Long id);

    UserEntity findUserByEmail(String email);

    void updateFirstName(Long id,String firstName);

    void updateLastName(Long id, String lastName);

    void updateCompanyName(Long id, String companyName);

    void updatePassword(UserEntity user, String password);

    boolean checkIfNewAndCurrentPasswordMatches(UserEntity user, String passwordToMatch);

    boolean checkIfPasswordAndConfirmPasswordMatches(String password, String confirmPassword);
}
