import java.util.Scanner;
import java.util.List;

public class Ui {
    private Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        String welcomeMsg = """
                ____________________________________________________________
                Hello! I'm lebron (but you can call me GOAT or leking, even pookiebron if your feeling like it)
                What can I do for you?
                ____________________________________________________________
                """;
        System.out.println(welcomeMsg);
    }

    public void showGoodbye() {
        String goodbyeMsg = """
                ___________________________________________________________
                 Bye. Hope to see you again soon!
                ____________________________________________________________
                """;
        System.out.println(goodbyeMsg);
    }

    public String readCommand() {
        System.out.print("You: ");
        return scanner.nextLine().trim();
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    public void showError(String message) {
        showLine();
        System.out.println(" OOPS!!! " + message);
        showLine();
    }

    public void showTaskAdded(Task task, int totalTasks) {
        showLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task.toString());
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
        showLine();
    }

    public void showTaskDeleted(Task task, int totalTasks) {
        showLine();
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task.toString());
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
        showLine();
    }

    public void showTaskMarked(Task task) {
        showLine();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task.toString());
        showLine();
    }

    public void showTaskUnmarked(Task task) {
        showLine();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task.toString());
        showLine();
    }

    public void showTaskList(List<Task> tasks) {
        showLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i).toString());
        }
        showLine();
    }

    public void showUnknownCommand() {
        showLine();
        System.out.println(" OOPS!!! I'm sorry, but I don't know what that means :-(");
        System.out.println(" Try 'list', 'todo <description>', 'deadline <desc> /by <date>',");
        System.out.println(" 'event <desc> /from <start> /to <end>', 'mark <number>',");
        System.out.println(" 'unmark <number>', 'delete <number>', or 'bye'.");
        showLine();
    }

    public void close() {
        scanner.close();
    }
}