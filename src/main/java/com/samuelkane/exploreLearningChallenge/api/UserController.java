package com.samuelkane.exploreLearningChallenge.api;

import com.samuelkane.exploreLearningChallenge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/users")
    public ResponseEntity<?> postCreateUser(HttpEntity<String> httpEntity) {
        try {
            userService.addUser(httpEntity.getBody());
        } catch (Exception e) {
            /**
             * I'm making a deliberate choice here to not catch specific errors.
             * None of the errors thrown by addUser() require an alternate way of
             * dealing with the error and, from a security perspective, I'd rather
             * most errors look identical so an attacker can't glean information
             * about the state of the service when issuing requests
             */
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/users/{userId}")
    public ResponseEntity<?> getSpecificUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping(path = "/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping(path = "/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.accepted().build();
    }
}
