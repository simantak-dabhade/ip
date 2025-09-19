package lebron.storage;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lebron.task.*;

/**
 * The Storage class is like Lebron's personal assistant for keeping track of your tasks!
 * 
 * It knows how to save your tasks to a file and load them back up when you restart
 * the chatbot. It handles all the messy details of file I/O and data formatting
 * so you don't have to worry about losing your precious task list.
 */
public class Storage {
    private final String filePath;

    /**
     * Creates a new Storage handler for the specified file.
     * 
     * @param filePath where to save and load your tasks from
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads all your saved tasks from the file.
     * 
     * If the file doesn't exist yet (maybe this is your first time?), that's totally fine -
     * we'll just return an empty list and you can start fresh. If there are any hiccups
     * reading the file, we'll do our best to recover and skip the problematic lines.
     * 
     * @return a list of all your saved tasks
     */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try {
            if (!Files.exists(Paths.get(filePath))) {
                return tasks;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Task task = parseTaskFromFile(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks from file: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Saves all your tasks to the file for safekeeping.
     * 
     * This makes sure your task list survives between chat sessions. The method
     * automatically creates any needed directories and handles the file formatting.
     * If something goes wrong, it'll let you know but won't crash the program.
     * 
     * @param tasks the complete list of tasks to save
     */
    public void save(List<Task> tasks) {
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                for (Task task : tasks) {
                    writer.println(formatTaskForFile(task));
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks to file: " + e.getMessage());
        }
    }

    /**
     * Formats a task for storage in the file.
     * 
     * Creates a pipe-separated format that includes task type, completion status,
     * description, and any time-related information for deadlines and events.
     * 
     * @param task the task to format
     * @return a formatted string ready for file storage
     */
    private String formatTaskForFile(Task task) {
        String doneStatus = task.isDone() ? "1" : "0";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        
        if (task instanceof Todo) {
            return "T | " + doneStatus + " | " + task.getDescription();
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D | " + doneStatus + " | " + task.getDescription() + " | " + deadline.getBy().format(formatter);
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return "E | " + doneStatus + " | " + task.getDescription() + " | " + event.getFrom().format(formatter) + " | " + event.getTo().format(formatter);
        }
        
        return "";
    }

    /**
     * Parses a task from a line in the storage file.
     * 
     * Expects a pipe-separated format with task type, completion status,
     * description, and any time information. Handles parsing errors gracefully
     * by returning null for malformed lines.
     * 
     * @param line the line from the file to parse
     * @return the parsed Task object, or null if parsing fails
     */
    private Task parseTaskFromFile(String line) {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return null;
        }

        String taskType = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task = null;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        
        try {
            switch (taskType) {
                case "T":
                    task = new Todo(description);
                    break;
                case "D":
                    if (parts.length >= 4) {
                        LocalDateTime byDateTime = LocalDateTime.parse(parts[3], formatter);
                        task = new Deadline(description, byDateTime);
                    }
                    break;
                case "E":
                    if (parts.length >= 5) {
                        LocalDateTime fromDateTime = LocalDateTime.parse(parts[3], formatter);
                        LocalDateTime toDateTime = LocalDateTime.parse(parts[4], formatter);
                        task = new Event(description, fromDateTime, toDateTime);
                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error parsing task from file: " + line + " - " + e.getMessage());
            return null;
        }

        if (task != null) {
            task.setDone(isDone);
        }

        return task;
    }
}