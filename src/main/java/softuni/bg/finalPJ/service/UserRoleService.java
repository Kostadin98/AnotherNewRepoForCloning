package softuni.bg.finalPJ.service;

import softuni.bg.finalPJ.models.entities.UserRoleEntity;
import softuni.bg.finalPJ.models.enums.UserRoleEnum;

import java.util.List;
import java.util.Optional;

public interface UserRoleService {

    List<UserRoleEntity> findAll();

    UserRoleEntity findUserRoleEntityByRole(UserRoleEnum role);
}
