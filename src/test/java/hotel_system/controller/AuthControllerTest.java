package hotel_system.controller;

import hotel_system.dto.JwtResponse;
import hotel_system.dto.LoginRequest;
import hotel_system.dto.RegistrationRequest;
import hotel_system.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void register_shouldReturnOkWhenRegistrationSuccess() throws Exception {
        RegistrationRequest request = new RegistrationRequest("john_doe", "password123", "John", "Doe");

        doNothing().when(authService).register(any(RegistrationRequest.class));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(authService, times(1)).register(any(RegistrationRequest.class));
    }

    @Test
    void register_shouldReturn500WhenServiceThrowsException() throws Exception {
        RegistrationRequest request = new RegistrationRequest("john_doe", "password123", "John", "Doe");

        doThrow(new RuntimeException("Registration failed")).when(authService).register(any(RegistrationRequest.class));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is5xxServerError());

        verify(authService, times(1)).register(any(RegistrationRequest.class));
    }

    @Test
    void login_shouldReturnJwtTokenWhenLoginSuccess() throws Exception {
        LoginRequest request = new LoginRequest("john_doe", "password123");
        String expectedToken = "jwt-token-123";
        JwtResponse response = new JwtResponse(expectedToken);

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        String result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(result.contains(expectedToken));

        verify(authService, times(1)).login(any(LoginRequest.class));
    }
    @Test
    void login_shouldReturn500WhenLoginFails() throws Exception {
        LoginRequest request = new LoginRequest("wrong_user", "wrong_password");

        when(authService.login(any(LoginRequest.class))).thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is5xxServerError());

        verify(authService, times(1)).login(any(LoginRequest.class));
    }
}