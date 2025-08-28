package lebron.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void parse_byeCommand_returnsByeCommand() {
        Parser.Command command = Parser.parse("bye");
        assertEquals(Parser.CommandType.BYE, command.getType());
        assertEquals("", command.getArgument());
    }

    @Test
    void parse_byeCommandWithDifferentCase_returnsByeCommand() {
        Parser.Command command = Parser.parse("BYE");
        assertEquals(Parser.CommandType.BYE, command.getType());
        assertEquals("", command.getArgument());
        
        Parser.Command command2 = Parser.parse("Bye");
        assertEquals(Parser.CommandType.BYE, command2.getType());
        assertEquals("", command2.getArgument());
    }

    @Test
    void parse_listCommand_returnsListCommand() {
        Parser.Command command = Parser.parse("list");
        assertEquals(Parser.CommandType.LIST, command.getType());
        assertEquals("", command.getArgument());
    }

    @Test
    void parse_todoCommandWithDescription_returnsTodoCommand() {
        Parser.Command command = Parser.parse("todo read book");
        assertEquals(Parser.CommandType.TODO, command.getType());
        assertEquals("read book", command.getArgument());
    }

    @Test
    void parse_todoCommandEmpty_returnsTodoCommandWithEmptyArgument() {
        Parser.Command command = Parser.parse("todo");
        assertEquals(Parser.CommandType.TODO, command.getType());
        assertEquals("", command.getArgument());
    }

    @Test
    void parse_todoCommandWithoutSpace_returnsTodoCommand() {
        Parser.Command command = Parser.parse("todoX");
        assertEquals(Parser.CommandType.TODO, command.getType());
        assertEquals("X", command.getArgument());
    }

    @Test
    void parse_deadlineCommandWithDescription_returnsDeadlineCommand() {
        Parser.Command command = Parser.parse("deadline return book /by tomorrow");
        assertEquals(Parser.CommandType.DEADLINE, command.getType());
        assertEquals("return book /by tomorrow", command.getArgument());
    }

    @Test
    void parse_deadlineCommandEmpty_returnsDeadlineCommandWithEmptyArgument() {
        Parser.Command command = Parser.parse("deadline");
        assertEquals(Parser.CommandType.DEADLINE, command.getType());
        assertEquals("", command.getArgument());
    }

    @Test
    void parse_eventCommandWithDescription_returnsEventCommand() {
        Parser.Command command = Parser.parse("event meeting /from 2pm /to 4pm");
        assertEquals(Parser.CommandType.EVENT, command.getType());
        assertEquals("meeting /from 2pm /to 4pm", command.getArgument());
    }

    @Test
    void parse_markCommandWithNumber_returnsMarkCommand() {
        Parser.Command command = Parser.parse("mark 1");
        assertEquals(Parser.CommandType.MARK, command.getType());
        assertEquals("1", command.getArgument());
    }

    @Test
    void parse_markCommandEmpty_returnsMarkCommandWithEmptyArgument() {
        Parser.Command command = Parser.parse("mark");
        assertEquals(Parser.CommandType.MARK, command.getType());
        assertEquals("", command.getArgument());
    }

    @Test
    void parse_unmarkCommandWithNumber_returnsUnmarkCommand() {
        Parser.Command command = Parser.parse("unmark 2");
        assertEquals(Parser.CommandType.UNMARK, command.getType());
        assertEquals("2", command.getArgument());
    }

    @Test
    void parse_deleteCommandWithNumber_returnsDeleteCommand() {
        Parser.Command command = Parser.parse("delete 3");
        assertEquals(Parser.CommandType.DELETE, command.getType());
        assertEquals("3", command.getArgument());
    }

    @Test
    void parse_unknownCommand_returnsUnknownCommand() {
        Parser.Command command = Parser.parse("invalid command");
        assertEquals(Parser.CommandType.UNKNOWN, command.getType());
        assertEquals("invalid command", command.getArgument());
    }

    @Test
    void parse_emptyString_returnsUnknownCommand() {
        Parser.Command command = Parser.parse("");
        assertEquals(Parser.CommandType.UNKNOWN, command.getType());
        assertEquals("", command.getArgument());
    }

    @Test
    void parse_nullInput_returnsUnknownCommand() {
        Parser.Command command = Parser.parse(null);
        assertEquals(Parser.CommandType.UNKNOWN, command.getType());
        assertEquals("", command.getArgument());
    }

    @Test
    void parse_whitespaceOnlyInput_returnsUnknownCommand() {
        Parser.Command command = Parser.parse("   ");
        assertEquals(Parser.CommandType.UNKNOWN, command.getType());
        assertEquals("", command.getArgument());
    }

    @Test
    void parse_commandWithLeadingAndTrailingSpaces_trimsSpaces() {
        Parser.Command command = Parser.parse("  todo   read book  ");
        assertEquals(Parser.CommandType.TODO, command.getType());
        assertEquals("  read book", command.getArgument());
    }

    @Test
    void parse_caseInsensitiveCommands_workCorrectly() {
        Parser.Command command1 = Parser.parse("TODO read book");
        assertEquals(Parser.CommandType.TODO, command1.getType());
        
        Parser.Command command2 = Parser.parse("DEADLINE homework /by tomorrow");
        assertEquals(Parser.CommandType.DEADLINE, command2.getType());
        
        Parser.Command command3 = Parser.parse("LIST");
        assertEquals(Parser.CommandType.LIST, command3.getType());
    }

    @Test
    void parse_edgeCaseCommandsWithoutSpaces_handledCorrectly() {
        // Test commands that are exactly at the boundary
        Parser.Command command1 = Parser.parse("markX");
        assertEquals(Parser.CommandType.MARK, command1.getType());
        assertEquals("X", command1.getArgument());
        
        Parser.Command command2 = Parser.parse("deleteabc");
        assertEquals(Parser.CommandType.DELETE, command2.getType());
        assertEquals("abc", command2.getArgument());
    }
}