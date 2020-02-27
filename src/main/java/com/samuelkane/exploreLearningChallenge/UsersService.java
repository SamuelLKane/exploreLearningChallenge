package com.samuelkane.exploreLearningChallenge;

import com.samuelkane.exploreLearningChallenge.db.User;
import com.samuelkane.exploreLearningChallenge.db.UsersRepository;
import com.samuelkane.exploreLearningChallenge.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(final UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    public void addUser(String json) throws JSONException, UserAlreadyExistsException {
        JSONObject decodedJson = new JSONObject(json);
        User user = new User();
      user.setFirstName(decodedJson.getString("firstname"));
      user.setLastName(decodedJson.getString("lastname"));
//TODO: Get this working, the problem with all of this is h2 integration
//      I can't get it to behave the same as at New Engen...
//        if(usersRepository.existsByFirstNameAndLastName(
//                user.getFirstName(),user.getLastName())
//        ){
//            throw new UserAlreadyExistsException;
//        }
        usersRepository.save(user);
    }

    public User getUser(Long id) {
        return usersRepository.findById(id).get();
    }

    public List<User> getAllUsers() {
        return StreamSupport
                .stream(usersRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(User::getLastName))
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        usersRepository.deleteById(id);
    }
}
