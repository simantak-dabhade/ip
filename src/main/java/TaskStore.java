import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskStore {

    private List<Task> items;
    private static final String DATA_FILE_PATH = "./data/lebron_data.txt";

    public TaskStore() {
        items = new ArrayList<>();
        loadFromFile();
    }

    public void addItem(Task item) {
        items.add(item);
        saveToFile();
    }

    public List<Task> readItems() {
        return items;
    }

    public Task deleteItem(int index) {
        if (index >= 0 && index < items.size()) {
            Task deletedTask = items.remove(index);
            saveToFile();
            return deletedTask;
        }
        return null;
    }

    public void updateTask() {
        saveToFile();
    }

    private void saveToFile() {
        try {
            Files.createDirectories(Paths.get("./data"));
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE_PATH))) {
                for (Task task : items) {
                    writer.println(formatTaskForFile(task));
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks to file: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        try {
            if (!Files.exists(Paths.get(DATA_FILE_PATH))) {
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Task task = parseTaskFromFile(line);
                    if (task != null) {
                        items.add(task);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks from file: " + e.getMessage());
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
