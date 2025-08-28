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

        if (trimmed.toLowerCase().startsWith("todo")) {
            if (trimmed.length() > 4 && trimmed.charAt(4) == ' ') {
                return new Command(CommandType.TODO, trimmed.substring(5));
            } else if (trimmed.equalsIgnoreCase("todo")) {
                return new Command(CommandType.TODO, "");
            } else {
                return new Command(CommandType.TODO, trimmed.substring(4));
            }
        }

        if (trimmed.toLowerCase().startsWith("deadline")) {
            if (trimmed.length() > 8 && trimmed.charAt(8) == ' ') {
                return new Command(CommandType.DEADLINE, trimmed.substring(9));
            } else if (trimmed.equalsIgnoreCase("deadline")) {
                return new Command(CommandType.DEADLINE, "");
            } else {
                return new Command(CommandType.DEADLINE, trimmed.substring(8));
            }
        }

        if (trimmed.toLowerCase().startsWith("event")) {
            if (trimmed.length() > 5 && trimmed.charAt(5) == ' ') {
                return new Command(CommandType.EVENT, trimmed.substring(6));
            } else if (trimmed.equalsIgnoreCase("event")) {
                return new Command(CommandType.EVENT, "");
            } else {
                return new Command(CommandType.EVENT, trimmed.substring(5));
            }
        }

        if (trimmed.toLowerCase().startsWith("mark")) {
            if (trimmed.length() > 4 && trimmed.charAt(4) == ' ') {
                return new Command(CommandType.MARK, trimmed.substring(5));
            } else if (trimmed.equalsIgnoreCase("mark")) {
                return new Command(CommandType.MARK, "");
            } else {
                return new Command(CommandType.MARK, trimmed.substring(4));
            }
        }

        if (trimmed.toLowerCase().startsWith("unmark")) {
            if (trimmed.length() > 6 && trimmed.charAt(6) == ' ') {
                return new Command(CommandType.UNMARK, trimmed.substring(7));
            } else if (trimmed.equalsIgnoreCase("unmark")) {
                return new Command(CommandType.UNMARK, "");
            } else {
                return new Command(CommandType.UNMARK, trimmed.substring(6));
            }
        }

        if (trimmed.toLowerCase().startsWith("delete")) {
            if (trimmed.length() > 6 && trimmed.charAt(6) == ' ') {
                return new Command(CommandType.DELETE, trimmed.substring(7));
            } else if (trimmed.equalsIgnoreCase("delete")) {
                return new Command(CommandType.DELETE, "");
            } else {
                return new Command(CommandType.DELETE, trimmed.substring(6));
            }
        }

        if (trimmed.toLowerCase().startsWith("find")) {
            if (trimmed.length() > 4 && trimmed.charAt(4) == ' ') {
                return new Command(CommandType.FIND, trimmed.substring(5));
            } else if (trimmed.equalsIgnoreCase("find")) {
                return new Command(CommandType.FIND, "");
            } else {
                return new Command(CommandType.FIND, trimmed.substring(4));
            }
        }

        return new Command(CommandType.UNKNOWN, trimmed);
    }
}