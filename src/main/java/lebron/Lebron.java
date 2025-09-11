package lebron;

import lebron.storage.Storage;
import lebron.data.TaskList;
import lebron.data.FreeTimeSlot;
import lebron.ui.Ui;
import lebron.parser.Parser;
import lebron.task.*;
import java.util.List;

/**
 * The main class for the Lebron chatbot - your personal task management assistant.
 * 
 * This chatbot helps you keep track of todos, deadlines, and events through a simple
 * command-line interface. Just like the GOAT himself, it's reliable and gets the job done!
 * 
 * Features include:
 * - Managing different types of tasks (todos, deadlines, events)
 * - Persistent storage of your tasks
 * - Smart date parsing for deadlines and events
 * - Search functionality to find specific tasks
 * 
 * @author Your friendly neighborhood developer
 * @version 1.0
 */
public class Lebron {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    /**
     * Creates a new Lebron chatbot instance and loads existing tasks from storage.
     * 
     * If there's an issue loading your saved tasks (maybe the file is corrupted or missing),
     * don't worry - we'll start fresh with an empty task list and let you know what happened.
     * 
     * @param filePath the path to the file where your tasks are stored
     */
    public Lebron(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            ui.showError("Error loading tasks: " + e.getMessage());
            tasks = new TaskList();
        }
    }

    /**
     * Starts the main chat loop where all the magic happens!
     * 
     * This method handles the interactive conversation with you, processing each command
     * and keeping things running smoothly until you decide to say goodbye. It's designed
     * to be forgiving - if something goes wrong, we'll catch it and keep going.
     */
    public void run() {
        ui.showWelcome();
        
        boolean isExit = false;
        while (!isExit) {
            try {
                String input = ui.readCommand();
                Parser.Command command = Parser.parse(input);
                
                switch (command.getType()) {
                    case BYE:
                        isExit = true;
                        break;
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
                    case FREETIME:
                        handleFreeTime(command.getArgument());
                        break;
                    case UNKNOWN:
                        ui.showUnknownCommand();
                        break;
                }
            } catch (Exception e) {
                ui.showError("An error occurred: " + e.getMessage());
            }
        }
        
        ui.showGoodbye();
        ui.close();
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

    private void handleFreeTime(String hoursStr) {
        if (hoursStr.trim().isEmpty()) {
            ui.showError("Please specify how many hours you need.\\nUse: freetime <hours>");
            return;
        }
        
        try {
            int hoursNeeded = Integer.parseInt(hoursStr.trim());
            
            if (hoursNeeded <= 0) {
                ui.showError("Please specify a positive number of hours.");
                return;
            }
            
            FreeTimeSlot freeSlot = tasks.findNextFreeTime(hoursNeeded);
            ui.showFreeTimeResult(freeSlot, hoursNeeded);
            
        } catch (NumberFormatException e) {
            ui.showError("That's not a valid number of hours.\\nPlease provide a number (e.g., freetime 4)");
        }
    }

    private void saveToStorage() {
        storage.save(tasks.getAllTasks());
    }

    /**
     * The entry point for the Lebron chatbot application.
     * 
     * This starts up the chatbot with the default data file location.
     * Your tasks will be saved to and loaded from "./data/lebron_data.txt".
     * 
     * @param args command line arguments (currently not used, but hey, maybe in the future!)
     */
    public static void main(String[] args) {
        new Lebron("./data/lebron_data.txt").run();
    }
}