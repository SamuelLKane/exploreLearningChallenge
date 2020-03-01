package com.samuelkane.exploreLearningChallenge;

import com.samuelkane.exploreLearningChallenge.domain.User;
import com.samuelkane.exploreLearningChallenge.domain.UserRepository;
import com.samuelkane.exploreLearningChallenge.exception.UserAlreadyExistsException;
import com.samuelkane.exploreLearningChallenge.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ExploreLearningChallengeApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // add user - success
    @Test
    public void testAddUserSuccess() throws Exception{
        String json = "{\n" +
                "\t\"id\": \"4\",\n" +
                "\t\"firstname\": \"Jessica\",\n" +
                "\t\"lastname\": \"Niblo\"\n" +
                "}";

        userService.addUser(json);
        User user = userRepository.findById(4L).get();
        assertThat(user.getId()).isEqualTo(4L);
        assertThat(user.getFirstName()).isEqualTo("Jessica");
        assertThat(user.getLastName()).isEqualTo("Niblo");
    }

    // add user - missing first name
    @Test(expected = JSONException.class)
    public void testAddUserMissingFirstName() throws Exception{
        String json = "{\n" +
                "\t\"id\": \"4\",\n" +
                "\t\"lastname\": \"Niblo\"\n" +
                "}";

        userService.addUser(json);
    }

    // add user - missing last name
    @Test(expected = JSONException.class)
    public void testAddUserMissingLastName() throws Exception{
        String json = "{\n" +
                "\t\"id\": \"4\",\n" +
                "\t\"firstname\": \"Jessica\",\n" +
                "}";

        userService.addUser(json);
    }

    // add user - missing id (success)
    @Test
    public void testAddUserMissingId() throws Exception{
        String json = "{\n" +
                "\t\"firstname\": \"Jessica\",\n" +
                "\t\"lastname\": \"Niblo\"\n" +
                "}";

        userService.addUser(json);
        User user = userRepository.findById(4L).get();
        assertThat(user.getId()).isEqualTo(4L);
        assertThat(user.getFirstName()).isEqualTo("Jessica");
        assertThat(user.getLastName()).isEqualTo("Niblo");
    }

    // add user - already exists
    @Test(expected = UserAlreadyExistsException.class)
    public void testAddUserAlreadyExists() throws Exception{
        String json = "{\n" +
                "\t\"id\": \"1\",\n" +
                "\t\"firstname\": \"Samuel\",\n" +
                "\t\"lastname\": \"Kane\"\n" +
                "}";

        userService.addUser(json);
    }

    // get - specific user - success
    @Test
    public void testGetUserSuccess() {
        User user = userService.getUser(1L);
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getFirstName()).isEqualTo("Samuel");
        assertThat(user.getLastName()).isEqualTo("Kane");
    }

    // get - specific user - invalid id (-1,0,4)
    @Test
    public void testGetUserInvalidId() {
        assertThat(userService.getUser(-1L)).isNull();
        assertThat(userService.getUser(0L)).isNull();
        assertThat(userService.getUser(4L)).isNull();
    }

    // get - all users - success
    @Test
    public void testGetAllUsers() {
        Iterable<User> users = userService.getAllUsers();
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        ids.add(3L);
        ArrayList<String> firstNames = new ArrayList<>();
        firstNames.add("Samuel");
        firstNames.add("Robin");
        firstNames.add("Carlos");
        ArrayList<String> lastNames = new ArrayList<>();
        lastNames.add("Kane");
        lastNames.add("Macklin");
        lastNames.add("Vizcaino");
        users.forEach( u -> {
            assertThat(u.getId()).isIn(ids);
            assertThat(u.getFirstName()).isIn(firstNames);
            assertThat(u.getLastName()).isIn(lastNames);
        });
    }

    // delete - success
    @Test
    public void deleteSuccess() {
        userService.deleteUser(1L);
        ArrayList<Long> id = new ArrayList<>();
        id.add(1L);
        userService.getAllUsers().forEach(
                u -> assertThat(u.getId()).isNotIn(id));
    }

    // delete - invalid id (-1,0,4)
    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteInvalidId_Negative1() {
        userService.deleteUser(-1L);
    }

    // delete - invalid id (-1,0,4)
    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteInvalidId_Zero() {
        userService.deleteUser(0L);
    }

    // delete - invalid id (-1,0,4)
    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteInvalidId_Four() {
        userService.deleteUser(4L);
    }

}
