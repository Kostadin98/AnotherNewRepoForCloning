package softuni.bg.finalPJ.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.service.CommentService;
import softuni.bg.finalPJ.service.UserService;


import java.security.Principal;

@Controller
public class CommentController {

    private final UserService userService;
    private final CommentService commentService;

    @Autowired
    public CommentController(UserService userService, CommentService commentService) {
        this.userService = userService;
        this.commentService = commentService;
    }


    @PostMapping("profile/{id}/addComment")
    public ModelAndView addComment(@PathVariable Long id,
                                   @RequestParam String content,
                                   @RequestParam(required = false) String author,
                                   Principal principal) {

        UserEntity profileOwner = userService.findById(id);
        UserEntity currentUser = principal != null ? userService.findUserByEmail(principal.getName()) : null;

        if (currentUser != null && currentUser.getId().equals(id)) {
            ModelAndView modelAndView = new ModelAndView("redirect:/profile/" + id);
            modelAndView.addObject("error", "CannotCommentOnOwnProfile");
            return modelAndView;
        }

        if (currentUser != null) {
            author = currentUser.fullName();
        } else if (author == null || author.isEmpty()) {
            author = "Anonymous";
        }


        commentService.addComment(profileOwner, author, content);


        return new ModelAndView("redirect:/profile/" + id);
    }

    @PostMapping("profile/{id}/deleteComment")
    public ModelAndView deleteComment(@PathVariable Long id,
                                      @RequestParam Long commentId,
                                      Principal principal) {

        UserEntity currentUser = principal != null ? userService.findUserByEmail(principal.getName()) : null;
        UserEntity profileOwner = userService.findById(id);

        commentService.deleteComment(commentId);

        ModelAndView modelAndView = new ModelAndView("redirect:/profile/" + id);
        modelAndView.addObject("profileOwner", profileOwner);

        return modelAndView;
    }

}
