package hotel_system.service;

import hotel_system.dao.UserDAO;
import hotel_system.enums.Role;
import hotel_system.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_shouldReturnUserDetailsWhenUserExists() {

        User user = new User();
        user.setLogin("john_doe");
        user.setPassword("encoded_password");
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);

        when(userDAO.findByLogin("john_doe")).thenReturn(user);

        UserDetails result = customUserDetailsService.loadUserByUsername("john_doe");

        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
        assertEquals("encoded_password", result.getPassword());
        assertTrue(result.isEnabled());
        verify(userDAO, times(1)).findByLogin("john_doe");
    }

    @Test
    void loadUserByUsername_shouldThrowExceptionWhenUserNotFound() {
        when(userDAO.findByLogin("nonexistent")).thenReturn(null);

        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("nonexistent")
        );

        verify(userDAO, times(1)).findByLogin("nonexistent");
    }

    @Test
    void loadUserByUsername_shouldSetEnabledFalseWhenUserDisabled() {
        User user = new User();
        user.setLogin("disabled_user");
        user.setPassword("encoded_password");
        user.setRole(Role.ROLE_USER);
        user.setEnabled(false);

        when(userDAO.findByLogin("disabled_user")).thenReturn(user);

        UserDetails result = customUserDetailsService.loadUserByUsername("disabled_user");

        assertNotNull(result);
        assertFalse(result.isEnabled());
        verify(userDAO, times(1)).findByLogin("disabled_user");
    }
}