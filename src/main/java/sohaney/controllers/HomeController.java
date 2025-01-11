package sohaney.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import sohaney.services.UserService;

@Controller
@RequestMapping
public class HomeController {
    // UserService has user login and registration related functions.
    private final UserService userService;

    /**
     * See notes in AuthInterceptor.java regarding how this works 
     * through dependency injection and inversion of control.
     */
    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView webpage() {
        if(userService.getLoggedInUser() != null) {
            return new ModelAndView("redirect:/account");
        } else {
            return new ModelAndView("redirect:/login");
        }
    }

    @GetMapping("/logout")
    public String logout() {
        userService.unAuthenticate();
        return "redirect:/login";
    }
}
