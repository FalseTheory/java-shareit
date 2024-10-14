package ru.practicum.shareit.booking;

public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    PAST,
    FUTURE,
    CURRENT,
    ALL;

    static BookingStatus fromString(String status) {
        for (BookingStatus val : BookingStatus.values()) {
            if (val.name().equals(status)) {
                return val;
            }
        }
        return null;
    }
}
