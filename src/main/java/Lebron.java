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
        itemStore.addItem(new Task(item));
        System.out.println("____________________________________________________________");
        System.out.println(" added: " + item);
        System.out.println("____________________________________________________________");
    }

    private void handleList() {
        List<Task> items = itemStore.readItems();
        System.out.println("____________________________________________________________");
        for (int i = 0; i < items.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + items.get(i).toString());
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
}