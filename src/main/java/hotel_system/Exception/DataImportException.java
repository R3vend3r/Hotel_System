package hotel_system.Exception;

public class DataImportException extends RuntimeException {
    public DataImportException(String message, Exception e) {
        super(message);
    }
}
