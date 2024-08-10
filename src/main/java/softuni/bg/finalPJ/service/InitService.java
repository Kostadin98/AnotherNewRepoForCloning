package softuni.bg.finalPJ.service;

import softuni.bg.finalPJ.models.entities.Category;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.models.entities.UserRoleEntity;
import softuni.bg.finalPJ.models.enums.CategoryEnum;
import softuni.bg.finalPJ.models.enums.UserRoleEnum;
import softuni.bg.finalPJ.repositories.CategoryRepository;
import softuni.bg.finalPJ.repositories.UserRepository;
import softuni.bg.finalPJ.repositories.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InitService {

    private UserRoleRepository userRoleRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;

    public InitService(UserRoleRepository userRoleRepository,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder, CategoryRepository categoryRepository
    ) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void init() {
        initRoles();
        initUsers();
    }

    private void initRoles() {
        if (userRoleRepository.count() == 0) {

            //Init roles
            UserRoleEntity normalUser = new UserRoleEntity().setRole(UserRoleEnum.NORMAL);
            UserRoleEntity admin = new UserRoleEntity().setRole(UserRoleEnum.ADMIN);

            userRoleRepository.save(normalUser);
            userRoleRepository.save(admin);

            //Init categories
            CategoryEnum[] categories = CategoryEnum.values();
            for (CategoryEnum categoryEnum: categories){

                Category categoryToSave = new Category().setCategory(categoryEnum);
                categoryRepository.save(categoryToSave);
            }
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            initAdmin();
            initNormalUser();
        }
    }

    private void initAdmin() {
        UserEntity admin = new UserEntity();
        admin.setEmail("admin@example");
        admin.setFirstName("Admin");
        admin.setLastName("Adminov");
        admin.setCompanyName("AdminCompany");
        admin.setPassword(passwordEncoder.encode("adminPassword"));
        admin.setRoles(userRoleRepository.findAll());

        userRepository.save(admin);
    }

    private void initNormalUser() {

        UserRoleEntity normalUserRole = userRoleRepository.findUserRoleEntityByRole(UserRoleEnum.NORMAL)
                .orElseThrow();

        UserEntity normalUser = new UserEntity();
        normalUser.setEmail("normalUser@example");
        normalUser.setFirstName("User");
        normalUser.setLastName("Userov");
        normalUser.setPassword(passwordEncoder.encode("normalUserPassword"));
        normalUser.setCompanyName("NormalCompany");
        normalUser.setRoles(List.of(normalUserRole));

        userRepository.save(normalUser);
    }

}
