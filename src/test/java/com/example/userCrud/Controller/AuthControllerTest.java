package com.example.userCrud.Controller;

import com.example.userCrud.Dto.AuthRequest;
import com.example.userCrud.Dto.TokenResponse;
import com.example.userCrud.Dto.UserResponse;
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
import com.example.userCrud.Dto.web_response;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void TestLoginFailedUsernameNotFound() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("Akmal");
        request.setPassword("Akmal123@");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            web_response<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<TokenResponse>>() {
            });
            Map<String, Object> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>() {
            });

            Map<String, Object> errorMap = (Map<String, Object>) responseMap.get("error");

            String errorDescription = (String) errorMap.get("Error Description");

            String errorMessage = (String) responseMap.get("message");

            String data = (String) responseMap.get("data");

            assertEquals("Username or password wrong", errorDescription);
            assertEquals("Error", errorMessage);
            assertNull(data);
            assertNotNull(response.getError());
        });
    }

    @Test
    void TestLoginFailedUserPasswordWrong() throws Exception{
        User user = new User();
        user.setUsername("AkmalM1");
        user.setPassword(BCrypt.hashpw("AkmalM1@", BCrypt.gensalt()));
        user.setEmail("AkmalM1@example.com");
        userRepository.save(user);

        AuthRequest request = new AuthRequest();
        request.setUsername("AkmalM1");
        request.setPassword("AkmalMK2@");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(
                result -> {
                    web_response<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<TokenResponse>>() {
                    });
                    Map<String, Object> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>() {
                    });

                    Map<String, Object> errorMap = (Map<String, Object>) responseMap.get("error");
                    String errorDescription = (String) errorMap.get("Error Description");

                    assertNull(response.getData());
                    assertNotNull(response.getError());
                    assertEquals("Username or password wrong", errorDescription);
                    assertEquals("Error", response.getMessage());
                }
        );
    }

    @Test
    void TestLoginFailedUserIsDeleted() throws Exception{
        User user = new User();
        user.setUsername("AkmalM1");
        user.setPassword(BCrypt.hashpw("AkmalM1@", BCrypt.gensalt()));
        user.setEmail("AkmalM1@example.com");
        user.set_deleted(true);
        userRepository.save(user);

        AuthRequest request = new AuthRequest();
        request.setUsername("AkmalM1");
        request.setPassword("AkmalM1@");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(
                result -> {
                    web_response<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<TokenResponse>>() {
                    });
                    Map<String, Object> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>() {
                    });

                    Map<String, Object> errorMap = (Map<String, Object>) responseMap.get("error");
                    String errorDescription = (String) errorMap.get("Error Description");

                    assertNull(response.getData());
                    assertNotNull(response.getError());
                    assertEquals("User Is Deleted from our World", errorDescription);
                    assertEquals("Error", response.getMessage());
                }
        );
    }

    @Test
    void TestLoginSuccess() throws Exception{
        User user = new User();
        user.setUsername("AkmalM1");
        user.setPassword(BCrypt.hashpw("AkmalM1@", BCrypt.gensalt()));
        user.setEmail("AkmalM1@example.com");
        userRepository.save(user);

        AuthRequest request = new AuthRequest();
        request.setUsername("AkmalM1");
        request.setPassword("AkmalM1@");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(
                result -> {
                    web_response<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<web_response<TokenResponse>>() {
                    });
                    Map<String, Object> responseMap = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>() {
                    });
                    assertNotNull(response.getData());
                    assertNotNull(response.getData().getToken());
                    assertNull(response.getError());
                    assertEquals("AkmalM1",response.getData().getUsername());
                    assertEquals("AkmalM1@example.com",response.getData().getEmail());
                    assertEquals("Success", response.getMessage());
                }
        );
    }
}