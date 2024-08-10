package softuni.bg.finalPJ.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.bg.finalPJ.models.entities.Image;

@Repository
public interface AvatarImageRepository extends JpaRepository<Image, Long> {

    Image findAvatarImageByUserId(Long id);
}
