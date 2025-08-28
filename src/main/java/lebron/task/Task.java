package lebron.task;

public abstract class Task {
    protected String description;
    protected boolean done;

    public Task(String description) {
        this.description = description;
        this.done = false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public abstract String getTypeIcon();

    @Override
    public String toString() {
        return getTypeIcon() + (done ? "[X] " : "[ ] ") + description;
    }
}