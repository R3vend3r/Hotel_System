package hotel_system.enums;

import lombok.Getter;

@Getter
public enum RoomType {
    STANDARD("Стандарт"),
    DELUXE("Люкс"),
    PRESIDENTIAL("Презедентский"),
    FAMILY("Семейный"),
    BUSINESS("Бизнесс-номер");

    private String value;

    RoomType(String value) {
        this.value = value;
    }

    public static RoomType fromValue(String value) {
        for (RoomType status : RoomType.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Неизвестный статус заказа: " + value);
    }
}
