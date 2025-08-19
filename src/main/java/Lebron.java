import java.util.Scanner;
import java.util.List;

public class Lebron {
    public static void main(String[] args) {
        Lebron lebron = new Lebron();
        lebron.welcomeMessage();
        lebron.chatLoop();
        lebron.goodbyeMessage();
    }

    private TaskStore itemStore;

    public Lebron() {
        itemStore = new TaskStore();
    }

    private void welcomeMessage() {
        String welcomeMsg = """
                ____________________________________________________________
                Hello! I'm lebron (but you can call me GOAT or leking, even pookiebron if your feeling like it)
                What can I do for you?
                ____________________________________________________________
                """;

        System.out.println(welcomeMsg);
    }

    private void goodbyeMessage() {
        String goodbyeMsg = """
                ___________________________________________________________
                 Bye. Hope to see you again soon!
                ____________________________________________________________
                """;

        System.out.println(goodbyeMsg);
    }

    private void chatLoop() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("You: ");
            String input = scanner.nextLine().trim();

            // exit condition
            if (input.equalsIgnoreCase("bye")) {
                break;
            }

            if (input.toLowerCase().startsWith("read ")) {
                handleRead(input.substring(5)); // everything after "read "
            } else if (input.toLowerCase().startsWith("todo")) {
                if (input.length() > 4 && input.charAt(4) == ' ') {
                    handleTodo(input.substring(5)); // everything after "todo "
                } else if (input.equalsIgnoreCase("todo")) {
                    handleTodo(""); // empty todo
                } else {
                    handleTodo(input.substring(4)); // handle "todoX" cases
                }
            } else if (input.toLowerCase().startsWith("deadline")) {
                if (input.length() > 8 && input.charAt(8) == ' ') {
                    handleDeadline(input.substring(9)); // everything after "deadline "
                } else if (input.equalsIgnoreCase("deadline")) {
                    handleDeadline(""); // empty deadline
                } else {
                    handleDeadline(input.substring(8)); // handle "deadlineX" cases
                }
            } else if (input.toLowerCase().startsWith("event")) {
                if (input.length() > 5 && input.charAt(5) == ' ') {
                    handleEvent(input.substring(6)); // everything after "event "
                } else if (input.equalsIgnoreCase("event")) {
                    handleEvent(""); // empty event
                } else {
                    handleEvent(input.substring(5)); // handle "eventX" cases
                }
            } else if (input.equalsIgnoreCase("list")) {
                handleList();
            } else if (input.toLowerCase().startsWith("mark")) {
                if (input.length() > 4 && input.charAt(4) == ' ') {
                    handleMark(input.substring(5)); // everything after "mark "
                } else if (input.equalsIgnoreCase("mark")) {
                    handleMark(""); // empty mark
                } else {
                    handleMark(input.substring(4)); // handle "markX" cases
                }
            } else if (input.toLowerCase().startsWith("unmark")) {
                if (input.length() > 6 && input.charAt(6) == ' ') {
                    handleUnmark(input.substring(7)); // everything after "unmark "
                } else if (input.equalsIgnoreCase("unmark")) {
                    handleUnmark(""); // empty unmark
                } else {
                    handleUnmark(input.substring(6)); // handle "unmarkX" cases
                }
            } else if (input.toLowerCase().startsWith("delete")) {
                if (input.length() > 6 && input.charAt(6) == ' ') {
                    handleDelete(input.substring(7)); // everything after "delete "
                } else if (input.equalsIgnoreCase("delete")) {
                    handleDelete(""); // empty delete
                } else {
                    handleDelete(input.substring(6)); // handle "deleteX" cases
                }
            } else {
                System.out.println("____________________________________________________________");
                System.out.println(" OOPS!!! I'm sorry, but I don't know what that means :-(");
                System.out.println(" Try 'list', 'todo <description>', 'deadline <desc> /by <date>',");
                System.out.println(" 'event <desc> /from <start> /to <end>', 'mark <number>',");
                System.out.println(" 'unmark <number>', 'delete <number>', or 'bye'.");
                System.out.println("____________________________________________________________");
            }
        }

        scanner.close();
    }

    private void handleRead(String item) {
        itemStore.addItem(new Todo(item));
        System.out.println("____________________________________________________________");
        System.out.println(" added: " + item);
        System.out.println("____________________________________________________________");
    }

    private void handleList() {
        List<Task> items = itemStore.readItems();
        System.out.println("____________________________________________________________");
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println(" " + (i + 1) + "." + items.get(i).toString());
        }
        System.out.println("____________________________________________________________");
    }

    private void handleMark(String indexStr) {
        if (indexStr.trim().isEmpty()) {
            System.out.println("____________________________________________________________");
            System.out.println(" OOPS!!! Please specify which task to mark as done.");
            System.out.println(" Use: mark <task number>");
            System.out.println("____________________________________________________________");
            return;
        }
        
        try {
            int index = Integer.parseInt(indexStr.trim()) - 1; // convert to 0-based index
            List<Task> items = itemStore.readItems();
            
            if (index >= 0 && index < items.size()) {
                Task task = items.get(index);
                task.setDone(true);
                System.out.println("____________________________________________________________");
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + task.toString());
                System.out.println("____________________________________________________________");
            } else {
                System.out.println("____________________________________________________________");
                System.out.println(" OOPS!!! I don't have a task with that number.");
                System.out.println(" Use 'list' to see your tasks first.");
                System.out.println("____________________________________________________________");
            }
        } catch (NumberFormatException e) {
            System.out.println("____________________________________________________________");
            System.out.println(" OOPS!!! That's not a valid task number.");
            System.out.println(" Please provide a number (e.g., mark 1)");
            System.out.println("____________________________________________________________");
        }
    }

    private void handleUnmark(String indexStr) {
        if (indexStr.trim().isEmpty()) {
            System.out.println("____________________________________________________________");
            System.out.println(" OOPS!!! Please specify which task to mark as not done.");
            System.out.println(" Use: unmark <task number>");
            System.out.println("____________________________________________________________");
            return;
        }
        
        try {
            int index = Integer.parseInt(indexStr.trim()) - 1; // convert to 0-based index
            List<Task> items = itemStore.readItems();
            
            if (index >= 0 && index < items.size()) {
                Task task = items.get(index);
                task.setDone(false);
                System.out.println("____________________________________________________________");
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + task.toString());
                System.out.println("____________________________________________________________");
            } else {
                System.out.println("____________________________________________________________");
                System.out.println(" OOPS!!! I don't have a task with that number.");
                System.out.println(" Use 'list' to see your tasks first.");
                System.out.println("____________________________________________________________");
            }
        } catch (NumberFormatException e) {
            System.out.println("____________________________________________________________");
            System.out.println(" OOPS!!! That's not a valid task number.");
            System.out.println(" Please provide a number (e.g., unmark 1)");
            System.out.println("____________________________________________________________");
        }
    }

    private void handleTodo(String description) {
        if (description.trim().isEmpty()) {
            System.out.println("____________________________________________________________");
            System.out.println(" OOPS!!! The description of a todo cannot be empty.");
            System.out.println("____________________________________________________________");
            return;
        }
        
        Todo todo = new Todo(description);
        itemStore.addItem(todo);
        System.out.println("____________________________________________________________");
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + todo.toString());
        System.out.println(" Now you have " + itemStore.readItems().size() + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    private void handleDeadline(String input) {
        if (input.trim().isEmpty()) {
            System.out.println("____________________________________________________________");
            System.out.println(" OOPS!!! The description of a deadline cannot be empty.");
            System.out.println(" Please use format: deadline <description> /by <date>");
            System.out.println("____________________________________________________________");
            return;
        }
        
        String[] parts = input.split(" /by ", 2);
        if (parts.length == 2) {
            String description = parts[0].trim();
            String by = parts[1].trim();
            
            if (description.isEmpty()) {
                System.out.println("____________________________________________________________");
                System.out.println(" OOPS!!! The description of a deadline cannot be empty.");
                System.out.println("____________________________________________________________");
                return;
            }
            
            if (by.isEmpty()) {
                System.out.println("____________________________________________________________");
                System.out.println(" OOPS!!! Please specify when the deadline is due.");
                System.out.println(" Please use format: deadline <description> /by <date>");
                System.out.println("____________________________________________________________");
                return;
            }
            
            Deadline deadline = new Deadline(description, by);
            itemStore.addItem(deadline);
            System.out.println("____________________________________________________________");
            System.out.println(" Got it. I've added this task:");
            System.out.println("   " + deadline.toString());
            System.out.println(" Now you have " + itemStore.readItems().size() + " tasks in the list.");
            System.out.println("____________________________________________________________");
        } else {
            System.out.println("____________________________________________________________");
            System.out.println(" OOPS!!! I need both a description and a due date.");
            System.out.println(" Please use format: deadline <description> /by <date>");
            System.out.println("____________________________________________________________");
        }
    }

    private void handleEvent(String input) {
        if (input.trim().isEmpty()) {
            System.out.println("____________________________________________________________");
            System.out.println(" OOPS!!! The description of an event cannot be empty.");
            System.out.println(" Please use format: event <description> /from <start> /to <end>");
            System.out.println("____________________________________________________________");
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
                    System.out.println("____________________________________________________________");
                    System.out.println(" OOPS!!! The description of an event cannot be empty.");
                    System.out.println("____________________________________________________________");
                    return;
                }
                
                if (from.isEmpty()) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" OOPS!!! Please specify when the event starts.");
                    System.out.println(" Please use format: event <description> /from <start> /to <end>");
                    System.out.println("____________________________________________________________");
                    return;
                }
                
                if (to.isEmpty()) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" OOPS!!! Please specify when the event ends.");
                    System.out.println(" Please use format: event <description> /from <start> /to <end>");
                    System.out.println("____________________________________________________________");
                    return;
                }
                
                Event event = new Event(description, from, to);
                itemStore.addItem(event);
                System.out.println("____________________________________________________________");
                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + event.toString());
                System.out.println(" Now you have " + itemStore.readItems().size() + " tasks in the list.");
                System.out.println("____________________________________________________________");
            } else {
                System.out.println("____________________________________________________________");
                System.out.println(" OOPS!!! I need both start and end times for the event.");
                System.out.println(" Please use format: event <description> /from <start> /to <end>");
                System.out.println("____________________________________________________________");
            }
        } else {
            System.out.println("____________________________________________________________");
            System.out.println(" OOPS!!! I need a description and timing for the event.");
            System.out.println(" Please use format: event <description> /from <start> /to <end>");
            System.out.println("____________________________________________________________");
        }
    }

    private void handleDelete(String indexStr) {
        if (indexStr.trim().isEmpty()) {
            System.out.println("____________________________________________________________");
            System.out.println(" OOPS!!! Please specify which task to delete.");
            System.out.println(" Use: delete <task number>");
            System.out.println("____________________________________________________________");
            return;
        }
        
        try {
            int index = Integer.parseInt(indexStr.trim()) - 1; // convert to 0-based index
            Task deletedTask = itemStore.deleteItem(index);
            
            if (deletedTask != null) {
                System.out.println("____________________________________________________________");
                System.out.println(" Noted. I've removed this task:");
                System.out.println("   " + deletedTask.toString());
                System.out.println(" Now you have " + itemStore.readItems().size() + " tasks in the list.");
                System.out.println("____________________________________________________________");
            } else {
                System.out.println("____________________________________________________________");
                System.out.println(" OOPS!!! I don't have a task with that number.");
                System.out.println(" Use 'list' to see your tasks first.");
                System.out.println("____________________________________________________________");
            }
        } catch (NumberFormatException e) {
            System.out.println("____________________________________________________________");
            System.out.println(" OOPS!!! That's not a valid task number.");
            System.out.println(" Please provide a number (e.g., delete 1)");
            System.out.println("____________________________________________________________");
        }
    }
}