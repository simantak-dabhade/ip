package lebron.task;

/**
 * The base class for all types of tasks in your list.
 * 
 * Whether it's a simple todo, a deadline, or an event, they all share some common
 * characteristics - they have a description, they can be marked as done, and they
 * have a visual representation. This abstract class captures all that shared behavior.
 */
public abstract class Task {
    protected String description;
    protected boolean done;

    /**
     * Creates a new task with the given description.
     * 
     * @param description what this task is about
     */
    public Task(String description) {
        this.description = description;
        this.done = false;
    }

    /**
     * Gets what this task is about.
     * 
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates what this task is about.
     * 
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Checks if this task has been completed.
     * 
     * @return true if done, false if still pending
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Marks this task as done or not done.
     * 
     * @param done true to mark as completed, false for pending
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     * Gets the visual icon that represents this type of task.
     * 
     * Each subclass provides its own icon (like [T] for todos, [D] for deadlines).
     * 
     * @return the type-specific icon string
     */
    public abstract String getTypeIcon();

    /**
     * Creates a nice string representation of this task.
     * 
     * Shows the type icon, completion status, and description all in one line.
     * 
     * @return a formatted string representation
     */
    @Override
    public String toString() {
        return getTypeIcon() + (done ? "[X] " : "[ ] ") + description;
    }
}