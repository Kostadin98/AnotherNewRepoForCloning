package softuni.bg.finalPJ.service;

import softuni.bg.finalPJ.models.DTOs.MessageDTO;
import softuni.bg.finalPJ.models.entities.Message;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageService {

    void saveMessage(MessageDTO messageDTO,Long id);

    List<Message> getMessagesForUser(Long userId);

    void deleteMessage(Long messageId, Long userId);

    String formatDate(LocalDateTime localDateTime);
}
