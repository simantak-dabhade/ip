# Lebron Task Manager

Lebron is a personal task management application that helps you organize your todos, deadlines, and events. Just like the GOAT himself, it's reliable and gets the job done! 🏀

## Features

- **Multiple Task Types**: Create todos, deadlines with due dates, and events with time periods
- **Smart Date Parsing**: Supports various date formats (yyyy-mm-dd, d/m/yyyy, with optional times)
- **Task Management**: Mark tasks as done/undone, delete tasks, and view your task list
- **Search Functionality**: Find tasks by keywords with case-insensitive partial matching
- **Free Time Finder**: Find available time slots in your schedule
- **Persistent Storage**: Your tasks are automatically saved and loaded between sessions
- **Dual Interface**: Both GUI (JavaFX) and console interfaces available

## Quick Start

### Running the Application

**GUI Version (Recommended):**
```bash
./gradlew run
```

**Console Version:**
```bash
java -cp build/classes/java/main lebron.Lebron
```

**JAR File:**
```bash
./gradlew shadowJar
java -jar build/libs/duke.jar
```

### Basic Commands

1. **Add a todo**: `todo <description>`
   ```
   todo read book
   ```

2. **Add a deadline**: `deadline <description> /by <date>`
   ```
   deadline submit report /by 2024-12-25
   deadline homework /by 25/12/2024 1400
   ```

3. **Add an event**: `event <description> /from <start> /to <end>`
   ```
   event team meeting /from 2024-12-25 1400 /to 2024-12-25 1600
   ```

4. **List all tasks**: `list`

5. **Mark task as done**: `mark <task_number>`

6. **Mark task as undone**: `unmark <task_number>`

7. **Delete a task**: `delete <task_number>`

8. **Find tasks**: `find <keyword>`

9. **Find free time**: `freetime <hours_needed>`

10. **Exit**: `bye`

## Code Architecture

The application follows a clean, object-oriented architecture with clear separation of concerns:

### Core Packages

```
src/main/java/lebron/
├── task/               # Task models and types
│   ├── Task.java          # Abstract base class for all tasks
│   ├── Todo.java          # Simple todo tasks
│   ├── Deadline.java      # Tasks with deadlines
│   └── Event.java         # Tasks with time periods
├── data/               # Data management
│   ├── TaskList.java      # Task collection management
│   └── FreeTimeSlot.java  # Free time representation
├── parser/             # Command parsing
│   └── Parser.java        # Command line parser
├── storage/            # Data persistence
│   └── Storage.java       # File I/O operations
├── ui/                 # User interfaces
│   ├── Ui.java           # Console interface
│   └── GuiUi.java        # GUI interface adapter
├── gui/                # JavaFX GUI components
│   ├── MainWindow.java    # Main GUI window
│   └── GuiLebron.java     # GUI application controller
├── Lebron.java         # Console application entry point
└── Launcher.java       # GUI application entry point
```

### Design Patterns Used

1. **Command Pattern**: Parser converts user input into Command objects
2. **Template Method**: Task class defines common behavior, subclasses implement specifics
3. **Factory Pattern**: Parser creates appropriate Command instances
4. **Observer Pattern**: UI components respond to task changes
5. **Strategy Pattern**: Different UI implementations (console vs GUI)

### Key Classes

**Task Hierarchy:**
- `Task` (abstract): Base class with common task functionality
- `Todo`: Simple tasks without timing constraints
- `Deadline`: Tasks with due dates
- `Event`: Tasks with start and end times

**Core Logic:**
- `TaskList`: Manages collection of tasks, handles operations like add/delete/find
- `Parser`: Parses user commands into structured Command objects
- `Storage`: Handles saving/loading tasks to/from file system

**User Interface:**
- `Ui`: Console-based interface with Scanner input
- `GuiUi`: JavaFX adapter that outputs to TextArea
- `MainWindow`: JavaFX main window with text input/output

## Date Format Support

The application supports multiple date formats for user convenience:

- `yyyy-mm-dd` (e.g., 2024-12-25)
- `yyyy-mm-dd HHmm` (e.g., 2024-12-25 1400)
- `d/m/yyyy` (e.g., 25/12/2024)
- `d/m/yyyy HHmm` (e.g., 25/12/2024 1400)

Times are in 24-hour format. If no time is specified, defaults to 00:00.

## Data Storage

Tasks are automatically saved to `./data/duke.txt` in a pipe-separated format:
```
T | 0 | read book
D | 1 | submit report | 2024-12-25T14:00
E | 0 | team meeting | 2024-12-25T14:00 | 2024-12-25T16:00
```

## Error Handling

The application includes comprehensive error handling:
- Invalid date formats are caught and user-friendly messages are shown
- Out-of-bounds task indices are handled gracefully
- File I/O errors are managed without crashing the application
- Malformed input is parsed and appropriate error messages are displayed

## Testing

The project includes comprehensive test coverage:

**Run all tests:**
```bash
./gradlew test
```

**Test categories:**
- Unit tests for individual classes (ParserTest, TaskListTest)
- Integration tests for end-to-end functionality (CommandIntegrationTest)
- Edge case testing for error scenarios

## Development Setup

### Prerequisites
- JDK 17 or higher
- Gradle (included via wrapper)

### Building
```bash
./gradlew build
```

### Running Tests
```bash
./gradlew test
```

### Code Quality Checks
```bash
./gradlew check
```

## Architecture Decisions

1. **Separation of Concerns**: Clear separation between UI, business logic, and data persistence
2. **Command Pattern**: Makes adding new commands easy and keeps parsing logic clean
3. **Inheritance Hierarchy**: Task subclasses share common behavior while implementing specific features
4. **Exception Handling**: Custom exceptions for user-friendly error messages
5. **Flexible Date Parsing**: Multiple format support for better user experience
6. **Persistent Storage**: Automatic save/load ensures no data loss

## Future Enhancements

- Task priorities and categories
- Recurring tasks
- Task dependencies
- Calendar integration
- Mobile application
- Multi-user support
- Cloud synchronization

---

**Author**: Lebron Task Manager Team  
**Version**: 1.0  
**Java Version**: 17+  
**License**: MIT