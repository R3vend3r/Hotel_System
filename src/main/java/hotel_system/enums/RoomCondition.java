package hotel_system.enums;

import lombok.Getter;

@Getter
public enum RoomCondition {
    ON_REPAIR("На ремонте"),
    READY("Готов"),
    CLEANING_REQUIRED("На обслуживание");

    private String value;

    RoomCondition(String displayName) {
        this.value = displayName;
    }

    public static RoomCondition fromValue(String value) {
        for (RoomCondition status : RoomCondition.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Неизвестный статус заказа: " + value);
    }
}