package softuni.bg.finalPJ.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.bg.finalPJ.models.DTOs.MessageDTO;
import softuni.bg.finalPJ.models.entities.Message;
import softuni.bg.finalPJ.models.entities.UserEntity;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByReceiver(UserEntity receiver);

}
