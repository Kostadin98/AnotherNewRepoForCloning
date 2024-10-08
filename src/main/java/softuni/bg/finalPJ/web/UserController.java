package softuni.bg.finalPJ.web;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import softuni.bg.finalPJ.models.entities.Comment;
import softuni.bg.finalPJ.models.entities.Image;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.models.entities.UserRoleEntity;
import softuni.bg.finalPJ.models.enums.UserRoleEnum;
import softuni.bg.finalPJ.service.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final ImageService imageService;
    private final QrCodeService qrCodeService;
    private final CommentService commentService;
    private final UserRoleService userRoleService;



    @Autowired
    public UserController(UserService userService,
                          ImageService imageService,
                          QrCodeService qrCodeService,
                          CommentService commentService,
                          UserRoleService userRoleService) {
        this.userService = userService;
        this.imageService = imageService;
        this.qrCodeService = qrCodeService;
        this.commentService = commentService;
        this.userRoleService = userRoleService;
    }


    //Logged user view
    @GetMapping("/profile")
    public ModelAndView showLoggedInUserProfile(Authentication authentication) throws IOException, WriterException {

        UserEntity user = userService.findUserByEmail(authentication.getName());

        //Generating QR code if not exists
        qrCodeService.saveQrIfHasNoExisting(user);

        ModelAndView modelAndView = new ModelAndView("profile");

        List<Comment> comments = commentService.getCommentsByUserId(user.getId());
        modelAndView.addObject("comments", comments);
        modelAndView.addObject("user", user);

        // Add a flag to determine if the logged-in user is the profile owner
        boolean isProfileOwner = userService.isProfileOwner(authentication, user);
        boolean isAdmin = userService.isAdmin(user.getId());
        modelAndView.addObject("isAdmin", isAdmin);
        modelAndView.addObject("isProfileOwner", isProfileOwner);
        return modelAndView;
    }

    //Not logged user view
    @GetMapping("/profile/{id}")
    public ModelAndView showUserProfile(@PathVariable("id") Long id, Authentication authentication)
            throws IOException, WriterException {
        ModelAndView modelAndView = new ModelAndView("profile");

        UserEntity user = userService.findById(id);

        //Generating QR code if not exists
        qrCodeService.saveQrIfHasNoExisting(user);

        List<Comment> comments = commentService.getCommentsByUserId(user.getId());
        modelAndView.addObject("comments", comments);
        modelAndView.addObject("user", user);

        boolean isProfileOwner = userService.isProfileOwner(authentication, user);
        boolean isAdmin = userService.isAdmin(id);
        modelAndView.addObject("isAdmin", isAdmin);
        modelAndView.addObject("isProfileOwner", isProfileOwner);

        return modelAndView;
    }

    @GetMapping("/profile/{id}/viewAsAnonymous")
    public ModelAndView viewProfileAsAnonymous(@PathVariable("id") Long id) throws IOException, WriterException {
        ModelAndView modelAndView = new ModelAndView("profile");

        UserEntity user = userService.findById(id);


        List<Comment> comments = commentService.getCommentsByUserId(user.getId());
        modelAndView.addObject("comments", comments);
        modelAndView.addObject("user", user);

        modelAndView.addObject("isProfileOwner", false);

        return modelAndView;
    }

    @GetMapping("/profile/{id}/gallery")
    public ModelAndView viewGallery(@PathVariable("id") Long id, Authentication authentication) {

        UserEntity user = userService.findById(id);

        boolean isProfileOwner = userService.isProfileOwner(authentication, user);

        List<Image> images = imageService.findImagesByUserId(id);
        ModelAndView modelAndView = new ModelAndView("gallery");
        modelAndView.addObject("user", user);
        modelAndView.addObject("images", images);
        modelAndView.addObject("isProfileOwner", isProfileOwner);
        return modelAndView;
    }

    @PostMapping("/profile/{id}/gallery/upload")
    public ModelAndView uploadImage(@PathVariable("id") Long id,
                                    @RequestParam("file") MultipartFile file,
                                    Principal principal) {

        UserEntity user = userService.findById(id);

        imageService.saveImage(file, id);

        return new ModelAndView("redirect:/profile/" + id + "/gallery");
    }

    @PostMapping("/profile/{id}/uploadAvatar")
    public ModelAndView uploadAvatar(@PathVariable("id") Long id,
                                    @RequestParam("file") MultipartFile file,
                                    Principal principal) {

        UserEntity user = userService.findById(id);

        imageService.saveAvatarImage(file, id);


        return new ModelAndView("redirect:/profile/" + id);
    }

    @PostMapping("/profile/{id}/updateDescription")
    public ModelAndView updateDescription(@PathVariable("id") Long id,
                                          @RequestParam("description") String description,
                                          Authentication authentication) {

        UserEntity user = userService.findById(id);
        userService.checkIfUserIsAuthorized(user, authentication);

        user.setDescription(description);
        userService.save(user);

        return new ModelAndView("redirect:/profile/" + id);
    }

    @PostMapping("/profile/{id}/setAdmin")
    public ModelAndView setAdmin(@PathVariable("id") Long id){

        UserEntity user = userService.findById(id);
        UserRoleEntity roleToSet = userRoleService.findUserRoleEntityByRole(UserRoleEnum.ADMIN);

        user.addRole(roleToSet);
        userService.save(user);
        return new ModelAndView("redirect:/profile/" + id);
    }
}