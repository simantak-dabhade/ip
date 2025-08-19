import java.util.Scanner;
import java.util.List;

public class Lebron {
    public static void main(String[] args) {
        Lebron lebron = new Lebron();
        lebron.welcomeMessage();
        lebron.chatLoop();
        lebron.goodbyeMessage();
    }

    private ItemStore itemStore;

    public Lebron() {
        itemStore = new ItemStore();
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
            } else {
                System.out.println("____________________________________________________________");
                System.out.println("Le-king: " + input);
                System.out.println("____________________________________________________________");
            }
        }

        scanner.close();
    }

    private void handleRead(String item) {
        itemStore.addItem(item);
        System.out.println("____________________________________________________________");
        System.out.println(" added: " + item);
        System.out.println("____________________________________________________________");
    }

    private void handleList() {
        List<String> items = itemStore.readItems();
        System.out.println("____________________________________________________________");
        for (int i = 0; i < items.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + items.get(i));
        }
        System.out.println("____________________________________________________________");
    }
}