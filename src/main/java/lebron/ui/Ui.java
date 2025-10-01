package lebron.ui;

import java.util.Scanner;
import java.util.List;
import lebron.task.Task;
import lebron.data.FreeTimeSlot;

/**
 * The Ui class handles all the conversation between you and Lebron!
 *
 * This is where the personality shines through - from the friendly welcome message
 * to the helpful error messages. It knows how to format everything nicely and
 * keeps the interface consistent throughout your chat session.
 */
public class Ui {
    private final Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Shows the friendly welcome message when you first start chatting.
     *
     */
    public void showWelcome() {
        String welcomeMsg = """
                ____________________________________________________________
                Hello! I'm lebron (but you can call me GOAT or leking, even pookiebron if your feeling like it)
                What can I do for you?
                ____________________________________________________________
                """;
        System.out.println(welcomeMsg);
    }

    /**
     * Shows a nice goodbye message when you're ready to leave the application.
     */
    public void showGoodbye() {
        String goodbyeMsg = """
                ___________________________________________________________
                 Bye. Hope to see you again soon!
                ____________________________________________________________
                """;
        System.out.println(goodbyeMsg);
    }

    /**
     * Reads your next command from the keyboard.
     * 
     * @return what you typed, cleaned up and ready to process
     */
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
        assert task != null : "Task should not be null when showing task added message";
        assert totalTasks > 0 : "Total task count should be positive after adding a task";
        
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

    private void printLines(String... lines) {
        for (String line : lines) {
            System.out.println(line);
        }
    }

    public void showUnknownCommand() {
        showLine();
        printLines(" OOPS!!! I'm sorry, but I don't know what that means :-(",
                  " Try 'list', 'todo <description>', 'deadline <desc> /by <date>',",
                  " 'event <desc> /from <start> /to <end>', 'mark <number>',",
                  " 'unmark <number>', 'delete <number>', 'find <keyword>',",
                  " 'freetime <hours>', or 'bye'.");
        showLine();
    }

    /**
     * Shows the results of a search operation.
     * 
     * If tasks were found, displays them in a nice numbered list.
     * If nothing matched, lets you know in a friendly way.
     * 
     * @param matchingTasks the tasks that matched your search
     * @param keyword what you were searching for (for the message)
     */
    public void showFindResults(List<Task> matchingTasks, String keyword) {
        showLine();
        if (matchingTasks.isEmpty()) {
            System.out.println(" No matching tasks found with keyword: " + keyword);
        } else {
            System.out.println(" Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + matchingTasks.get(i).toString());
            }
        }
        showLine();
    }

    /**
     * Shows the result of a free time search.
     * 
     * If a free slot was found, displays the time and duration.
     * If no free time is available, provides a helpful message.
     * 
     * @param freeSlot the free time slot found, or null if none available
     * @param hoursRequested the number of hours that were requested
     */
    public void showFreeTimeResult(FreeTimeSlot freeSlot, int hoursRequested) {
        showLine();
        if (freeSlot == null) {
            System.out.println(" Sorry, I couldn't find a " + hoursRequested + " hour free slot in the next 2 weeks.");
            System.out.println(" Your schedule is pretty packed! Maybe consider shorter time blocks?");
        } else {
            System.out.println(" Great news! I found a " + hoursRequested + " hour free slot for you:");
            System.out.println(" " + freeSlot.toString());
        }
        showLine();
    }

    /**
     * Cleans up resources when the chat session ends.
     */
    public void close() {
        scanner.close();
    }
}
