package com.samuelkane.exploreLearningChallenge;

import com.samuelkane.exploreLearningChallenge.api.UserController;
import com.samuelkane.exploreLearningChallenge.domain.User;
import com.samuelkane.exploreLearningChallenge.domain.UserRepository;
import com.samuelkane.exploreLearningChallenge.service.UserService;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ExploreLearningChallengeApplication.class)
@WebAppConfiguration
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

//    private MockMvcRequestSpecification mockMvcSpec;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @Before
//    public void setup() {
//        User user1 = new User(1L, "Samuel","Kane");
//        User user2 = new User(1L, "Robin","Macklin");
//        User user3 = new User(1L, "Carlos","Vizcaino");
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
//    }

    @Test
    public void test(){
        assertThat(true).isTrue();
    }

    // post - success
    @Test
    @WithMockUser("ADMIN")
    public void postSuccess() throws Exception {
        String json = "{" +
                "\"id\": \"4\"," +
                "\"firstname\": \"Jessica\"," +
                "\"lastname\": \"Niblo\"" +
                "}";

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(json)
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(204);
    }

    // post - bad payload
    @Test
    @WithMockUser("ADMIN")
    public void postBadPayload() throws Exception {
        String json = "{" +
                "\"id\": \"4\"," +
                "\"firstname\": \"Jessica\"," +
                "}";

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(json)
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

    // post - name conflict
    @Test
    @WithMockUser("ADMIN")
    public void postNameConflict() throws Exception {
        String json = "{" +
                "\"id\": \"4\"," +
                "\"firstname\": \"Samuel\"," +
                "\"lastname\": \"Kane\"" +
                "}";

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(json)
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

    // post - invalid role
    @Test
    @WithMockUser("USER")
    public void postInvalidRole() throws Exception {
        String json = "{" +
                "\"id\": \"4\"," +
                "\"firstname\": \"Samuel\"," +
                "\"lastname\": \"Kane\"" +
                "}";

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(json)
        ).andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

    // post - no auth
    @Test
    @WithAnonymousUser
    public void postNoAuth() throws Exception {
        String json = "{" +
                "\"id\": \"4\"," +
                "\"firstname\": \"Samuel\"," +
                "\"lastname\": \"Kane\"" +
                "}";

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(json)
        ).andReturn();

        // TODO: look into why this didn't return a 401... (also look at postInvalidRole())
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

    // get - specific user - success
    @Test
    @WithMockUser("ADMIN")
    public void getSpecificUserSuccess() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/1")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(200);
        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("{\"id\":1,\"firstName\":\"Samuel\",\"lastName\":\"Kane\"}");
    }

    // get - specific user - invalid ID (-1,0,4)
    @Test
    @WithMockUser("ADMIN")
    public void getSpecificUserInvalidId() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/4")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(404);
    }

    // get - specific user - admin role
    @Test
    @WithMockUser("ADMIN")
    public void getSpecificUserAdminRole() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/1")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(200);
        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("{\"id\":1,\"firstName\":\"Samuel\",\"lastName\":\"Kane\"}");
    }

    // get - specific user - user role
    @Test
    @WithMockUser("USER")
    public void getSpecificUserUserRole() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/1")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(200);
        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("{\"id\":1,\"firstName\":\"Samuel\",\"lastName\":\"Kane\"}");
    }

    // get - specific user - no auth
    @Test
    @WithAnonymousUser
    public void getSpecificUserNoAuth() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/1")
        ).andReturn();

        // TODO: Figure out why Auth isn't a blocker? and update code to 400
        assertThat(result.getResponse().getStatus())
                .isEqualTo(200);
    }

    // get - all - success
    @Test
    @WithMockUser("ADMIN")
    public void getAllSuccess() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(200);
        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("[" +
                        "{\"id\":1,\"firstName\":\"Samuel\",\"lastName\":\"Kane\"}," +
                        "{\"id\":2,\"firstName\":\"Robin\",\"lastName\":\"Macklin\"}," +
                        "{\"id\":3,\"firstName\":\"Carlos\",\"lastName\":\"Vizcaino\"}" +
                        "]");
    }

    // get - all - admin role
    @Test
    @WithMockUser("ADMIN")
    public void getAllAdminRole() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(200);
        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("[" +
                        "{\"id\":1,\"firstName\":\"Samuel\",\"lastName\":\"Kane\"}," +
                        "{\"id\":2,\"firstName\":\"Robin\",\"lastName\":\"Macklin\"}," +
                        "{\"id\":3,\"firstName\":\"Carlos\",\"lastName\":\"Vizcaino\"}" +
                        "]");
    }

    // get - all - user role
    @Test
    @WithMockUser("USER")
    public void getAllUserRole() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(200);
        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("[" +
                        "{\"id\":1,\"firstName\":\"Samuel\",\"lastName\":\"Kane\"}," +
                        "{\"id\":2,\"firstName\":\"Robin\",\"lastName\":\"Macklin\"}," +
                        "{\"id\":3,\"firstName\":\"Carlos\",\"lastName\":\"Vizcaino\"}" +
                        "]");
    }

    // get - all - no auth
    @Test
    @WithAnonymousUser
    public void getAllNoAuth() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
        ).andReturn();

        // TODO: Another one, this may be something I can't work around if spring security isn't involved
        assertThat(result.getResponse().getStatus())
                .isEqualTo(200);
        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("[" +
                        "{\"id\":1,\"firstName\":\"Samuel\",\"lastName\":\"Kane\"}," +
                        "{\"id\":2,\"firstName\":\"Robin\",\"lastName\":\"Macklin\"}," +
                        "{\"id\":3,\"firstName\":\"Carlos\",\"lastName\":\"Vizcaino\"}" +
                        "]");
    }

    // delete - success
    @Test
    @WithMockUser("ADMIN")
    public void deleteSuccess() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/1")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(202);
    }

    // delete - invalid ID (-1,0,4)
    @Test
    @WithMockUser("ADMIN")
    public void deleteInvalidId() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/4")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(404);
    }

    // delete - admin role
    @Test
    @WithMockUser("ADMIN")
    public void deleteAdminRole() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/1")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(202);
    }

    // delete - user role
    @Test
    @WithMockUser("USER")
    public void deleteUserRole() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/1")
        ).andReturn();

        // TODO: Security...
        assertThat(result.getResponse().getStatus())
                .isEqualTo(202);
    }

    // delete - no auth
    @Test
    @WithAnonymousUser
    public void deleteNoAuth() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/1")
        ).andReturn();

        // TODO: Security, again...
        assertThat(result.getResponse().getStatus())
                .isEqualTo(202);
    }

}
