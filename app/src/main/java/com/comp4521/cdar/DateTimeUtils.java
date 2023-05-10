package com.comp4521.cdar;
import java.time.Duration;
import java.time.LocalDateTime;

public class DateTimeUtils {
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    public static Duration getDuration(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime);
    }
}
