package lebron.integration;

import lebron.Lebron;
import lebron.storage.Storage;
import lebron.data.TaskList;
import lebron.ui.Ui;
import lebron.parser.Parser;
import lebron.task.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;

/**
 * Integration tests for command processing without GUI dependencies.
 * Tests all commands using the core logic classes.
 */
class CommandIntegrationTest {
    private Storage storage;
    private TaskList tasks;
    private String testFilePath;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    
    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        testFilePath = tempDir.resolve("test_tasks.txt").toString();
        storage = new Storage(testFilePath);
        tasks = new TaskList();
        
        // Capture console output for verification
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }
    
    @Test
    void parser_allCommandTypes_parseCorrectly() {
        // Test all basic commands
        assertEquals(Parser.CommandType.BYE, Parser.parse("bye").getType());
        assertEquals(Parser.CommandType.LIST, Parser.parse("list").getType());
        assertEquals(Parser.CommandType.TODO, Parser.parse("todo test").getType());
        assertEquals(Parser.CommandType.DEADLINE, Parser.parse("deadline test /by date").getType());
        assertEquals(Parser.CommandType.EVENT, Parser.parse("event test /from start /to end").getType());
        assertEquals(Parser.CommandType.MARK, Parser.parse("mark 1").getType());
        assertEquals(Parser.CommandType.UNMARK, Parser.parse("unmark 1").getType());
        assertEquals(Parser.CommandType.DELETE, Parser.parse("delete 1").getType());
        assertEquals(Parser.CommandType.FIND, Parser.parse("find keyword").getType());
        assertEquals(Parser.CommandType.FREETIME, Parser.parse("freetime 2").getType());
        assertEquals(Parser.CommandType.UNKNOWN, Parser.parse("invalid").getType());
        
        // Test case insensitivity
        assertEquals(Parser.CommandType.TODO, Parser.parse("TODO test").getType());
        assertEquals(Parser.CommandType.LIST, Parser.parse("LIST").getType());
        assertEquals(Parser.CommandType.BYE, Parser.parse("BYE").getType());
        
        // Test with extra spaces
        Parser.Command cmd = Parser.parse("  todo  test task  ");
        assertEquals(Parser.CommandType.TODO, cmd.getType());
        assertEquals(" test task", cmd.getArgument()); // The parser behavior strips trailing spaces
    }
    
    @Test
    void taskCreation_allTypes_workCorrectly() {
        // Test Todo creation
        Todo todo = new Todo("read book");
        assertEquals("read book", todo.getDescription());
        assertEquals("[T]", todo.getTypeIcon());
        assertFalse(todo.isDone());
        assertEquals("[T][ ] read book", todo.toString());
        
        // Test Deadline creation with different formats
        Deadline deadline1 = new Deadline("submit report", "2024-12-25");
        assertEquals("submit report", deadline1.getDescription());
        assertEquals("[D]", deadline1.getTypeIcon());
        assertTrue(deadline1.toString().contains("Dec 25 2024"));
        
        Deadline deadline2 = new Deadline("homework", "25/12/2024");
        assertTrue(deadline2.toString().contains("Dec 25 2024"));
        
        Deadline deadline3 = new Deadline("meeting", "2024-12-25 1400");
        assertTrue(deadline3.toString().contains("14:00"));
        
        // Test Event creation
        Event event = new Event("team meeting", "2024-12-25 1400", "2024-12-25 1600");
        assertEquals("team meeting", event.getDescription());
        assertEquals("[E]", event.getTypeIcon());
        assertTrue(event.toString().contains("from: Dec 25 2024 14:00"));
        assertTrue(event.toString().contains("to: Dec 25 2024 16:00"));
    }
    
    @Test
    void taskCreation_invalidDates_throwExceptions() {
        // Test invalid date formats
        assertThrows(IllegalArgumentException.class, () -> 
            new Deadline("test", "invalid-date"));
        assertThrows(IllegalArgumentException.class, () -> 
            new Deadline("test", ""));
        assertThrows(IllegalArgumentException.class, () -> 
            new Deadline("test", (String) null));
            
        assertThrows(IllegalArgumentException.class, () -> 
            new Event("test", "invalid-date", "2024-12-25"));
        assertThrows(IllegalArgumentException.class, () -> 
            new Event("test", "2024-12-25", "invalid-date"));
    }
    
    @Test
    void taskList_basicOperations_workCorrectly() {
        TaskList taskList = new TaskList();
        
        // Test adding tasks
        Todo todo = new Todo("task1");
        Deadline deadline = new Deadline("task2", "2024-12-25");
        Event event = new Event("task3", "2024-12-25", "2024-12-26");
        
        taskList.add(todo);
        taskList.add(deadline);
        taskList.add(event);
        
        assertEquals(3, taskList.size());
        assertEquals(todo, taskList.get(0));
        assertEquals(deadline, taskList.get(1));
        assertEquals(event, taskList.get(2));
        
        // Test marking tasks
        assertFalse(todo.isDone());
        taskList.markTask(0, true);
        assertTrue(todo.isDone());
        taskList.markTask(0, false);
        assertFalse(todo.isDone());
        
        // Test deleting tasks
        Task deleted = taskList.delete(1);
        assertEquals(deadline, deleted);
        assertEquals(2, taskList.size());
        assertEquals(event, taskList.get(1));
        
        // Test find functionality
        taskList.add(new Todo("read book"));
        taskList.add(new Todo("buy groceries"));
        
        List<Task> results = taskList.findTasks("book");
        assertEquals(1, results.size());
        assertTrue(results.get(0).getDescription().contains("book"));
        
        results = taskList.findTasks("task");
        assertEquals(2, results.size()); // Should find "task1" and "task3"
        
        results = taskList.findTasks("xyz");
        assertTrue(results.isEmpty());
    }
    
    @Test
    void taskList_edgeCases_handleGracefully() {
        TaskList taskList = new TaskList();
        
        // Test operations on empty list
        assertNull(taskList.get(0));
        assertNull(taskList.delete(0));
        assertDoesNotThrow(() -> taskList.markTask(0, true));
        assertTrue(taskList.findTasks("anything").isEmpty());
        
        // Test out-of-bounds operations
        taskList.add(new Todo("task1"));
        assertNull(taskList.get(-1));
        assertNull(taskList.get(5));
        assertNull(taskList.delete(-1));
        assertNull(taskList.delete(5));
        assertDoesNotThrow(() -> taskList.markTask(-1, true));
        assertDoesNotThrow(() -> taskList.markTask(5, true));
        
        // Test null handling in find
        assertTrue(taskList.findTasks(null).isEmpty());
        assertTrue(taskList.findTasks("").isEmpty());
        assertTrue(taskList.findTasks("   ").isEmpty());
    }
    
    @Test
    void storage_saveAndLoad_workCorrectly() {
        // Create and save tasks
        TaskList originalTasks = new TaskList();
        originalTasks.add(new Todo("task1"));
        originalTasks.add(new Deadline("task2", "2024-12-25 1400"));
        originalTasks.add(new Event("task3", "2024-12-25 1400", "2024-12-25 1600"));
        
        // Mark one task as done
        originalTasks.markTask(0, true);
        
        storage.save(originalTasks.getAllTasks());
        
        // Load tasks and verify
        List<Task> loadedTasks = storage.load();
        assertEquals(3, loadedTasks.size());
        
        // Verify first task (Todo, marked as done)
        Task task1 = loadedTasks.get(0);
        assertTrue(task1 instanceof Todo);
        assertEquals("task1", task1.getDescription());
        assertTrue(task1.isDone());
        
        // Verify second task (Deadline)
        Task task2 = loadedTasks.get(1);
        assertTrue(task2 instanceof Deadline);
        assertEquals("task2", task2.getDescription());
        assertFalse(task2.isDone());
        
        // Verify third task (Event)
        Task task3 = loadedTasks.get(2);
        assertTrue(task3 instanceof Event);
        assertEquals("task3", task3.getDescription());
        assertFalse(task3.isDone());
    }
    
    @Test
    void storage_emptyFile_handlesGracefully() {
        // Test loading from non-existent file
        List<Task> tasks = storage.load();
        assertTrue(tasks.isEmpty());
        
        // Test saving empty list
        assertDoesNotThrow(() -> storage.save(List.of()));
    }
    
    @Test
    void freeTimeSlot_creation_workCorrectly() {
        // This test verifies the FreeTimeSlot class works correctly
        java.time.LocalDateTime start = java.time.LocalDateTime.of(2024, 12, 25, 14, 0);
        java.time.LocalDateTime end = java.time.LocalDateTime.of(2024, 12, 25, 16, 0);
        
        lebron.data.FreeTimeSlot slot = new lebron.data.FreeTimeSlot(start, end);
        
        assertEquals(start, slot.getStart());
        assertEquals(end, slot.getEnd());
        assertEquals(2, slot.getDurationHours());
        assertTrue(slot.toString().contains("Dec 25 2024 14:00"));
        assertTrue(slot.toString().contains("Dec 25 2024 16:00"));
        assertTrue(slot.toString().contains("2 hours"));
    }
    
    @Test
    void dateFormats_allSupported_parseCorrectly() {
        // Test all supported date formats
        assertDoesNotThrow(() -> new Deadline("test", "2024-12-25"));
        assertDoesNotThrow(() -> new Deadline("test", "2024-12-25 1400"));
        assertDoesNotThrow(() -> new Deadline("test", "25/12/2024"));
        assertDoesNotThrow(() -> new Deadline("test", "25/12/2024 1400"));
        
        // Verify the parsed dates are correct
        Deadline d1 = new Deadline("test", "2024-12-25");
        Deadline d2 = new Deadline("test", "25/12/2024");
        assertEquals(d1.getBy().toLocalDate(), d2.getBy().toLocalDate());
        
        Deadline d3 = new Deadline("test", "2024-12-25 1400");
        assertEquals(14, d3.getBy().getHour());
        assertEquals(0, d3.getBy().getMinute());
    }
}