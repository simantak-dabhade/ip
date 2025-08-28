package lebron.storage;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lebron.task.*;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

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