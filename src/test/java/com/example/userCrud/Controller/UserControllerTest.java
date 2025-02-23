package com.example.userCrud.Controller;

import com.example.userCrud.Dto.*;
import com.example.userCrud.Entity.User;
import com.example.userCrud.Hash.BCrypt;
import com.example.userCrud.Repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("Akmal");
        request.setEmail("Akmal@gmail.com");
        request.setPassword("Akmal123@");

        mockMvc.perform(
                post("/users/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            web_response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<UserResponse>>() {
            });
            assertEquals("Success",response.getMessage());
            UserResponse userData = response.getData();
            assertNotNull(userData);
            assertEquals("Akmal", userData.getUsername());
            assertEquals("Akmal@gmail.com", userData.getEmail());
            assertEquals("Akmal", userData.getCreated_by());
            assertEquals("There is no update yet", userData.getUpdated_by());
        });
    }

    @Test
    void testCreateUserNull() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("");
        request.setEmail("");
        request.setPassword("");

        mockMvc.perform(
                post("/users/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            web_response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<UserResponse>>() {
            });
            assertNotNull(response.getError());
            assertNull(response.getData());
        });
    }

    @Test
    void testCreateUserUsernameAlreadyExist() throws Exception {
        CreateUserRequest initialRequest = new CreateUserRequest();
        initialRequest.setUsername("Akmal");
        initialRequest.setEmail("Akmal@gmail.com");
        initialRequest.setPassword("Akmal123@");

        // Perform the first create user request
        mockMvc.perform(
                post("/users/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(initialRequest))
        ).andExpect(status().isOk());

        // Step 2: Attempt to create another user with the same username
        CreateUserRequest duplicateRequest = new CreateUserRequest();
        duplicateRequest.setUsername("Akmal"); // Same username to trigger the "already exists" error
        duplicateRequest.setEmail("akmal2@gmail.com");
        duplicateRequest.setPassword("Akmal456@");


        mockMvc.perform(
                post("/users/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            // Deserialize the response into a Map
            Map<String, Object> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>() {
            });

            Map<String, Object> errorMap = (Map<String, Object>) responseMap.get("error");

            String errorDescription = (String) errorMap.get("Error Description");

            String errorMessage = (String) responseMap.get("message");

            String data = (String) responseMap.get("data");

            assertNull(data);
            assertNotNull(errorMessage);
            assertEquals("Error", errorMessage);
            assertNotNull(errorDescription);
            assertEquals("Username already exists", errorDescription);
        });
    }

    @Test
    void testCreateUserEmailWrongFormat() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("Akmal");
        request.setEmail("Akmal2121");
        request.setPassword("Akmal123@");
        mockMvc.perform(
                post("/users/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            web_response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<UserResponse>>() {
            });
            assertNotNull(response.getError());
            assertNull(response.getData());
        });
    }

    @Test
    void testCreateUserPasswordWrongFormat() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("Akmal");
        request.setEmail("Akmal@example.com");
        request.setPassword("Akmal");
        mockMvc.perform(
                post("/users/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            web_response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<UserResponse>>() {
            });
            assertNotNull(response.getError());
            assertNull(response.getData());
        });
    }

    @Test
    void testCreateUserWithRoleSuccess() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("Akmal");
        request.setEmail("Akmal@example.com");
        request.setPassword("Akmal12@2");
        request.setIdRoles(1L);
        mockMvc.perform(
                post("/users/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            web_response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<UserResponse>>() {
            });
            assertNotNull(response.getData());
            assertEquals("Success",response.getMessage());
            UserResponse userData = response.getData();
            assertNotNull(userData);
            assertEquals("Akmal", userData.getUsername());
            assertEquals("Akmal@example.com", userData.getEmail());
            assertEquals("Akmal", userData.getCreated_by());
        });
    }

    @Test
    void testAddRoleSuccess() throws Exception{
        User user = new User();
        user.setUsername("AkmalM1");
        user.setPassword(BCrypt.hashpw("AkmalM1@", BCrypt.gensalt()));
        user.setEmail("AkmalM1@example.com");
        userRepository.save(user);

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("AkmalM1");
        authRequest.setPassword("AkmalM1@");

        AtomicReference<String> token = new AtomicReference<>("");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    web_response<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<TokenResponse>>() {
                    });
                    token.set(response.getData().getToken());
                }
        );
        AddRoleRequest request = new AddRoleRequest();
        request.setRoleId(1L);
        mockMvc.perform(
                patch("/users/addRole")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    web_response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<UserResponse>>() {
                    });
                    assertNotNull(response.getData());
                    assertEquals("Success",response.getMessage());
                    assertNotNull(response.getData());
                    assertEquals("AkmalM1", response.getData().getUsername());
                    assertEquals("AkmalM1@example.com", response.getData().getEmail());
                    assertNotEquals("There is no update yet",response.getData().getUpdated_by());
                    assertNotNull(response.getData().getRoles());
                }
        );
    }

    @Test
    void testAddRoleNotFound() throws Exception{
        User user = new User();
        user.setUsername("AkmalM1");
        user.setPassword(BCrypt.hashpw("AkmalM1@", BCrypt.gensalt()));
        user.setEmail("AkmalM1@example.com");
        userRepository.save(user);

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("AkmalM1");
        authRequest.setPassword("AkmalM1@");

        AtomicReference<String> token = new AtomicReference<>("");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    web_response<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<TokenResponse>>() {
                    });
                    token.set(response.getData().getToken());
                }
        );
        AddRoleRequest request = new AddRoleRequest();
        request.setRoleId(50L);
        mockMvc.perform(
                patch("/users/addRole")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+ token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isNotFound()
        ).andDo(
                result -> {
                    web_response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<UserResponse>>() {
                    });
                    Map<String, Object> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>() {
                    });

                    Map<String, Object> errorMap = (Map<String, Object>) responseMap.get("error");
                    String errorDescription = (String) errorMap.get("Error Description");
                    assertNull(response.getData());
                    assertEquals("Role Not Found", errorDescription);
                    assertEquals("Error",response.getMessage());
                }
        );
    }

    @Test
    void testAddRoleUserIsUnauthorized() throws Exception {
        // Create a new user
        User user = new User();
        user.setUsername("AkmalM1");
        user.setPassword(BCrypt.hashpw("AkmalM1@", BCrypt.gensalt()));
        user.setEmail("AkmalM1@example.com");
        userRepository.save(user);

        // Mark user as deleted
        user.set_deleted(true);
        userRepository.save(user);

        // Create the request for adding a role
        AddRoleRequest request = new AddRoleRequest();
        request.setRoleId(1L);

        // Perform the request and expect Unauthorized status
        mockMvc.perform(
                patch("/users/addRole")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            // Parse the response as a Map
            Map<String, Object> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>() {
            });

            // Extract the error message
            String errorMessage = (String) responseMap.get("error");

            // Assert that the error message matches the expected message
            assertNotNull(errorMessage);
            assertEquals("You're not authorized to access this resource.", errorMessage);
        });
    }

    @Test
    void testGetUserSuccess() throws Exception {
        // Create a new user
        User user = new User();
        user.setUsername("AkmalM1");
        user.setPassword(BCrypt.hashpw("AkmalM1@", BCrypt.gensalt()));
        user.setEmail("AkmalM1@example.com");
        userRepository.save(user);

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("AkmalM1");
        authRequest.setPassword("AkmalM1@");

        AtomicReference<String> token = new AtomicReference<>("");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    web_response<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<TokenResponse>>() {
                    });
                    token.set(response.getData().getToken());
                }
        );

        mockMvc.perform(
                get("/users/get")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            // Parse the response as a Map
            List<UserResponse> responseList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<UserResponse>>() {
            });
            assertNotNull(responseList);
            assertEquals(1, responseList.size());  // Assuming you're fetching a list with one user
            UserResponse userResponse = responseList.get(0);
            assertEquals("AkmalM1", userResponse.getUsername());
            assertEquals("AkmalM1@example.com", userResponse.getEmail());
        });
    }

    @Test
    void testGetUserIsUnauthorized() throws Exception {
        // Create a new user
        User user = new User();
        user.setUsername("AkmalM1");
        user.setPassword(BCrypt.hashpw("AkmalM1@", BCrypt.gensalt()));
        user.setEmail("AkmalM1@example.com");
        userRepository.save(user);

        // Perform the request and expect Unauthorized status
        mockMvc.perform(
                get("/users/get")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization"," ")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            // Parse the response as a Map
            Map<String, Object> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>() {
            });
            // Extract the error message
            String errorMessage = (String) responseMap.get("error");

            // Assert that the error message matches the expected message
            assertNotNull(errorMessage);
            assertEquals("You're not authorized to access this resource.", errorMessage);
        });
    }

    @Test
    void testGetUserWithIdSuccess() throws Exception {
        // Create a new user
        User user = new User();
        user.setUsername("AkmalM1");
        user.setPassword(BCrypt.hashpw("AkmalM1@", BCrypt.gensalt()));
        user.setEmail("AkmalM1@example.com");
        userRepository.save(user);

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("AkmalM1");
        authRequest.setPassword("AkmalM1@");

        AtomicReference<String> token = new AtomicReference<>("");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    web_response<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<TokenResponse>>() {
                    });
                    token.set(response.getData().getToken());
                }
        );

        mockMvc.perform(
                get("/users/get/105")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            // Parse the response as a Map
            Map<String, Object> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>() {
            });
            web_response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<UserResponse>>() {
            });
            assertNotNull(response.getData());
            assertEquals("Success",response.getMessage());
            assertNotNull(response.getData());
            assertEquals("AkmalM1", response.getData().getUsername());
            assertEquals("AkmalM1@example.com", response.getData().getEmail());
        });
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        // Create a new user
        User user = new User();
        user.setUsername("AkmalM1");
        user.setPassword(BCrypt.hashpw("AkmalM1@", BCrypt.gensalt()));
        user.setEmail("AkmalM1@example.com");
        userRepository.save(user);

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("AkmalM1");
        authRequest.setPassword("AkmalM1@");

        AtomicReference<String> token = new AtomicReference<>("");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    web_response<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<TokenResponse>>() {
                    });
                    token.set(response.getData().getToken());
                }
        );

        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("AkmalM1Update");
        request.setEmail("AkmalM1Update@example.com");

        mockMvc.perform(
                patch("/users/update/111")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            // Parse the response as a Map
            Map<String, Object> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>() {
            });
            web_response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<UserResponse>>() {
            });
            assertNotNull(response.getData());
            assertEquals("Success",response.getMessage());
            assertNotNull(response.getData());
            assertEquals("AkmalM1Update", response.getData().getUsername());
            assertEquals("AkmalM1Update@example.com", response.getData().getEmail());
        });
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        // Create a new user
        User user = new User();
        user.setUsername("AkmalM1");
        user.setPassword(BCrypt.hashpw("AkmalM1@", BCrypt.gensalt()));
        user.setEmail("AkmalM1@example.com");
        userRepository.save(user);

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("AkmalM1");
        authRequest.setPassword("AkmalM1@");

        AtomicReference<String> token = new AtomicReference<>("");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    web_response<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<TokenResponse>>() {
                    });
                    token.set(response.getData().getToken());
                }
        );

        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("AkmalM1Update");
        request.setEmail("AkmalM1Update@example.com");

        mockMvc.perform(
                patch("/users/update/111")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            // Parse the response as a Map
            Map<String, Object> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>() {
            });
            web_response<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<UserResponse>>() {
            });

            Map<String, Object> errorMap = (Map<String, Object>) responseMap.get("error");

            String errorDescription = (String) errorMap.get("Error Description");
            assertNull(response.getData());
            assertEquals("Error",response.getMessage());
            assertEquals("User not found", errorDescription);
        });
    }
}