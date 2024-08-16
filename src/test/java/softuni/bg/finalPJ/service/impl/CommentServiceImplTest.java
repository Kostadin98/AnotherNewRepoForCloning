package softuni.bg.finalPJ.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import softuni.bg.finalPJ.models.entities.Comment;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.CommentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl serviceToTest;

    @Mock
    private CommentRepository mockCommentRepository;

    @BeforeEach
    void setUp() {
        serviceToTest = new CommentServiceImpl(mockCommentRepository);
    }

    @Test
    void testGetCommentsByUserId() {
        Long userId = 1L;
        List<Comment> testComments = new ArrayList<>();
        testComments.add(createTestComment(1L, "Test content 1", "Author 1"));
        testComments.add(createTestComment(2L, "Test content 2", "Author 2"));

        when(mockCommentRepository.findByUserId(userId)).thenReturn(testComments);

        List<Comment> result = serviceToTest.getCommentsByUserId(userId);

        Assertions.assertNotNull(result, "The result should not be null");
        Assertions.assertEquals(2, result.size(), "The result size should be 2");
        Assertions.assertEquals("Test content 1", result.get(0).getContent(), "First comment content should match");
        Assertions.assertEquals("Author 2", result.get(1).getAuthor(), "Second comment author should match");
    }

    @Test
    void testDeleteComment() {
        Long commentId = 1L;
        Comment testComment = createTestComment(commentId, "Test content", "Author");

        when(mockCommentRepository.findById(commentId)).thenReturn(Optional.of(testComment));

        serviceToTest.deleteComment(commentId);

        verify(mockCommentRepository, times(1)).delete(testComment);
    }

    @Test
    void testAddComment() {
        UserEntity testUser = createTestUser();
        String author = "Author";
        String content = "Test content";

        serviceToTest.addComment(testUser, author, content);

        verify(mockCommentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testFindByIdFound() {
        Long commentId = 1L;
        Comment testComment = createTestComment(commentId, "Test content", "Author");

        when(mockCommentRepository.findById(commentId)).thenReturn(Optional.of(testComment));

        Comment result = serviceToTest.findById(commentId);

        Assertions.assertNotNull(result, "The result should not be null");
        Assertions.assertEquals("Test content", result.getContent(), "The content should match");
        Assertions.assertEquals("Author", result.getAuthor(), "The author should match");
    }

    @Test
    void testFindByIdNotFound() {
        Long commentId = 1L;

        when(mockCommentRepository.findById(commentId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            serviceToTest.findById(commentId);
        });

        String expectedMessage = "Comment not found";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage), "The exception message should match");
    }

    @Test
    void testFormatDate() {
        LocalDateTime now = LocalDateTime.of(2024, 8, 16, 14, 30, 0);
        String expectedDate = "08/16/2024 14:30:00";

        String formattedDate = serviceToTest.formatDate(now);

        Assertions.assertEquals(expectedDate, formattedDate, "The formatted date should match the expected format");
    }

    @Test
    void testGetAuthorNameWithUser() {
        UserEntity testUser = createTestUser();
        String author = serviceToTest.getAuthorName(testUser, null);

        Assertions.assertEquals("John Doe", author, "The author name should match the user's full name");
    }

    @Test
    void testGetAuthorNameAnonymous() {
        String author = serviceToTest.getAuthorName(null, null);

        Assertions.assertEquals("Anonymous", author, "The author name should default to 'Anonymous'");
    }

    private Comment createTestComment(Long id, String content, String author) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setContent(content);
        comment.setAuthor(author);
        comment.setCreatedDate("08/16/2024 14:30:00");

        return comment;
    }

    private UserEntity createTestUser() {
        return new UserEntity()
                .setFirstName("John")
                .setLastName("Doe");
    }
}
