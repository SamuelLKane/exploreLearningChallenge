package com.samuelkane.exploreLearningChallenge.service;

import com.samuelkane.exploreLearningChallenge.domain.User;
import com.samuelkane.exploreLearningChallenge.domain.UserRepository;
import com.samuelkane.exploreLearningChallenge.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void addUser(String json) throws JSONException, UserAlreadyExistsException {
        JSONObject decodedJson = new JSONObject(json);
        User user = new User();
        user.setFirstName(decodedJson.getString("firstname"));
        user.setLastName(decodedJson.getString("lastname"));
        if(userRepository.existsByFirstNameAndLastName(
                user.getFirstName(),user.getLastName())
        ){
            throw new UserAlreadyExistsException();
        }
        userRepository.save(user);
    }

    public User getUser(Long id) {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException e){
            return null;
        }
    }

    public List<User> getAllUsers() {
        return StreamSupport
                .stream(userRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(User::getLastName))
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) { userRepository.deleteById(id); }
}
