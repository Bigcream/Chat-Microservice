package com.message.chatservice.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final long ONE_DAY = 86400;
    private static final long TWO_MINUTE = 120;

    private static final long ONE_HOUR = 3600;

    private static final long TWO_HOUR = 7200;
    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        long timeDifference = convertLcDateTimeToLong(currentTime) - convertLcDateTimeToLong(localDateTime);
        if(timeDifference < ONE_DAY){
            return DateTimeFormatter.ofPattern("HH:mm").format(localDateTime);
        }
        return DateTimeFormatter.ofPattern("MM-dd").format(localDateTime);
    }

    public static String formatTimeNotification(LocalDateTime localDateTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        long timeDifference = convertLcDateTimeToLong(currentTime) - convertLcDateTimeToLong(localDateTime);
        if (timeDifference < TWO_MINUTE) {
            return "a few moments ago";
        } else if (timeDifference > TWO_MINUTE && timeDifference < ONE_HOUR) {
            return (timeDifference / 60) + " minutes ago";
        } else if (timeDifference > ONE_HOUR && timeDifference < TWO_HOUR) {
            return "1 hour ago";
        } else if (timeDifference > TWO_HOUR && timeDifference < ONE_DAY) {
            return (timeDifference / 3600) + " hours ago";
        } else {
            return DateTimeFormatter.ofPattern("MM-dd").format(localDateTime);
        }
    }

    public static LocalDateTime today() {
        return LocalDateTime.now();
    }

    public static long convertLcDateTimeToLong(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }
}
