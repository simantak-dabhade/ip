package lebron.data;

import java.util.ArrayList;
import java.util.List;
import lebron.task.Task;

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
}