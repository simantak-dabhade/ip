package lebron.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    public Event(String description, String fromStr, String toStr) {
        super(description);
        this.from = parseDateTime(fromStr);
        this.to = parseDateTime(toStr);
    }

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public String getFromString() {
        return from.format(OUTPUT_FORMATTER);
    }

    public String getToString() {
        return to.format(OUTPUT_FORMATTER);
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
        return "[E]";
    }

    @Override
    public String toString() {
        return getTypeIcon() + (done ? "[X] " : "[ ] ") + description + " (from: " + getFromString() + " to: " + getToString() + ")";
    }
}