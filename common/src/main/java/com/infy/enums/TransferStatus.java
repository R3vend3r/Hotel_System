package com.infy.enums;

public enum TransferStatus {
    SUCCESS("готово"),
    FAILED("завершилось с ошибкой");

    private String value;

    TransferStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TransferStatus fromValue(String value) {
        for (TransferStatus status : TransferStatus.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Неизвестный статус: " + value);
    }
}
