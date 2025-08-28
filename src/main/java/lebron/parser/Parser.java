package lebron.parser;

public class Parser {
    
    public enum CommandType {
        TODO, DEADLINE, EVENT, LIST, MARK, UNMARK, DELETE, FIND, BYE, UNKNOWN
    }

    public static class Command {
        private CommandType type;
        private String argument;

        public Command(CommandType type, String argument) {
            this.type = type;
            this.argument = argument;
        }

        public CommandType getType() {
            return type;
        }

        public String getArgument() {
            return argument;
        }
    }

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