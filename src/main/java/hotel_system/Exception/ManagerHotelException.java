package hotel_system.Exception;

public class ManagerHotelException extends RuntimeException {
    public ManagerHotelException(String message, Throwable cause) {
        super(message, cause);
    }
    public ManagerHotelException(String message) {
        super(message);
    }
}

