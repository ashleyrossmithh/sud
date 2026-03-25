package sud.core.util;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalDateUtils {
    public static final LocalDateTime MAX_DATE_TIME = LocalDateTime.of(9999, 12, 31, 0, 0, 0);
    public static final LocalDate MAX_DATE = LocalDate.of(9999, 12, 31);
    public static final LocalDate MIN_DATE = LocalDate.of(1900, 1, 1);
    public static final Pattern pattDateDDMMYYYY = Pattern.compile("\\b(\\d{1,2}[./-]?\\d{1,2}[./-]?\\d{4})\\b");
    public static final Pattern pattDateDDMMYY = Pattern.compile("\\b(\\d{1,2}[./-]?\\d{1,2}[./-]?\\d{2})\\b");
    public static final Pattern pattDateDDMMYYYYTIME = Pattern.compile("\\b(\\d{1,2}[./-]?\\d{1,2}[./-]?\\d{4} +\\d{1,2}:\\d{1,2}:\\d{1,2})\\b");
    public static final Pattern pattDateDDMMYYTIME = Pattern.compile("\\b(\\d{1,2}[./-]?\\d{1,2}[./-]?\\d{2} +\\d{1,2}:\\d{1,2}:\\d{1,2})\\b");
    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String DATE_FORMAT_SHORT = "dd.MM.yy";
    public static final String DATE_SLASH_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_LONG_FORMAT = "dd.MM.yyyy HH:mm:ss";
    public static final String DATE_TIME_SHORT_FORMAT = "dd.MM.yyyy HH:mm";
    public static final String XML_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String XML_DATE_FORMAT = "yyyy-MM-dd";

    private LocalDateUtils() {
    }

    public static LocalDate now() {
        return LocalDate.now();
    }

    public static LocalDate max(LocalDate first, LocalDate second) {
        if (first != null && second != null) {
            return first.isBefore(second) ? second : first;
        } else {
            return first != null ? first : second;
        }
    }

    public static LocalDate min(LocalDate first, LocalDate second) {
        if (first != null && second != null) {
            return first.isAfter(second) ? second : first;
        } else {
            return first != null ? first : second;
        }
    }

    public static LocalDate of(Date value) {
        return of2((Date)Objects.requireNonNull(value)).toLocalDate();
    }

    public static LocalDateTime of2(Date value) {
        if (value == null) {
            return null;
        } else {
            Instant instant = Instant.ofEpochMilli(value.getTime());
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate startOfMonth(LocalDate forDate) {
        return forDate.withDayOfMonth(1);
    }

    public static LocalDate endOfMonth(LocalDate forDate) {
        return forDate.withDayOfMonth(forDate.lengthOfMonth());
    }

    public static LocalDate parseLocalDate(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return parseLocalDate(date, formatter);
    }

    public static LocalDate fromText(String dateText) {
        return parseLocalDate(dateText, "dd.MM.yyyy");
    }

    public static LocalDate parseLocalDate(String date, DateTimeFormatter formatter) {
        return LocalDate.parse(date, formatter);
    }

    public static LocalDateTime parseLocalDateTime(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return parseLocalDateTime(date, formatter);
    }

    public static LocalDateTime parseLocalDateTime(String date, DateTimeFormatter formatter) {
        return LocalDateTime.parse(date, formatter);
    }

    public static String format(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalDateTime date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String dateToString(LocalDate date) {
        return format(date, "dd.MM.yyyy");
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String containsDate(String value) {
        Matcher m = pattDateDDMMYYYY.matcher(value);
        if (m.find()) {
            return m.group(1);
        } else {
            m = pattDateDDMMYY.matcher(value);
            return m.find() ? m.group(1) : null;
        }
    }

    public static String formatObject(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof LocalDate) {
            LocalDate ldi = (LocalDate)o;
            return format(ldi, "dd.MM.yyyy");
        } else if (o instanceof LocalDateTime) {
            LocalDateTime ldti = (LocalDateTime)o;
            return format(ldti, "dd.MM.yyyy HH:mm:ss");
        } else {
            return o.toString();
        }
    }

    public static String toString(LocalDateTime dateTime) {
        return dateTime == null ? "" : formatObject(dateTime);
    }

    public static Object toString(LocalDate date) {
        return date == null ? "" : formatObject(date);
    }

    public static Date toJavaUtilDate(LocalDate localDate) {
        return localDate == null ? null : Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static boolean hasTimePart(Date value) {
        LocalDateTime time = of2((Date)Objects.requireNonNull(value));
        return time.getHour() != 0 && time.getMinute() == 0 && time.getSecond() == 0;
    }

    public static LocalDateTime of(LocalDate value) {
        return value == null ? null : LocalDateTime.of(value, LocalTime.of(0, 0));
    }

    public static List<LocalDate> datesFromDatePeriod(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> result = new ArrayList();

        for(LocalDate nextDate = startDate; !nextDate.isAfter(endDate); nextDate = nextDate.plusDays(1L)) {
            result.add(nextDate);
        }

        return result;
    }

    public static LocalDate localDateFromIsoInstant(String str) {
        return LocalDateTime.ofInstant(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(str)), ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime localDateTimeFromIsoInstant(String str) {
        return LocalDateTime.ofInstant(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(str)), ZoneId.systemDefault());
    }

    public static Temporal fromXMLDateTimeString(String value) {
        String dateStr = value.substring(0, Math.min(value.length(), 19));
        return (Temporal)(dateStr.length() > 10 ? parseLocalDateTime(dateStr, "yyyy-MM-dd HH:mm:ss") : parseLocalDate(dateStr, "yyyy-MM-dd"));
    }
}
