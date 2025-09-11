package lebron.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a free time slot found in the schedule
 */
public class FreeTimeSlot {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final long durationHours;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    public FreeTimeSlot(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
        this.durationHours = java.time.Duration.between(start, end).toHours();
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public long getDurationHours() {
        return durationHours;
    }

    public String getStartString() {
        return start.format(FORMATTER);
    }

    public String getEndString() {
        return end.format(FORMATTER);
    }

    @Override
    public String toString() {
        return getStartString() + " to " + getEndString() + " (" + durationHours + " hours)";
    }
}