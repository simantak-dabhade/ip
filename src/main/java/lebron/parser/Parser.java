package lebron.parser;

/**
 * The Parser class handles the tricky business of understanding what you're trying to say!
 * 
 * It takes your raw input and transforms it into something the chatbot can work with.
 * This class is pretty smart - it can handle typos, case differences, and various
 * ways you might phrase commands.
 */
public class Parser {
    
    /**
     * All the different types of commands that Lebron understands.
     * 
     * From simple todos to complex events, we've got you covered.
     * If we don't recognize something, it gets labeled as UNKNOWN.
     */
    public enum CommandType {
        TODO, DEADLINE, EVENT, LIST, MARK, UNMARK, DELETE, FIND, BYE, UNKNOWN
    }

    /**
     * A neat little package that holds a parsed command.
     * 
     * Think of it as a structured way to represent what you want to do
     * and any details that go with it.
     */
    public static class Command {
        private final CommandType type;
        private final String argument;

        public Command(CommandType type, String argument) {
            this.type = type;
            this.argument = argument;
        }

        /**
         * Gets the type of command (todo, deadline, etc.)
         * @return the command type
         */
        public CommandType getType() {
            return type;
        }

        /**
         * Gets any additional info that came with the command
         * @return the argument string, or empty string if none
         */
        public String getArgument() {
            return argument;
        }
    }

    /**
     * The main parsing method - this is where the magic happens!
     * 
     * Takes whatever you type and figures out what you mean. It's pretty flexible -
     * handles different cases, extra spaces, and various ways of writing commands.
     * When in doubt, it tries to be helpful rather than strict.
     * 
     * @param input what you typed in the chat
     * @return a Command object with the parsed command type and arguments
     */
    public static Command parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new Command(CommandType.UNKNOWN, "");
        }

        String trimmed = input.trim();

        if (trimmed.equalsIgnoreCase("bye")) {
            return new Command(CommandType.BYE, "");
        }

        if (trimmed.equalsIgnoreCase("list")) {
            return new Command(CommandType.LIST, "");
        }

        Command todoCommand = parseCommandWithKeyword(trimmed, "todo", CommandType.TODO);
        if (todoCommand != null) return todoCommand;

        Command deadlineCommand = parseCommandWithKeyword(trimmed, "deadline", CommandType.DEADLINE);
        if (deadlineCommand != null) return deadlineCommand;

        Command eventCommand = parseCommandWithKeyword(trimmed, "event", CommandType.EVENT);
        if (eventCommand != null) return eventCommand;

        Command markCommand = parseCommandWithKeyword(trimmed, "mark", CommandType.MARK);
        if (markCommand != null) return markCommand;

        Command unmarkCommand = parseCommandWithKeyword(trimmed, "unmark", CommandType.UNMARK);
        if (unmarkCommand != null) return unmarkCommand;

        Command deleteCommand = parseCommandWithKeyword(trimmed, "delete", CommandType.DELETE);
        if (deleteCommand != null) return deleteCommand;

        Command findCommand = parseCommandWithKeyword(trimmed, "find", CommandType.FIND);
        if (findCommand != null) return findCommand;

        return new Command(CommandType.UNKNOWN, trimmed);
    }

    /**
     * Helper method to parse commands that follow the pattern: keyword [argument]
     * 
     * @param input the trimmed input string
     * @param keyword the command keyword to match
     * @param type the command type to return
     * @return a Command object if the keyword matches, null otherwise
     */
    private static Command parseCommandWithKeyword(String input, String keyword, CommandType type) {
        if (!input.toLowerCase().startsWith(keyword.toLowerCase())) {
            return null;
        }

        if (input.equalsIgnoreCase(keyword)) {
            return new Command(type, "");
        }

        if (input.length() > keyword.length() && input.charAt(keyword.length()) == ' ') {
            return new Command(type, input.substring(keyword.length() + 1));
        } else if (input.length() > keyword.length()) {
            return new Command(type, input.substring(keyword.length()));
        }

        return new Command(type, "");
    }
}