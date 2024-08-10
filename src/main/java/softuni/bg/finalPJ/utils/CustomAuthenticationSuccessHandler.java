package softuni.bg.finalPJ.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.service.UserService;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String email = ((UserEntity) authentication.getPrincipal()).getEmail();
        UserEntity user = userService.findUserByEmail(email);

        String targetUrl = "/home/" + user.getId();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
