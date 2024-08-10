package softuni.bg.finalPJ.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import softuni.bg.finalPJ.models.DTOs.MessageDTO;
import softuni.bg.finalPJ.models.entities.Message;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.MessageRepository;
import softuni.bg.finalPJ.service.MessageService;
import softuni.bg.finalPJ.service.UserService;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, UserService userService, ModelMapper modelMapper) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void saveMessage(MessageDTO messageDTO, Long userId) {

        UserEntity receiver = userService.findById(userId);
        String formattedDate = formatDate(LocalDateTime.now());

        Message message = this.modelMapper.map(messageDTO, Message.class);

        message.setFormattedDate(formattedDate);
        message.setReceiver(receiver);

        messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesForUser(Long userId) {
        UserEntity receiver = userService.findById(userId);
        return messageRepository.findByReceiver(receiver);
    }

    @Override
    public void deleteMessage(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found"));
        if (message.getReceiver().getId().equals(userId)) {
            messageRepository.delete(message);
        } else {
            throw new AccessDeniedException("You are not authorized to delete this message");
        }
    }

    @Override
    public String formatDate(LocalDateTime localDateTime){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        String formattedDate = formatter.format(localDateTime);

        return formattedDate;
    }
}
