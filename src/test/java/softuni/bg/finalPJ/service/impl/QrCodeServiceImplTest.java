package softuni.bg.finalPJ.service.impl;

import com.google.zxing.WriterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QrCodeServiceImplTest {

    private QrCodeServiceImpl serviceToTest;

    @Mock
    private UserRepository mockUserRepository;

    @Captor
    private ArgumentCaptor<UserEntity> userCaptor;

    @BeforeEach
    void setUp() {
        serviceToTest = new QrCodeServiceImpl(mockUserRepository);
    }

    @Test
    void testGenerateQRCodeImage() throws IOException, WriterException {
        Long userId = 1L;
        String text = "Test QR Code";

        String filePath = serviceToTest.generateQRCodeImage(text, userId);

        // Verify the file path
        assertNotNull(filePath);
        assertTrue(filePath.endsWith("qr_code_" + userId + "_qr.png"));

        // Verify the file is created
        Path fullFilePath = Path.of("src/main/resources/static/images/qr-codes/qr_code_" + userId + "_qr.png");
        assertTrue(Files.exists(fullFilePath));

        // Cleanup (optional)
        Files.deleteIfExists(fullFilePath);
    }

    @Test
    void testSaveQrIfHasNoExisting_createsQRCode() throws IOException, WriterException {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setQrCodePath(null); // No existing QR code path

        serviceToTest.saveQrIfHasNoExisting(user);

        // Verify that QR code path was set and user was saved
        verify(mockUserRepository, times(1)).save(userCaptor.capture());
        UserEntity savedUser = userCaptor.getValue();
        assertNotNull(savedUser.getQrCodePath());
        assertTrue(savedUser.getQrCodePath().contains("qr_code_" + user.getId() + "_qr.png"));
    }

    @Test
    void testSaveQrIfHasNoExisting_doesNotCreateQRCodeIfAlreadyExists() throws IOException, WriterException {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setQrCodePath("/images/qr-codes/existing_qr.png"); // Existing QR code path

        serviceToTest.saveQrIfHasNoExisting(user);

        // Verify that userRepository.save() was not called because the QR code already exists
        verify(mockUserRepository, times(0)).save(any(UserEntity.class));
    }
}
