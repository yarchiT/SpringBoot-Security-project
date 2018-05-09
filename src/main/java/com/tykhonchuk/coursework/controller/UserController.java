package com.tykhonchuk.coursework.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.tykhonchuk.coursework.model.User;
import com.tykhonchuk.coursework.service.UserService;
import com.tykhonchuk.coursework.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    @Autowired
    private UserServiceImpl userService;




}
