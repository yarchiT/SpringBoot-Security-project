package com.tykhonchuk.coursework.web;

import com.tykhonchuk.coursework.model.User;
import com.tykhonchuk.coursework.repository.UserRepository;
import com.tykhonchuk.coursework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;

@CrossOrigin
@RestController
public class UserRestController {

    private final UserRepository userRepository;
    private UserService userService;

    @Autowired
    UserRestController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.POST)
    public ModelAndView login(@RequestParam String email,@RequestParam String password){
        HttpStatus status = userService.login(email,password);

        ModelAndView modelAndView = new ModelAndView();

        if(status.value() == 200){
            modelAndView.setViewName("home");
        }else{
            modelAndView.addObject("errorMessage", "Email or password invalid");
            modelAndView.setViewName("login");
            return modelAndView;
        }

        return new ModelAndView("redirect:/home");
    }


    @RequestMapping(value={ "/home"}, method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.CREATED)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }


    //Get all
    @GetMapping("/users")
    @CrossOrigin
    public Collection<User> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User name " + authentication.getName());
        return userRepository.findAll();
    }

    //Get by id
    @GetMapping("/users/{email}")
    public User getUserByNickName(@PathVariable("email") String email) {
        return userRepository.findByEmail(email);
    }

    /*@PostMapping("/login")
    public ResponseEntity<String> signin(@RequestParam String nickName, @RequestParam String password) {

    }*/
}
