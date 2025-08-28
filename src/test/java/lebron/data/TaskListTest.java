package lebron.data;

import lebron.task.Task;
import lebron.task.Todo;
import lebron.task.Deadline;
import lebron.task.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class TaskListTest {
    private TaskList taskList;
    private Task todoTask;
    private Task deadlineTask;
    private Task eventTask;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
        todoTask = new Todo("read book");
        deadlineTask = new Deadline("submit assignment", "2024-12-25 1400");
        eventTask = new Event("project meeting", "2024-12-20 1400", "2024-12-20 1600");
    }

    @Test
    void add_singleTask_increasesSize() {
        assertEquals(0, taskList.size());
        taskList.add(todoTask);
        assertEquals(1, taskList.size());
    }

    @Test
    void add_multipleTasks_increasesSize() {
        taskList.add(todoTask);
        taskList.add(deadlineTask);
        taskList.add(eventTask);
        assertEquals(3, taskList.size());
    }

    @Test
    void add_nullTask_handledGracefully() {
        // Test that adding null doesn't crash the system
        taskList.add(null);
        assertEquals(1, taskList.size());
        assertNull(taskList.get(0));
    }

    @Test
    void get_validIndex_returnsCorrectTask() {
        taskList.add(todoTask);
        taskList.add(deadlineTask);
        
        assertEquals(todoTask, taskList.get(0));
        assertEquals(deadlineTask, taskList.get(1));
    }

    @Test
    void get_invalidNegativeIndex_returnsNull() {
        taskList.add(todoTask);
        assertNull(taskList.get(-1));
    }

    @Test
    void get_invalidLargeIndex_returnsNull() {
        taskList.add(todoTask);
        assertNull(taskList.get(5));
        assertNull(taskList.get(100));
    }

    @Test
    void get_emptyList_returnsNull() {
        assertNull(taskList.get(0));
        assertNull(taskList.get(-1));
        assertNull(taskList.get(1));
    }

    @Test
    void delete_validIndex_removesTaskAndReturnsIt() {
        taskList.add(todoTask);
        taskList.add(deadlineTask);
        assertEquals(2, taskList.size());
        
        Task deletedTask = taskList.delete(0);
        assertEquals(todoTask, deletedTask);
        assertEquals(1, taskList.size());
        assertEquals(deadlineTask, taskList.get(0));
    }

    @Test
    void delete_invalidNegativeIndex_returnsNull() {
        taskList.add(todoTask);
        Task deletedTask = taskList.delete(-1);
        assertNull(deletedTask);
        assertEquals(1, taskList.size());
    }

    @Test
    void delete_invalidLargeIndex_returnsNull() {
        taskList.add(todoTask);
        Task deletedTask = taskList.delete(5);
        assertNull(deletedTask);
        assertEquals(1, taskList.size());
    }

    @Test
    void delete_emptyList_returnsNull() {
        assertNull(taskList.delete(0));
        assertEquals(0, taskList.size());
    }

    @Test
    void delete_lastItem_listBecomesEmpty() {
        taskList.add(todoTask);
        assertEquals(1, taskList.size());
        
        Task deletedTask = taskList.delete(0);
        assertEquals(todoTask, deletedTask);
        assertEquals(0, taskList.size());
    }

    @Test
    void markTask_validIndexMarkDone_marksTaskAsDone() {
        taskList.add(todoTask);
        assertFalse(todoTask.isDone());
        
        taskList.markTask(0, true);
        assertTrue(todoTask.isDone());
    }

    @Test
    void markTask_validIndexUnmark_marksTaskAsNotDone() {
        taskList.add(todoTask);
        todoTask.setDone(true);
        assertTrue(todoTask.isDone());
        
        taskList.markTask(0, false);
        assertFalse(todoTask.isDone());
    }

    @Test
    void markTask_invalidIndex_doesNotCrash() {
        taskList.add(todoTask);
        
        // These should not throw exceptions
        assertDoesNotThrow(() -> taskList.markTask(-1, true));
        assertDoesNotThrow(() -> taskList.markTask(5, true));
        
        // Original task should be unchanged
        assertFalse(todoTask.isDone());
    }

    @Test
    void markTask_emptyList_doesNotCrash() {
        assertDoesNotThrow(() -> taskList.markTask(0, true));
        assertDoesNotThrow(() -> taskList.markTask(-1, false));
    }

    @Test
    void getAllTasks_returnsAllTasks() {
        taskList.add(todoTask);
        taskList.add(deadlineTask);
        taskList.add(eventTask);
        
        List<Task> allTasks = taskList.getAllTasks();
        assertEquals(3, allTasks.size());
        assertTrue(allTasks.contains(todoTask));
        assertTrue(allTasks.contains(deadlineTask));
        assertTrue(allTasks.contains(eventTask));
    }

    @Test
    void getAllTasks_emptyList_returnsEmptyList() {
        List<Task> allTasks = taskList.getAllTasks();
        assertTrue(allTasks.isEmpty());
    }

    @Test
    void constructor_withExistingList_copiesAllTasks() {
        List<Task> existingTasks = new ArrayList<>();
        existingTasks.add(todoTask);
        existingTasks.add(deadlineTask);
        
        TaskList newTaskList = new TaskList(existingTasks);
        assertEquals(2, newTaskList.size());
        assertEquals(todoTask, newTaskList.get(0));
        assertEquals(deadlineTask, newTaskList.get(1));
    }

    @Test
    void constructor_withNullList_createsEmptyTaskList() {
        TaskList newTaskList = new TaskList(null);
        assertEquals(0, newTaskList.size());
    }

    @Test
    void size_reflectsCurrentNumberOfTasks() {
        assertEquals(0, taskList.size());
        
        taskList.add(todoTask);
        assertEquals(1, taskList.size());
        
        taskList.add(deadlineTask);
        assertEquals(2, taskList.size());
        
        taskList.delete(0);
        assertEquals(1, taskList.size());
        
        taskList.delete(0);
        assertEquals(0, taskList.size());
    }

    @Test
    void integration_addDeleteMarkOperations_workTogether() {
        // Add multiple tasks
        taskList.add(todoTask);
        taskList.add(deadlineTask);
        taskList.add(eventTask);
        assertEquals(3, taskList.size());
        
        // Mark first task as done
        taskList.markTask(0, true);
        assertTrue(taskList.get(0).isDone());
        
        // Delete middle task
        Task deleted = taskList.delete(1);
        assertEquals(deadlineTask, deleted);
        assertEquals(2, taskList.size());
        
        // Verify remaining tasks are correct
        assertEquals(todoTask, taskList.get(0));
        assertEquals(eventTask, taskList.get(1));
        assertTrue(taskList.get(0).isDone());
        assertFalse(taskList.get(1).isDone());
    }

    @Test
    void findTasks_exactKeywordMatch_returnsMatchingTasks() {
        taskList.add(todoTask); // "read book"
        taskList.add(deadlineTask); // "submit assignment"
        taskList.add(new Todo("return book"));
        taskList.add(new Todo("buy groceries"));
        
        List<Task> results = taskList.findTasks("book");
        assertEquals(2, results.size());
        assertTrue(results.contains(todoTask));
        assertTrue(results.stream().anyMatch(task -> task.getDescription().equals("return book")));
    }

    @Test
    void findTasks_partialKeywordMatch_returnsMatchingTasks() {
        taskList.add(todoTask); // "read book"
        taskList.add(deadlineTask); // "submit assignment"
        taskList.add(new Todo("reading homework"));
        
        List<Task> results = taskList.findTasks("read");
        assertEquals(2, results.size());
        assertTrue(results.contains(todoTask));
        assertTrue(results.stream().anyMatch(task -> task.getDescription().equals("reading homework")));
    }

    @Test
    void findTasks_caseInsensitiveSearch_returnsMatchingTasks() {
        taskList.add(new Todo("Read Book"));
        taskList.add(new Todo("RETURN BOOK"));
        taskList.add(new Todo("book review"));
        
        List<Task> results = taskList.findTasks("BOOK");
        assertEquals(3, results.size());
        
        results = taskList.findTasks("book");
        assertEquals(3, results.size());
        
        results = taskList.findTasks("Book");
        assertEquals(3, results.size());
    }

    @Test
    void findTasks_noMatches_returnsEmptyList() {
        taskList.add(todoTask);
        taskList.add(deadlineTask);
        taskList.add(eventTask);
        
        List<Task> results = taskList.findTasks("xyz");
        assertTrue(results.isEmpty());
    }

    @Test
    void findTasks_emptyKeyword_returnsEmptyList() {
        taskList.add(todoTask);
        taskList.add(deadlineTask);
        
        List<Task> results = taskList.findTasks("");
        assertTrue(results.isEmpty());
        
        results = taskList.findTasks("   ");
        assertTrue(results.isEmpty());
    }

    @Test
    void findTasks_nullKeyword_returnsEmptyList() {
        taskList.add(todoTask);
        taskList.add(deadlineTask);
        
        List<Task> results = taskList.findTasks(null);
        assertTrue(results.isEmpty());
    }

    @Test
    void findTasks_emptyTaskList_returnsEmptyList() {
        List<Task> results = taskList.findTasks("book");
        assertTrue(results.isEmpty());
    }

    @Test
    void findTasks_withNullTasks_handlesGracefully() {
        taskList.add(todoTask);
        taskList.add(null);
        taskList.add(deadlineTask);
        
        List<Task> results = taskList.findTasks("book");
        assertEquals(1, results.size());
        assertTrue(results.contains(todoTask));
    }

    @Test
    void findTasks_multipleWordKeyword_returnsMatchingTasks() {
        taskList.add(new Todo("submit assignment today"));
        taskList.add(new Todo("assignment due tomorrow"));
        taskList.add(new Todo("read book assignment"));
        
        List<Task> results = taskList.findTasks("assignment");
        assertEquals(3, results.size());
    }

    @Test
    void findTasks_keywordWithSpaces_trimsAndMatches() {
        taskList.add(todoTask); // "read book"
        taskList.add(new Todo("book review"));
        
        List<Task> results = taskList.findTasks("  book  ");
        assertEquals(2, results.size());
    }
}