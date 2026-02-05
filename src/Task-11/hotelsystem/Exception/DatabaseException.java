package hotelsystem.Exception;

import java.sql.SQLException;
import java.util.Optional;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }

    public boolean isConstraintViolation() {
        return getCause() instanceof SQLException &&
                ((SQLException) getCause()).getSQLState().startsWith("23");
    }

    public Optional<String> getSQLState() {
        return getCause() instanceof SQLException ?
                Optional.of(((SQLException) getCause()).getSQLState()) :
                Optional.empty();
    }
}