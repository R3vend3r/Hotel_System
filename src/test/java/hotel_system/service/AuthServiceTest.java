package hotel_system.service;

import hotel_system.dto.JwtResponse;
import hotel_system.dto.LoginRequest;
import hotel_system.dto.RegistrationRequest;
import hotel_system.model.entity.Client;
import hotel_system.model.entity.User;
import hotel_system.dao.ClientDAO;
import hotel_system.dao.UserDAO;
import hotel_system.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private ClientDAO clientDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldCreateUserAndClientSuccessfully() {
        RegistrationRequest request = new RegistrationRequest(
                "john_doe",
                "password123",
                "John",
                "Doe"
        );

        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");

        authService.register(request);

        verify(userDAO, times(1)).create(any(User.class));
        verify(clientDAO, times(1)).create(any(Client.class));
        verify(passwordEncoder, times(1)).encode("password123");
    }
    @Test
    void register_shouldThrowExceptionWhenUserCreationFails() {
        RegistrationRequest request = new RegistrationRequest(
                "john_doe",
                "password123",
                "John",
                "Doe"
        );

        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        doThrow(new RuntimeException("Database error")).when(userDAO).create(any(User.class));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.register(request)
        );

        assertTrue(exception.getMessage().contains("Registration failed"));
        verify(userDAO, times(1)).create(any(User.class));
        verify(clientDAO, never()).create(any(Client.class));
    }

    @Test
    void register_shouldThrowExceptionWhenClientCreationFails() {
        RegistrationRequest request = new RegistrationRequest(
                "john_doe",
                "password123",
                "John",
                "Doe"
        );

        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        doNothing().when(userDAO).create(any(User.class));
        doThrow(new RuntimeException("Client creation error")).when(clientDAO).create(any(Client.class));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.register(request)
        );

        assertTrue(exception.getMessage().contains("Registration failed"));
        verify(userDAO, times(1)).create(any(User.class));
        verify(clientDAO, times(1)).create(any(Client.class));
    }

    @Test
    void login_shouldReturnJwtResponseWhenCredentialsValid() {
        LoginRequest request = new LoginRequest("john_doe", "password123");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getPassword()).thenReturn("encoded_password");

        when(customUserDetailsService.loadUserByUsername("john_doe")).thenReturn(userDetails);
        when(passwordEncoder.matches(eq("password123"), eq("encoded_password"))).thenReturn(true);
        when(jwtUtils.generateJwtToken(userDetails)).thenReturn("jwt_token");

        JwtResponse result = authService.login(request);

        assertNotNull(result);
        assertEquals("jwt_token", result.token());

        verify(customUserDetailsService, times(1)).loadUserByUsername("john_doe");
        verify(passwordEncoder, times(1)).matches(eq("password123"), eq("encoded_password"));
        verify(jwtUtils, times(1)).generateJwtToken(userDetails);
    }

    @Test
    void login_shouldThrowExceptionWhenUserNotFound() {
        LoginRequest request = new LoginRequest("nonexistent", "password");

        when(customUserDetailsService.loadUserByUsername("nonexistent")).thenReturn(null);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );

        assertEquals("Invalid login or password", exception.getMessage());
        verify(customUserDetailsService, times(1)).loadUserByUsername("nonexistent");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtils, never()).generateJwtToken(any());
    }

    @Test
    void login_shouldThrowExceptionWhenPasswordIncorrect() {
        LoginRequest request = new LoginRequest("john_doe", "wrong_password");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getPassword()).thenReturn("encoded_password");

        when(customUserDetailsService.loadUserByUsername("john_doe")).thenReturn(userDetails);
        when(passwordEncoder.matches("wrong_password", "encoded_password")).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );

        assertEquals("Invalid login or password", exception.getMessage());
        verify(customUserDetailsService).loadUserByUsername("john_doe");
        verify(passwordEncoder).matches("wrong_password", "encoded_password");
        verify(jwtUtils, never()).generateJwtToken(any());
    }

    @Test
    void checkPassword_shouldReturnTrueWhenPasswordMatches() {
        String rawPassword = "password123";
        String encodedPassword = "encoded_password";

        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean result = authService.checkPassword(rawPassword, encodedPassword);

        assertTrue(result);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }

    @Test
    void checkPassword_shouldReturnFalseWhenPasswordDoesNotMatch() {
        String rawPassword = "wrong_password";
        String encodedPassword = "encoded_password";

        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        boolean result = authService.checkPassword(rawPassword, encodedPassword);

        assertFalse(result);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }
}