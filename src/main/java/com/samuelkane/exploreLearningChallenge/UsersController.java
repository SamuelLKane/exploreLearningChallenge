package com.samuelkane.exploreLearningChallenge;

import com.samuelkane.exploreLearningChallenge.db.User;
import com.samuelkane.exploreLearningChallenge.exception.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin // Look into what this specifically does
@RestController
@Slf4j
@RequestMapping("v1")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService){
        this.usersService = usersService;
    }

//    @PostMapping(value = "/users")
//    public ResponseEntity<?> postCreateUser(HttpEntity<String> httpEntity) {
//        try {
//            usersService.addUser(httpEntity.getBody());
//        } catch (Exception e) {
//            /**
//             * I'm making a deliberate choice here to not catch specific errors.
//             * None of the errors thrown by addUser() require an alternate way of
//             * dealing with the error and, from a security perspective, I'd rather
//             * most errors look identical so an attacker can't glean information
//             * about the state of the service when issuing requests
//             */
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping(path = "/users/{userId}")
//    public ResponseEntity<?> getSpecificUser(@PathVariable("userId") Long userId) {
//        return ResponseEntity.ok(usersService.getUser(userId));
//    }
//
//    @GetMapping(path = "/users")
//    public ResponseEntity<?> getAllUsers() {
//        return ResponseEntity.ok(usersService.getAllUsers());
//    }
//
//    @DeleteMapping(path = "/users/{userId}")
//    public void deleteUser(@PathVariable("userId") Long userId){
//        usersService.deleteUser(userId);
//    }
}
