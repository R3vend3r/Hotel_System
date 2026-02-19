package hotel_system.Utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class HotelConfig {

    @Value("${hotel.room.status.change.enabled:true}")
    private boolean roomStatusChangeEnabled;

    @Value("${hotel.room.history.entries.max:3}")
    private int maxHistoryEntries;

    @Value("${hotel.database.file:hotel_db.json}")
    private String databaseFilePath;

    @Value("${hotel.auto.save.enabled:true}")
    private boolean autoSaveEnabled;
}