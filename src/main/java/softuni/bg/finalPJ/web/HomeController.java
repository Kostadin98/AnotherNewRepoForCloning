package softuni.bg.finalPJ.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.service.CategoryService;
import softuni.bg.finalPJ.service.UserService;

import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;
    private final CategoryService categoryService;

    public HomeController(UserService userService, CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }


    @GetMapping("/search")
    public ModelAndView listUsers(@RequestParam(value = "query", required = false) String query,
                                  @RequestParam(value = "category", required = false) Long categoryId) {
        List<UserEntity> users;
        if (query != null && !query.isEmpty()) {
            users = userService.searchUsers(query, categoryId);
        } else if(categoryId != null) {
            users = userService.searchUsers(query, categoryId);
        }else {
            users = userService.findAllUsers();
        }

        ModelAndView modelAndView = new ModelAndView("search");
        modelAndView.addObject("users", users);
        modelAndView.addObject("query", query);
        modelAndView.addObject("categoryId", categoryId);
        modelAndView.addObject("categories", categoryService.findAll());
        return modelAndView;
    }



    @GetMapping("/")
    public ModelAndView index(){

        return new ModelAndView("index");
    }

    @GetMapping("/home")
    public ModelAndView home(@AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.findUserByEmail(userDetails.getUsername());

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping("/about")
    public ModelAndView about(){

        return new ModelAndView("about");
    }


}
