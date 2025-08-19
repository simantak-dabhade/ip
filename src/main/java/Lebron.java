import java.util.Scanner;

public class Lebron {
    public static void main(String[] args) {
        Lebron lebron = new Lebron();
        lebron.welcomeMessage();
        lebron.chatLoop();
        lebron.goodbyeMessage();
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
            String input = scanner.nextLine();

            // breaking out if the person types bye
            if (input.equalsIgnoreCase("bye")) {
                break;
            }

            System.out.println("____________________________________________________________");
            System.out.println("Le-king: " + input); // echo back
            System.out.println("____________________________________________________________");
        }

        scanner.close();
    }

}
