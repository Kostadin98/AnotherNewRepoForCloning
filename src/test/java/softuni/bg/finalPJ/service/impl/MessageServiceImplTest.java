package softuni.bg.finalPJ.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import softuni.bg.finalPJ.models.DTOs.MessageDTO;
import softuni.bg.finalPJ.models.entities.Message;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.MessageRepository;
import softuni.bg.finalPJ.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceImplTest {

    private MessageServiceImpl serviceToTest;

    @Mock
    private MessageRepository mockMessageRepository;

    @Mock
    private UserService mockUserService;

    @Mock
    private ModelMapper mockModelMapper;

    private UserEntity testUser;
    private Message testMessage;

    @BeforeEach
    void setUp() {
        serviceToTest = new MessageServiceImpl(mockMessageRepository, mockUserService, mockModelMapper);

        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        testMessage = new Message();
        testMessage.setId(1L);
        testMessage.setSenderName("Sender");
        testMessage.setSenderEmail("sender@example.com");
        testMessage.setSenderPhone("123-456-7890");
        testMessage.setContent("Test message content");
        testMessage.setFormattedDate(serviceToTest.formatDate(LocalDateTime.now()));
        testMessage.setReceiver(testUser);
    }

    @Test
    void testSaveMessage() {
        // Arrange
        MessageDTO messageDTO = new MessageDTO()
                .setSenderName("Sender")
                .setSenderEmail("sender@example.com")
                .setSenderPhone("123-456-7890")
                .setContent("Test message content");

        when(mockUserService.findById(1L)).thenReturn(testUser);
        when(mockModelMapper.map(messageDTO, Message.class)).thenReturn(testMessage);

        // Act
        serviceToTest.saveMessage(messageDTO, 1L);

        // Assert
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(mockMessageRepository, times(1)).save(messageCaptor.capture());
        Message savedMessage = messageCaptor.getValue();

        assertEquals("Sender", savedMessage.getSenderName());
        assertEquals("sender@example.com", savedMessage.getSenderEmail());
        assertEquals("123-456-7890", savedMessage.getSenderPhone());
        assertEquals("Test message content", savedMessage.getContent());
        assertEquals(testUser, savedMessage.getReceiver());
        assertNotNull(savedMessage.getFormattedDate());
    }

    @Test
    void testGetMessagesForUser() {
        // Arrange
        when(mockUserService.findById(1L)).thenReturn(testUser);
        when(mockMessageRepository.findByReceiver(testUser)).thenReturn(List.of(testMessage));

        // Act
        List<Message> messages = serviceToTest.getMessagesForUser(1L);

        // Assert
        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals(testMessage, messages.get(0));
    }

    @Test
    void testDeleteMessage_Success() {
        // Arrange
        when(mockMessageRepository.findById(1L)).thenReturn(Optional.of(testMessage));

        // Act
        serviceToTest.deleteMessage(1L, 1L);

        // Assert
        verify(mockMessageRepository, times(1)).delete(testMessage);
    }

    @Test
    void testDeleteMessage_AccessDenied() {
        // Arrange
        UserEntity anotherUser = new UserEntity();
        anotherUser.setId(2L);

        testMessage.setReceiver(anotherUser);

        when(mockMessageRepository.findById(1L)).thenReturn(Optional.of(testMessage));

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> serviceToTest.deleteMessage(1L, 1L));
        verify(mockMessageRepository, never()).delete(testMessage);
    }

    @Test
    void testFormatDate() {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.of(2023, 8, 1, 12, 0, 0);
        String expectedFormattedDate = "08/01/2023 12:00:00";

        // Act
        String formattedDate = serviceToTest.formatDate(dateTime);

        // Assert
        assertEquals(expectedFormattedDate, formattedDate);
    }
}
