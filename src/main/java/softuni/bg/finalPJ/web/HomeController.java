package softuni.bg.finalPJ.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.service.CategoryService;
import softuni.bg.finalPJ.service.ScheduledTaskService;
import softuni.bg.finalPJ.service.UserService;

import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final ScheduledTaskService scheduledTaskService;

    public HomeController(UserService userService, CategoryService categoryService, ScheduledTaskService scheduledTaskService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.scheduledTaskService = scheduledTaskService;
    }


    @GetMapping("/search")
    public ModelAndView listUsers(@RequestParam(value = "query", required = false) String query,
                                  @RequestParam(value = "category", required = false) Long categoryId) {

        List<UserEntity> users = userService.searchUsers(query, categoryId);

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

        String weeklyMessage = scheduledTaskService.getWeeklyMessage();
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("user", user);
        modelAndView.addObject("weeklyMessage", weeklyMessage);

        return modelAndView;
    }

    @GetMapping("/about")
    public ModelAndView about(){

        return new ModelAndView("about");
    }


}
