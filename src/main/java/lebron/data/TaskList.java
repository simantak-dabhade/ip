package lebron.data;

import java.util.ArrayList;
import java.util.List;
import lebron.task.Task;

public class TaskList {
    private List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task delete(int index) {
        if (index >= 0 && index < tasks.size()) {
            return tasks.remove(index);
        }
        return null;
    }

    public Task get(int index) {
        if (index >= 0 && index < tasks.size()) {
            return tasks.get(index);
        }
        return null;
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public int size() {
        return tasks.size();
    }

    public void markTask(int index, boolean isDone) {
        Task task = get(index);
        if (task != null) {
            task.setDone(isDone);
        }
    }
}