package lebron.ui;

import java.util.List;
import javafx.scene.control.TextArea;
import lebron.task.Task;
import lebron.data.FreeTimeSlot;

/**
 * GUI version of the Ui class that outputs to a JavaFX TextArea instead of console.
 * 
 * This class provides the same interface as the original Ui class but directs
 * all output to a TextArea component for the GUI interface.
 */
public class GuiUi {
    private TextArea chatHistory;

    public GuiUi(TextArea chatHistory) {
        this.chatHistory = chatHistory;
    }

    public void showWelcome() {
        String welcomeMsg = """
                ____________________________________________________________
                Hello! I'm lebron (but you can call me GOAT or leking, even pookiebron if your feeling like it)
                What can I do for you?
                ____________________________________________________________
                """;
        chatHistory.appendText(welcomeMsg + "\n");
        scrollToBottom();
    }

    public void showGoodbye() {
        String goodbyeMsg = """
                ___________________________________________________________
                 Bye. Hope to see you again soon!
                ____________________________________________________________
                """;
        chatHistory.appendText(goodbyeMsg + "\n");
        scrollToBottom();
    }

    public void showLine() {
        chatHistory.appendText("____________________________________________________________\n");
        scrollToBottom();
    }

    public void showError(String message) {
        showLine();
        chatHistory.appendText(" OOPS!!! " + message + "\n");
        showLine();
        scrollToBottom();
    }

    public void showTaskAdded(Task task, int totalTasks) {
        showLine();
        chatHistory.appendText(" Got it. I've added this task:\n");
        chatHistory.appendText("   " + task.toString() + "\n");
        chatHistory.appendText(" Now you have " + totalTasks + " tasks in the list.\n");
        showLine();
        scrollToBottom();
    }

    public void showTaskDeleted(Task task, int totalTasks) {
        showLine();
        chatHistory.appendText(" Noted. I've removed this task:\n");
        chatHistory.appendText("   " + task.toString() + "\n");
        chatHistory.appendText(" Now you have " + totalTasks + " tasks in the list.\n");
        showLine();
        scrollToBottom();
    }

    public void showTaskMarked(Task task) {
        showLine();
        chatHistory.appendText(" Nice! I've marked this task as done:\n");
        chatHistory.appendText("   " + task.toString() + "\n");
        showLine();
        scrollToBottom();
    }

    public void showTaskUnmarked(Task task) {
        showLine();
        chatHistory.appendText(" OK, I've marked this task as not done yet:\n");
        chatHistory.appendText("   " + task.toString() + "\n");
        showLine();
        scrollToBottom();
    }

    public void showTaskList(List<Task> tasks) {
        showLine();
        chatHistory.appendText(" Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            chatHistory.appendText(" " + (i + 1) + "." + tasks.get(i).toString() + "\n");
        }
        showLine();
        scrollToBottom();
    }

    public void showUnknownCommand() {
        showLine();
        chatHistory.appendText(" OOPS!!! I'm sorry, but I don't know what that means :-(\n");
        chatHistory.appendText(" Try 'list', 'todo <description>', 'deadline <desc> /by <date>',\n");
        chatHistory.appendText(" 'event <desc> /from <start> /to <end>', 'mark <number>',\n");
        chatHistory.appendText(" 'unmark <number>', 'delete <number>', 'find <keyword>', or 'bye'.\n");
        showLine();
        scrollToBottom();
    }

    public void showFindResults(List<Task> matchingTasks, String keyword) {
        showLine();
        if (matchingTasks.isEmpty()) {
            chatHistory.appendText(" No matching tasks found with keyword: " + keyword + "\n");
        } else {
            chatHistory.appendText(" Here are the matching tasks in your list:\n");
            for (int i = 0; i < matchingTasks.size(); i++) {
                chatHistory.appendText(" " + (i + 1) + "." + matchingTasks.get(i).toString() + "\n");
            }
        }
        showLine();
        scrollToBottom();
    }

    public void showFreeTimeResult(FreeTimeSlot freeSlot, int hoursNeeded) {
        showLine();
        if (freeSlot == null) {
            chatHistory.appendText(" Sorry, I couldn't find any free time slots of " + hoursNeeded + " hours.\n");
        } else {
            chatHistory.appendText(" Found a free time slot:\n");
            chatHistory.appendText("   " + freeSlot.toString() + "\n");
        }
        showLine();
        scrollToBottom();
    }

    private void scrollToBottom() {
        chatHistory.setScrollTop(Double.MAX_VALUE);
    }

    public void close() {
        // No scanner to close in GUI version
    }
}