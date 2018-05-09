package com.tykhonchuk.coursework.service;

import com.tykhonchuk.coursework.model.User;

public interface UserService {
     User findUserByEmail(String email);
     void saveUser(User user);
}
