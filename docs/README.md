# Lebron User Guide

![Lebron Task Manager](https://img.shields.io/badge/Lebron-Task%20Manager-orange?style=for-the-badge&logo=java)

Lebron is your personal task management assistant that helps you stay organized and productive. Just like the GOAT himself, it's reliable, efficient, and always delivers when you need it most! 🏀

Whether you're managing daily todos, tracking important deadlines, or scheduling events, Lebron has got your back with a clean interface and smart features.

## Quick Start

1. **Download and run** the application using `./gradlew run`
2. **Start adding tasks** using simple commands
3. **Stay organized** with automatic saving and smart search

## Adding Todos

Create simple tasks that you need to complete without any specific timing.

**Command:** `todo <description>`

**Example:** `todo read CS2103T textbook`

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read CS2103T textbook
 Now you have 1 tasks in the list.
__./gradlew clean shadowJar__________________________________________________________
```

## Adding Deadlines

Create tasks with specific due dates to keep track of important deadlines.

**Command:** `deadline <description> /by <date>`

**Supported date formats:**
- `yyyy-mm-dd` (e.g., 2024-12-25)
- `yyyy-mm-dd HHmm` (e.g., 2024-12-25 1400)
- `d/m/yyyy` (e.g., 25/12/2024)
- `d/m/yyyy HHmm` (e.g., 25/12/2024 1400)

**Examples:**
```
deadline submit project report /by 2024-12-25
deadline attend lecture /by 25/12/2024 1400
```

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [D][ ] submit project report (by: Dec 25 2024 00:00)
 Now you have 2 tasks in the list.
____________________________________________________________
```

## Adding Events

Schedule events that happen during specific time periods.

**Command:** `event <description> /from <start_date> /to <end_date>`

**Example:** `event team meeting /from 2024-12-25 1400 /to 2024-12-25 1600`

**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [E][ ] team meeting (from: Dec 25 2024 14:00 to: Dec 25 2024 16:00)
 Now you have 3 tasks in the list.
____________________________________________________________
```

## Viewing Your Tasks

Display all your tasks in a neat, organized list.

**Command:** `list`

**Expected output:**
```
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read CS2103T textbook
 2.[D][ ] submit project report (by: Dec 25 2024 00:00)
 3.[E][ ] team meeting (from: Dec 25 2024 14:00 to: Dec 25 2024 16:00)
____________________________________________________________
```

## Marking Tasks as Done

Mark completed tasks to track your progress.

**Command:** `mark <task_number>`

**Example:** `mark 1`

**Expected output:**
```
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read CS2103T textbook
____________________________________________________________
```

## Unmarking Tasks

Change your mind? Unmark tasks that you marked as done by mistake.

**Command:** `unmark <task_number>`

**Example:** `unmark 1`

**Expected output:**
```
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] read CS2103T textbook
____________________________________________________________
```

## Deleting Tasks

Remove tasks that are no longer needed.

**Command:** `delete <task_number>`

**Example:** `delete 2`

**Expected output:**
```
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] submit project report (by: Dec 25 2024 00:00)
 Now you have 2 tasks in the list.
____________________________________________________________
```

## Finding Tasks

Search for tasks using keywords. The search is case-insensitive and matches partial words.

**Command:** `find <keyword>`

**Example:** `find book`

**Expected output:**
```
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] read CS2103T textbook
____________________________________________________________
```

## Finding Free Time

Discover available time slots in your schedule for planning new activities.

**Command:** `freetime <hours_needed>`

**Example:** `freetime 3`

**Expected output:**
```
____________________________________________________________
 Found a free time slot:
   Dec 26 2024 09:00 to Dec 26 2024 18:00 (9 hours)
____________________________________________________________
```

If no suitable time slot is found:
```
____________________________________________________________
 Sorry, I couldn't find any free time slots of 3 hours.
____________________________________________________________
```

## Exiting the Application

Save your work and close the application gracefully.

**Command:** `bye`

**Expected output:**
```
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## Tips and Tricks

### 💡 **Smart Date Parsing**
- Use any of the supported date formats for maximum flexibility
- Add times in 24-hour format (1400 = 2:00 PM)
- Dates without times default to midnight (00:00)

### 🔍 **Efficient Searching**
- Search works with partial matches: "book" finds "textbook"
- Case doesn't matter: "BOOK" and "book" work the same
- Search across all task types and their descriptions

### 📅 **Task Management**
- Task numbers can change after deletions - always check with `list` first
- Completed tasks stay in your list until manually deleted
- All changes are automatically saved

### ⚡ **Keyboard Shortcuts**
- Commands are case-insensitive: `TODO` and `todo` work the same
- Extra spaces are handled gracefully: `  todo   read book  ` works fine

## Error Messages

Lebron provides helpful error messages when things go wrong:

**Invalid command:**
```
____________________________________________________________
 OOPS!!! I'm sorry, but I don't know what that means :-(
 Try 'list', 'todo <description>', 'deadline <desc> /by <date>',
 'event <desc> /from <start> /to <end>', 'mark <number>',
 'unmark <number>', 'delete <number>', 'find <keyword>',
 'freetime <hours>', or 'bye'.
____________________________________________________________
```

**Invalid date format:**
```
____________________________________________________________
 OOPS!!! Invalid date format: tomorrow. Please use formats like: 
 yyyy-mm-dd, yyyy-mm-dd HHmm, d/m/yyyy, or d/m/yyyy HHmm
____________________________________________________________
```

**Invalid task number:**
```
____________________________________________________________
 OOPS!!! I don't have a task with that number.
 Use 'list' to see your tasks first.
____________________________________________________________
```

## Frequently Asked Questions

**Q: Where are my tasks saved?**
A: Tasks are automatically saved to `./data/duke.txt` and loaded when you restart the application.

**Q: Can I edit existing tasks?**
A: Currently, you can mark/unmark and delete tasks. To modify a task description, delete the old one and create a new one.

**Q: What happens if I enter an invalid date?**
A: Lebron will show you a helpful error message with examples of valid date formats.

**Q: Can I use different date formats in the same session?**
A: Yes! Mix and match any of the supported date formats as needed.

**Q: How does the free time feature work?**
A: It analyzes your scheduled events and finds gaps in your calendar that match your requested duration.

---

**Need help?** All commands are designed to be intuitive and self-explanatory. When in doubt, try the `list` command to see your current tasks, or `bye` to exit safely.

Happy task managing! 🎯