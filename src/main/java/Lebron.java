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
            } else if (input.toLowerCase().startsWith("todo ")) {
                handleTodo(input.substring(5)); // everything after "todo "
            } else if (input.toLowerCase().startsWith("deadline ")) {
                handleDeadline(input.substring(9)); // everything after "deadline "
            } else if (input.toLowerCase().startsWith("event ")) {
                handleEvent(input.substring(6)); // everything after "event "
            } else if (input.equalsIgnoreCase("list")) {
                handleList();
            } else if (input.toLowerCase().startsWith("mark ")) {
                handleMark(input.substring(5)); // everything after "mark "
            } else if (input.toLowerCase().startsWith("unmark ")) {
                handleUnmark(input.substring(7)); // everything after "unmark "
            } else {
                System.out.println("____________________________________________________________");
                System.out.println("Le-king: " + input);
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
                System.out.println(" Invalid task number!");
                System.out.println("____________________________________________________________");
            }
        } catch (NumberFormatException e) {
            System.out.println("____________________________________________________________");
            System.out.println(" Please provide a valid task number!");
            System.out.println("____________________________________________________________");
        }
    }

    private void handleUnmark(String indexStr) {
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
                System.out.println(" Invalid task number!");
                System.out.println("____________________________________________________________");
            }
        } catch (NumberFormatException e) {
            System.out.println("____________________________________________________________");
            System.out.println(" Please provide a valid task number!");
            System.out.println("____________________________________________________________");
        }
    }

    private void handleTodo(String description) {
        Todo todo = new Todo(description);
        itemStore.addItem(todo);
        System.out.println("____________________________________________________________");
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + todo.toString());
        System.out.println(" Now you have " + itemStore.readItems().size() + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    private void handleDeadline(String input) {
        String[] parts = input.split(" /by ", 2);
        if (parts.length == 2) {
            String description = parts[0].trim();
            String by = parts[1].trim();
            Deadline deadline = new Deadline(description, by);
            itemStore.addItem(deadline);
            System.out.println("____________________________________________________________");
            System.out.println(" Got it. I've added this task:");
            System.out.println("   " + deadline.toString());
            System.out.println(" Now you have " + itemStore.readItems().size() + " tasks in the list.");
            System.out.println("____________________________________________________________");
        } else {
            System.out.println("____________________________________________________________");
            System.out.println(" Please use format: deadline <description> /by <date>");
            System.out.println("____________________________________________________________");
        }
    }

    private void handleEvent(String input) {
        String[] parts = input.split(" /from ", 2);
        if (parts.length == 2) {
            String description = parts[0].trim();
            String[] timeParts = parts[1].split(" /to ", 2);
            if (timeParts.length == 2) {
                String from = timeParts[0].trim();
                String to = timeParts[1].trim();
                Event event = new Event(description, from, to);
                itemStore.addItem(event);
                System.out.println("____________________________________________________________");
                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + event.toString());
                System.out.println(" Now you have " + itemStore.readItems().size() + " tasks in the list.");
                System.out.println("____________________________________________________________");
            } else {
                System.out.println("____________________________________________________________");
                System.out.println(" Please use format: event <description> /from <start> /to <end>");
                System.out.println("____________________________________________________________");
            }
        } else {
            System.out.println("____________________________________________________________");
            System.out.println(" Please use format: event <description> /from <start> /to <end>");
            System.out.println("____________________________________________________________");
        }
    }
}