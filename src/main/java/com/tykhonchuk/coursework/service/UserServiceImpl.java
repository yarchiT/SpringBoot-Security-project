package com.tykhonchuk.coursework.service;

import com.tykhonchuk.coursework.model.Role;
import com.tykhonchuk.coursework.model.User;
import com.tykhonchuk.coursework.repository.RoleRepository;
import com.tykhonchuk.coursework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
/*        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));*/
        userRepository.save(user);
    }

    @Override
    public HttpStatus login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user == null)
            return HttpStatus.NOT_FOUND;

        if(bCryptPasswordEncoder.matches(password, user.getPassword())){
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_FOUND;
    }
}
