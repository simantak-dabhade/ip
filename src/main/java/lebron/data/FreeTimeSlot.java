package lebron.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a free time slot found in the schedule.
 * 
 * A FreeTimeSlot contains start and end times, and can calculate
 * the duration between them. Used by the freetime feature to show
 * available time periods in the user's schedule.
 */
public class FreeTimeSlot {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final long durationHours;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    /**
     * Creates a new free time slot.
     * 
     * @param start the start time of the free slot
     * @param end the end time of the free slot
     */
    public FreeTimeSlot(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
        this.durationHours = java.time.Duration.between(start, end).toHours();
    }

    /**
     * Gets the start time of this free slot.
     * 
     * @return the start time
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Gets the end time of this free slot.
     * 
     * @return the end time
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Gets the duration of this free slot in hours.
     * 
     * @return the duration in hours
     */
    public long getDurationHours() {
        return durationHours;
    }

    /**
     * Gets the start time as a formatted string.
     * 
     * @return the start time formatted as "MMM dd yyyy HH:mm"
     */
    public String getStartString() {
        return start.format(FORMATTER);
    }

    /**
     * Gets the end time as a formatted string.
     * 
     * @return the end time formatted as "MMM dd yyyy HH:mm"
     */
    public String getEndString() {
        return end.format(FORMATTER);
    }

    /**
     * Creates a string representation of this free time slot.
     * 
     * @return a formatted string showing the time range and duration
     */
    @Override
    public String toString() {
        return getStartString() + " to " + getEndString() + " (" + durationHours + " hours)";
    }
}