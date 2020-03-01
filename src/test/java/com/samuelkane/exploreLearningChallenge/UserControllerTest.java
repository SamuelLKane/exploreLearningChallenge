package com.samuelkane.exploreLearningChallenge;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    // post - success
    @Test
    @WithMockUser(username = "admin", password = "{noop}password",roles = {"ADMIN"})
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
    @WithMockUser(username = "admin", password = "{noop}password",roles = {"ADMIN"})
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
    @WithMockUser(username = "admin", password = "{noop}password",roles = {"ADMIN"})
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
    @WithMockUser(username = "user", password = "{noop}password")
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

        assertThat(result.getResponse().getStatus()).isEqualTo(403);
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
        assertThat(result.getResponse().getStatus()).isEqualTo(401);
    }

    // get - specific user - success
    @Test
    @WithMockUser(username = "admin", password = "{noop}password",roles = {"ADMIN"})
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
    @WithMockUser(username = "admin", password = "{noop}password",roles = {"ADMIN"})
    public void getSpecificUserInvalidId() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/4")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(404);

        result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/0")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(404);

        result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/-1")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(404);
    }

    // get - specific user - admin role
    @Test
    @WithMockUser(username = "admin", password = "{noop}password",roles = {"ADMIN"})
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
    @WithMockUser(username = "user", password = "{noop}password")
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

        assertThat(result.getResponse().getStatus())
                .isEqualTo(401);
    }

    // get - all - success
    @Test
    @WithMockUser(username = "admin", password = "{noop}password",roles = {"ADMIN"})
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
    @WithMockUser(username = "admin", password = "{noop}password",roles = {"ADMIN"})
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
    @WithMockUser(username = "user", password = "{noop}password")
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

        assertThat(result.getResponse().getStatus())
                .isEqualTo(401);
    }

    // delete - success
    @Test
    @WithMockUser(username = "admin", password = "{noop}password",roles = {"ADMIN"})
    public void deleteSuccess() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/1")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(202);
    }

    // delete - invalid ID (-1,0,4)
    @Test
    @WithMockUser(username = "admin", password = "{noop}password",roles = {"ADMIN"})
    public void deleteInvalidId() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/4")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(404);

        result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/0")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(404);

        result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/-1")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(404);
    }

    // delete - admin role
    @Test
    @WithMockUser(username = "admin", password = "{noop}password",roles = {"ADMIN"})
    public void deleteAdminRole() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/1")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(202);
    }

    // delete - user role
    @Test
    @WithMockUser(username = "user", password = "{noop}password")
    public void deleteUserRole() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/1")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(403);
    }

    // delete - no auth
    @Test
    @WithAnonymousUser
    public void deleteNoAuth() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/1")
        ).andReturn();

        assertThat(result.getResponse().getStatus())
                .isEqualTo(401);
    }

}
