package jebi.hendardi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    // Fungsi untuk mengubah string menjadi LocalDate
    public static LocalDate parseToLocalDate(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateStr, formatter);
    }

    // Fungsi untuk mengubah LocalDate menjadi string
    public static String formatLocalDate(LocalDate date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    // Fungsi untuk mendapatkan tanggal saat ini
    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }

    // Fungsi untuk mendapatkan tanggal dan waktu saat ini
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }
}
