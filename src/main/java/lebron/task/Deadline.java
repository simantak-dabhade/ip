package lebron.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a specific deadline.
 * 
 * A Deadline task has all the basic task functionality plus a specific date and time
 * by which the task must be completed. It supports multiple date formats for user
 * convenience and displays the deadline in a human-readable format.
 */
public class Deadline extends Task {
    private final LocalDateTime by;
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    /**
     * Creates a new Deadline task with the given description and deadline string.
     * 
     * @param description what this deadline task is about
     * @param byStr the deadline in string format (supports multiple formats)
     * @throws IllegalArgumentException if the date string is invalid or empty
     */
    public Deadline(String description, String byStr) {
        super(description);
        this.by = parseDateTime(byStr);
    }

    /**
     * Creates a new Deadline task with the given description and deadline.
     * 
     * @param description what this deadline task is about
     * @param by the deadline as a LocalDateTime object
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Gets the deadline date and time.
     * 
     * @return the deadline as a LocalDateTime object
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Gets the deadline formatted as a human-readable string.
     * 
     * @return the deadline formatted as "MMM dd yyyy HH:mm" (e.g., "Dec 25 2024 14:00")
     */
    public String getByString() {
        return by.format(OUTPUT_FORMATTER);
    }

    /**
     * Parses a date-time string into a LocalDateTime object.
     * 
     * Supports multiple formats:
     * - yyyy-mm-dd (defaults to 00:00:00)
     * - yyyy-mm-dd HHmm
     * - d/m/yyyy (defaults to 00:00:00)
     * - d/m/yyyy HHmm
     * 
     * @param dateTimeStr the date-time string to parse
     * @return the parsed LocalDateTime
     * @throws IllegalArgumentException if the string is null, empty, or in an invalid format
     */
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

    /**
     * Returns the type icon for deadline tasks.
     * 
     * @return "[D]" to indicate this is a Deadline task
     */
    @Override
    public String getTypeIcon() {
        return "[D]";
    }

    /**
     * Creates a string representation of this deadline task.
     * 
     * Includes the type icon, completion status, description, and formatted deadline.
     * 
     * @return a formatted string like "[D][X] submit report (by: Dec 25 2024 14:00)"
     */
    @Override
    public String toString() {
        return getTypeIcon() + (done ? "[X] " : "[ ] ") + description + " (by: " + getByString() + ")";
    }
}