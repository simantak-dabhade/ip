import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    private LocalDateTime by;
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    public Deadline(String description, String byStr) {
        super(description);
        this.by = parseDateTime(byStr);
    }

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    public LocalDateTime getBy() {
        return by;
    }

    public String getByString() {
        return by.format(OUTPUT_FORMATTER);
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Date cannot be empty");
        }

        String trimmed = dateTimeStr.trim();
        
        try {
            // Try yyyy-mm-dd format first
            if (trimmed.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return LocalDateTime.parse(trimmed + "T00:00:00");
            }
            
            // Try yyyy-mm-dd HHmm format
            if (trimmed.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
                String[] parts = trimmed.split(" ");
                String datePart = parts[0];
                String timePart = parts[1];
                String hour = timePart.substring(0, 2);
                String minute = timePart.substring(2, 4);
                return LocalDateTime.parse(datePart + "T" + hour + ":" + minute + ":00");
            }
            
            // Try d/m/yyyy format
            if (trimmed.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                String[] parts = trimmed.split("/");
                String day = String.format("%02d", Integer.parseInt(parts[0]));
                String month = String.format("%02d", Integer.parseInt(parts[1]));
                String year = parts[2];
                return LocalDateTime.parse(year + "-" + month + "-" + day + "T00:00:00");
            }
            
            // Try d/m/yyyy HHmm format
            if (trimmed.matches("\\d{1,2}/\\d{1,2}/\\d{4} \\d{4}")) {
                String[] parts = trimmed.split(" ");
                String[] dateParts = parts[0].split("/");
                String day = String.format("%02d", Integer.parseInt(dateParts[0]));
                String month = String.format("%02d", Integer.parseInt(dateParts[1]));
                String year = dateParts[2];
                String timePart = parts[1];
                String hour = timePart.substring(0, 2);
                String minute = timePart.substring(2, 4);
                return LocalDateTime.parse(year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":00");
            }
            
            // If no pattern matches, try direct parsing
            return LocalDateTime.parse(trimmed);
            
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateTimeStr + ". Please use formats like: yyyy-mm-dd, yyyy-mm-dd HHmm, d/m/yyyy, or d/m/yyyy HHmm");
        }
    }

    @Override
    public String getTypeIcon() {
        return "[D]";
    }

    @Override
    public String toString() {
        return getTypeIcon() + (done ? "[X] " : "[ ] ") + description + " (by: " + getByString() + ")";
    }
}