package com.tykhonchuk.coursework.service;

import com.tykhonchuk.coursework.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface UserService {
     User findUserByEmail(String email);
     void saveUser(User user);
     HttpStatus login(String email, String password);
}
