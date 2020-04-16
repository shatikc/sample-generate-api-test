package com.example.petstore.controller;

import com.example.petstore.api.UserApi;
import com.example.petstore.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController implements UserApi {


    @Override
    public ResponseEntity<User> createUser(@Valid User user) {
        return null;
    }

    @Override
    public ResponseEntity<User> createUsersWithListInput(@Valid List<User> user) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteUser(String username) {
        return null;
    }

    @Override
    public ResponseEntity<User> getUserByName(String username) {
        return null;
    }

    @Override
    public ResponseEntity<String> loginUser(@Valid String username, @Valid String password) {
        return null;
    }

    @Override
    public ResponseEntity<Void> logoutUser() {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateUser(String username, @Valid User user) {
        return null;
    }
}