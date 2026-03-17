package hotel_system.service;

import hotel_system.dto.JwtResponse;
import hotel_system.dto.LoginRequest;
import hotel_system.dto.RegistrationRequest;
import hotel_system.enums.Role;
import hotel_system.model.entity.Client;
import hotel_system.model.entity.User;
import hotel_system.dao.ClientDAO;
import hotel_system.dao.UserDAO;
import hotel_system.security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserDAO userDAO;
    private final ClientDAO customerDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthService(UserDAO userRepository,
                       ClientDAO customerDAO,
                       PasswordEncoder passwordEncoder, JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService) {
        this.userDAO = userRepository;
        this.customerDAO = customerDAO;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Transactional
    public void register(RegistrationRequest request) {
        try {
            User user = new User();
            user.setLogin(request.username());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setRole(Role.ROLE_USER);
            user.setEnabled(true);
            user.setCreatedAt(LocalDateTime.now());
            userDAO.create(user);

            Client client = new Client(
                    request.name(),
                    request.surname(),
                    user
            );
            customerDAO.create(client);
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    public JwtResponse login(LoginRequest request) {
        var user = customUserDetailsService.loadUserByUsername(request.username());

        if (user == null || !checkPassword(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid login or password");
        }

        String token = jwtUtils.generateJwtToken(user);
        return new JwtResponse(token);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}