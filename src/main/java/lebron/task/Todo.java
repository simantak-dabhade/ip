package lebron.task;

/**
 * Represents a basic todo task without any specific timing constraints.
 * 
 * A Todo is the simplest type of task - just something that needs to be done
 * without any deadline or time window. It inherits all the basic task functionality
 * like marking as done and has its own type icon.
 */
public class Todo extends Task {
    
    /**
     * Creates a new Todo task with the given description.
     * 
     * @param description what this todo task is about
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the type icon for todo tasks.
     * 
     * @return "[T]" to indicate this is a Todo task
     */
    @Override
    public String getTypeIcon() {
        return "[T]";
    }
}