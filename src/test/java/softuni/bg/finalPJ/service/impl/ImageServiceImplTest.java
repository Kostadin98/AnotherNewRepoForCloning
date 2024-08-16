package softuni.bg.finalPJ.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import softuni.bg.finalPJ.models.entities.Image;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.AvatarImageRepository;
import softuni.bg.finalPJ.repositories.ImageRepository;
import softuni.bg.finalPJ.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplTest {

    @Mock
    private ImageRepository mockImageRepository;

    @Mock
    private AvatarImageRepository mockAvatarImageRepository;

    @Mock
    private UserService mockUserService;

    @Mock
    private MultipartFile mockFile;

    @InjectMocks
    private ImageServiceImpl serviceToTest;

    @Captor
    private ArgumentCaptor<Image> imageCaptor;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1L);
    }

    @Test
    void testFindImagesByUserId() {
        // Arrange
        Long userId = 1L;
        Image image1 = new Image();
        Image image2 = new Image();
        List<Image> images = List.of(image1, image2);

        when(mockImageRepository.findImagesByUserId(userId)).thenReturn(images);

        // Act
        List<Image> result = serviceToTest.findImagesByUserId(userId);

        // Assert
        assertEquals(2, result.size());
        verify(mockImageRepository, times(1)).findImagesByUserId(userId);
    }

    @Test
    void testSaveImage() throws IOException {
        // Arrange
        when(mockUserService.findById(1L)).thenReturn(testUser);
        when(mockFile.getOriginalFilename()).thenReturn("image.jpg");
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(mockFile.getBytes()).thenReturn("file content".getBytes());

        Path userDirectory = Paths.get("src/main/resources/static/images/user_1/");
        Files.createDirectories(userDirectory);

        // Act
        serviceToTest.saveImage(mockFile, 1L);

        // Assert
        verify(mockImageRepository, times(1)).save(imageCaptor.capture());
        Image savedImage = imageCaptor.getValue();

        assertEquals("image.jpg", savedImage.getFileName());

        // Normalize the expected path to match the system's path format
        String expectedPath = Paths.get("/images/user_1/image.jpg").toString();
        assertEquals(expectedPath, savedImage.getFilePath());

        assertEquals("image/jpeg", savedImage.getFileType());
        assertEquals(testUser, savedImage.getUser());
    }


    @Test
    void testSaveImageIOException() throws IOException {
        // Arrange
        when(mockUserService.findById(1L)).thenReturn(testUser);
        when(mockFile.getOriginalFilename()).thenReturn("image.jpg");
        doThrow(new IOException()).when(mockFile).getBytes();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> serviceToTest.saveImage(mockFile, 1L));
        verify(mockImageRepository, never()).save(any(Image.class));
    }

    @Test
    void testSaveAvatarImage() throws IOException {
        // Arrange
        when(mockUserService.findById(1L)).thenReturn(testUser);
        when(mockFile.getOriginalFilename()).thenReturn("avatar.jpg");
        when(mockFile.getContentType()).thenReturn("image/jpeg");
        when(mockFile.getBytes()).thenReturn("file content".getBytes());

        Path userDirectory = Paths.get("src/main/resources/static/images/avatars/user_1/");
        Files.createDirectories(userDirectory);

        // Act
        serviceToTest.saveAvatarImage(mockFile, 1L);

        // Assert
        verify(mockAvatarImageRepository, times(1)).save(imageCaptor.capture());
        Image savedAvatarImage = imageCaptor.getValue();

        assertEquals("avatar.jpg", savedAvatarImage.getFileName());

        // Normalize the expected path to match the system's path format
        String expectedAvatarPath = Paths.get("/images/avatars/user_1/avatar.jpg").toString();
        assertEquals(expectedAvatarPath, savedAvatarImage.getFilePath());

        assertEquals("image/jpeg", savedAvatarImage.getFileType());
        assertEquals(testUser, savedAvatarImage.getUser());

        // Verify that the user's avatar image was updated
        verify(mockUserService, times(1)).save(testUser);
    }


    @Test
    void testSaveAvatarImageIOException() throws IOException {
        // Arrange
        when(mockUserService.findById(1L)).thenReturn(testUser);
        when(mockFile.getOriginalFilename()).thenReturn("avatar.jpg");
        doThrow(new IOException()).when(mockFile).getBytes();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> serviceToTest.saveAvatarImage(mockFile, 1L));
        verify(mockAvatarImageRepository, never()).save(any(Image.class));
    }
}
