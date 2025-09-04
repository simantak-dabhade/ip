package lebron.gui;

import javafx.scene.control.TextArea;
import lebron.storage.Storage;
import lebron.data.TaskList;
import lebron.ui.GuiUi;
import lebron.parser.Parser;
import lebron.task.*;
import java.util.List;

/**
 * GUI version of the Lebron chatbot that works with JavaFX interface.
 * 
 * This class provides the same functionality as the original Lebron class
 * but is designed to work with a GUI TextArea instead of console input/output.
 */
public class GuiLebron {
    private final Storage storage;
    private TaskList tasks;
    private final GuiUi ui;

    public GuiLebron(String filePath, TextArea chatHistory) {
        ui = new GuiUi(chatHistory);
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            ui.showError("Error loading tasks: " + e.getMessage());
            tasks = new TaskList();
        }
        ui.showWelcome();
    }

    /**
     * Processes a single command from the GUI and returns the response.
     * 
     * @param input the user's input command
     * @return true if the command was "bye" (indicating the user wants to exit)
     */
    public boolean processCommand(String input) {
        try {
            Parser.Command command = Parser.parse(input);
            
            switch (command.getType()) {
                case BYE:
                    ui.showGoodbye();
                    return true; // Signal to exit
                case LIST:
                    handleList();
                    break;
                case TODO:
                    handleTodo(command.getArgument());
                    break;
                case DEADLINE:
                    handleDeadline(command.getArgument());
                    break;
                case EVENT:
                    handleEvent(command.getArgument());
                    break;
                case MARK:
                    handleMark(command.getArgument());
                    break;
                case UNMARK:
                    handleUnmark(command.getArgument());
                    break;
                case DELETE:
                    handleDelete(command.getArgument());
                    break;
                case FIND:
                    handleFind(command.getArgument());
                    break;
                case UNKNOWN:
                    ui.showUnknownCommand();
                    break;
            }
        } catch (Exception e) {
            ui.showError("An error occurred: " + e.getMessage());
        }
        return false; // Continue running
    }

    private void handleList() {
        ui.showTaskList(tasks.getAllTasks());
    }

    private void handleTodo(String description) {
        if (description.trim().isEmpty()) {
            ui.showError("The description of a todo cannot be empty.");
            return;
        }
        
        Todo todo = new Todo(description);
        tasks.add(todo);
        saveToStorage();
        ui.showTaskAdded(todo, tasks.size());
    }

    private void handleDeadline(String input) {
        if (input.trim().isEmpty()) {
            ui.showError("The description of a deadline cannot be empty.\\nPlease use format: deadline <description> /by <date>");
            return;
        }
        
        String[] parts = input.split(" /by ", 2);
        if (parts.length == 2) {
            String description = parts[0].trim();
            String by = parts[1].trim();
            
            if (description.isEmpty()) {
                ui.showError("The description of a deadline cannot be empty.");
                return;
            }
            
            if (by.isEmpty()) {
                ui.showError("Please specify when the deadline is due.\\nPlease use format: deadline <description> /by <date>");
                return;
            }
            
            try {
                Deadline deadline = new Deadline(description, by);
                tasks.add(deadline);
                saveToStorage();
                ui.showTaskAdded(deadline, tasks.size());
            } catch (IllegalArgumentException e) {
                ui.showError(e.getMessage());
            }
        } else {
            ui.showError("I need both a description and a due date.\\nPlease use format: deadline <description> /by <date>");
        }
    }

    private void handleEvent(String input) {
        if (input.trim().isEmpty()) {
            ui.showError("The description of an event cannot be empty.\\nPlease use format: event <description> /from <start> /to <end>");
            return;
        }
        
        String[] parts = input.split(" /from ", 2);
        if (parts.length == 2) {
            String description = parts[0].trim();
            String[] timeParts = parts[1].split(" /to ", 2);
            if (timeParts.length == 2) {
                String from = timeParts[0].trim();
                String to = timeParts[1].trim();
                
                if (description.isEmpty()) {
                    ui.showError("The description of an event cannot be empty.");
                    return;
                }
                
                if (from.isEmpty()) {
                    ui.showError("Please specify when the event starts.\\nPlease use format: event <description> /from <start> /to <end>");
                    return;
                }
                
                if (to.isEmpty()) {
                    ui.showError("Please specify when the event ends.\\nPlease use format: event <description> /from <start> /to <end>");
                    return;
                }
                
                try {
                    Event event = new Event(description, from, to);
                    tasks.add(event);
                    saveToStorage();
                    ui.showTaskAdded(event, tasks.size());
                } catch (IllegalArgumentException e) {
                    ui.showError(e.getMessage());
                }
            } else {
                ui.showError("I need both start and end times for the event.\\nPlease use format: event <description> /from <start> /to <end>");
            }
        } else {
            ui.showError("I need a description and timing for the event.\\nPlease use format: event <description> /from <start> /to <end>");
        }
    }

    private void handleMark(String indexStr) {
        if (indexStr.trim().isEmpty()) {
            ui.showError("Please specify which task to mark as done.\\nUse: mark <task number>");
            return;
        }
        
        try {
            int index = Integer.parseInt(indexStr.trim()) - 1;
            Task task = tasks.get(index);
            
            if (task != null) {
                tasks.markTask(index, true);
                saveToStorage();
                ui.showTaskMarked(task);
            } else {
                ui.showError("I don't have a task with that number.\\nUse 'list' to see your tasks first.");
            }
        } catch (NumberFormatException e) {
            ui.showError("That's not a valid task number.\\nPlease provide a number (e.g., mark 1)");
        }
    }

    private void handleUnmark(String indexStr) {
        if (indexStr.trim().isEmpty()) {
            ui.showError("Please specify which task to mark as not done.\\nUse: unmark <task number>");
            return;
        }
        
        try {
            int index = Integer.parseInt(indexStr.trim()) - 1;
            Task task = tasks.get(index);
            
            if (task != null) {
                tasks.markTask(index, false);
                saveToStorage();
                ui.showTaskUnmarked(task);
            } else {
                ui.showError("I don't have a task with that number.\\nUse 'list' to see your tasks first.");
            }
        } catch (NumberFormatException e) {
            ui.showError("That's not a valid task number.\\nPlease provide a number (e.g., unmark 1)");
        }
    }

    private void handleDelete(String indexStr) {
        if (indexStr.trim().isEmpty()) {
            ui.showError("Please specify which task to delete.\\nUse: delete <task number>");
            return;
        }
        
        try {
            int index = Integer.parseInt(indexStr.trim()) - 1;
            Task deletedTask = tasks.delete(index);
            
            if (deletedTask != null) {
                saveToStorage();
                ui.showTaskDeleted(deletedTask, tasks.size());
            } else {
                ui.showError("I don't have a task with that number.\\nUse 'list' to see your tasks first.");
            }
        } catch (NumberFormatException e) {
            ui.showError("That's not a valid task number.\\nPlease provide a number (e.g., delete 1)");
        }
    }

    private void handleFind(String keyword) {
        if (keyword.trim().isEmpty()) {
            ui.showError("Please specify a keyword to search for.\\nUse: find <keyword>");
            return;
        }
        
        List<Task> matchingTasks = tasks.findTasks(keyword);
        ui.showFindResults(matchingTasks, keyword);
    }

    private void saveToStorage() {
        storage.save(tasks.getAllTasks());
    }
}