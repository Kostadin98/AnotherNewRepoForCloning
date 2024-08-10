package softuni.bg.finalPJ.service;


import softuni.bg.finalPJ.models.entities.Comment;
import softuni.bg.finalPJ.models.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    void addComment(UserEntity user, String author, String content);

    List<Comment> getCommentsByUserId(Long userId);

    void deleteComment(Long commentId);

    Comment findById (Long commentId);

    String formatDate(LocalDateTime localDateTime);
}
