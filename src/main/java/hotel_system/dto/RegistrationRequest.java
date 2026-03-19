package hotel_system.dto;

public record RegistrationRequest(
        String username,
        String password,
        String name,
        String surname
) {}
