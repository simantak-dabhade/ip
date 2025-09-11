package lebron.data;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;
import lebron.task.Task;
import lebron.task.Event;

/**
 * The TaskList class is your personal task manager - it keeps track of all your stuff!
 * 
 * Think of it as a smart list that not only holds your tasks but also knows how to
 * manipulate them in useful ways. It can add, remove, search, and mark tasks as done.
 * It's designed to be robust and handle edge cases gracefully.
 */
public class TaskList {
    private List<Task> tasks;

    /**
     * Creates a new, empty task list ready to hold your tasks.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list from an existing collection of tasks.
     * 
     * @param tasks the existing tasks to start with (can be null, we'll handle it)
     */
    public TaskList(List<Task> tasks) {
        this.tasks = (tasks != null) ? tasks : new ArrayList<>();
    }

    /**
     * Adds a new task to your list.
     * 
     * @param task the task to add (even handles null gracefully)
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task from your list and returns it.
     * 
     * @param index the position of the task to remove (1-based from user perspective, but 0-based here)
     * @return the removed task, or null if the index is out of bounds
     */
    public Task delete(int index) {
        if (index >= 0 && index < tasks.size()) {
            return tasks.remove(index);
        }
        return null;
    }

    /**
     * Gets a specific task from your list without removing it.
     * 
     * @param index the position of the task to get
     * @return the task at that position, or null if index is invalid
     */
    public Task get(int index) {
        if (index >= 0 && index < tasks.size()) {
            return tasks.get(index);
        }
        return null;
    }

    /**
     * Gets all tasks in the list.
     * 
     * @return the complete list of tasks
     */
    public List<Task> getAllTasks() {
        return tasks;
    }

    /**
     * Tells you how many tasks you have.
     * 
     * @return the number of tasks in your list
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Marks a task as done or not done.
     * 
     * @param index which task to mark
     * @param isDone true to mark as done, false to mark as not done
     */
    public void markTask(int index, boolean isDone) {
        Task task = get(index);
        if (task != null) {
            task.setDone(isDone);
        }
    }

    /**
     * Searches through your tasks to find ones that match a keyword.
     * 
     * This is a case-insensitive search that looks through task descriptions.
     * Super handy when you have lots of tasks and need to find specific ones!
     * 
     * @param keyword what to search for in task descriptions
     * @return a list of matching tasks (empty list if no matches or invalid keyword)
     */
    public List<Task> findTasks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Task> matchingTasks = new ArrayList<>();
        String lowercaseKeyword = keyword.trim().toLowerCase();
        
        for (Task task : tasks) {
            if (task != null && task.getDescription().toLowerCase().contains(lowercaseKeyword)) {
                matchingTasks.add(task);
            }
        }
        
        return matchingTasks;
    }

    /**
     * Finds the next free time slot of the specified duration.
     * 
     * Searches through your schedule to find when you have uninterrupted time.
     * Considers working hours (9 AM to 9 PM) and looks ahead up to 14 days.
     * 
     * @param hoursNeeded the minimum number of hours needed
     * @return the earliest available free time slot, or null if none found
     */
    public FreeTimeSlot findNextFreeTime(int hoursNeeded) {
        if (hoursNeeded <= 0) {
            return null;
        }

        List<Event> events = getEventsFromTasks();
        LocalDateTime searchStart = LocalDateTime.now();
        
        for (int day = 0; day < 14; day++) {
            LocalDate currentDate = searchStart.toLocalDate().plusDays(day);
            LocalDateTime dayStart = currentDate.atTime(9, 0);
            LocalDateTime dayEnd = currentDate.atTime(21, 0);
            
            FreeTimeSlot freeSlot = findFreeTimeInDay(dayStart, dayEnd, hoursNeeded, events);
            if (freeSlot != null) {
                return freeSlot;
            }
        }
        
        return null;
    }

    private List<Event> getEventsFromTasks() {
        List<Event> events = new ArrayList<>();
        for (Task task : tasks) {
            if (task instanceof Event) {
                events.add((Event) task);
            }
        }
        
        events.sort((e1, e2) -> e1.getFrom().compareTo(e2.getFrom()));
        return events;
    }

    private FreeTimeSlot findFreeTimeInDay(LocalDateTime dayStart, LocalDateTime dayEnd, 
                                          int hoursNeeded, List<Event> events) {
        LocalDateTime currentTime = dayStart;
        
        // Filter events to only those that overlap with this day
        List<Event> dayEvents = new ArrayList<>();
        for (Event event : events) {
            LocalDateTime eventStart = event.getFrom();
            LocalDateTime eventEnd = event.getTo();
            
            // Check if event overlaps with the day
            if (!(eventEnd.isBefore(dayStart) || eventStart.isAfter(dayEnd))) {
                dayEvents.add(event);
            }
        }
        
        // Sort events by start time
        dayEvents.sort((e1, e2) -> e1.getFrom().compareTo(e2.getFrom()));
        
        // Check time slots between events
        for (Event event : dayEvents) {
            LocalDateTime eventStart = event.getFrom().isAfter(dayStart) ? event.getFrom() : dayStart;
            
            long availableHours = java.time.Duration.between(currentTime, eventStart).toHours();
            
            if (availableHours >= hoursNeeded) {
                return new FreeTimeSlot(currentTime, currentTime.plusHours(hoursNeeded));
            }
            
            currentTime = event.getTo().isAfter(currentTime) ? event.getTo() : currentTime;
            if (currentTime.isAfter(dayEnd)) {
                break;
            }
        }
        
        // Check remaining time after all events
        if (currentTime.isBefore(dayEnd)) {
            long remainingHours = java.time.Duration.between(currentTime, dayEnd).toHours();
            if (remainingHours >= hoursNeeded) {
                return new FreeTimeSlot(currentTime, currentTime.plusHours(hoursNeeded));
            }
        }
        
        return null;
    }
}