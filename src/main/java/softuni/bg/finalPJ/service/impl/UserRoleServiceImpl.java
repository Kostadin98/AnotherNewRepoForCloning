package softuni.bg.finalPJ.service.impl;

import org.springframework.stereotype.Service;
import softuni.bg.finalPJ.models.entities.UserRoleEntity;
import softuni.bg.finalPJ.models.enums.UserRoleEnum;
import softuni.bg.finalPJ.repositories.UserRoleRepository;
import softuni.bg.finalPJ.service.UserRoleService;

import java.util.List;
import java.util.Optional;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public List<UserRoleEntity> findAll() {
        return userRoleRepository.findAll();
    }

    @Override
    public UserRoleEntity findUserRoleEntityByRole(UserRoleEnum role) {
        return userRoleRepository.findUserRoleEntityByRole(role)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }
}
