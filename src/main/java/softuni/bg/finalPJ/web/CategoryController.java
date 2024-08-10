package softuni.bg.finalPJ.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import softuni.bg.finalPJ.models.entities.Category;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.service.CategoryService;
import softuni.bg.finalPJ.service.UserService;

import java.util.List;

@Controller
public class CategoryController {

    private final UserService userService;
    private final CategoryService categoryService;

    public CategoryController(UserService userService, CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }


    @GetMapping("profile/{id}/manageCategories")
    public ModelAndView manageCategories(@PathVariable("id") Long id,
                                         Authentication authentication) {
        UserEntity user = userService.findById(id);
        List<Category> allCategories = categoryService.findAll();

        if (!user.getEmail().equals(authentication.getName())) {
            throw new SecurityException("You cannot manage categories for another user");
        }

        ModelAndView modelAndView = new ModelAndView("manageCategories");
        modelAndView.addObject("user", user);
        modelAndView.addObject("categories", allCategories);
        modelAndView.addObject("userCategories", user.getCategories());
        return modelAndView;
    }

    @PostMapping("/profile/{id}/manageCategories/addCategory")
    public ModelAndView addCategory(@PathVariable("id") Long id,
                                    @RequestParam("categoryId") Long categoryId,
                              Authentication authentication) {
        UserEntity user = userService.findById(id);

        if (!user.getEmail().equals(authentication.getName())) {
            throw new SecurityException("You cannot modify another user's categories");
        }

        Category category = categoryService.findById(categoryId);
        user.getCategories().add(category);
        userService.save(user);

        return new ModelAndView("redirect:/profile/" + user.getId() + "/manageCategories");
    }

    @PostMapping("/profile/{id}/manageCategories/removeCategory/{categoryId}")
    public ModelAndView removeCategory(@PathVariable("id") Long id,
                                       @RequestParam("categoryId") Long categoryId,
                                 Authentication authentication) {
        UserEntity user = userService.findById(id);

        if (!user.getEmail().equals(authentication.getName())) {
            throw new SecurityException("You cannot modify another user's categories");
        }

        Category category = categoryService.findById(categoryId);

        user.getCategories().remove(category);
        userService.save(user);

        return new ModelAndView("redirect:/profile/" + user.getId() + "/manageCategories");
    }
}

