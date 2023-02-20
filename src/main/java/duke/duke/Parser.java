package duke.duke;

import java.util.ArrayList;

import duke.exceptions.DukeExceptions;
import duke.tasks.Deadline;
import duke.tasks.Event;
import duke.tasks.Task;
import duke.tasks.ToDo;



/**
 * Deals with detecting commands in user inputs and then executing them.
 */
public class Parser {
    private TaskList data;

    public Parser(TaskList data) {
        this.data = data;
    }

    public TaskList getTaskList() {
        return this.data;
    }

    /**
     * Parses a user input to detect commands, and then executing them,
     * and finally returns an appropriate response message.
     * @param input User input.
     * @return A response message.
     */
    public String parse(String input) {
        if (input.equals("bye")) {
            Storage.writeFile(data);
            return "Bye!";
        }
        if (input.equals("list")) {
            return data.toString();
        }

        if (input.contains("unmark")) {
            char query = input.charAt(input.length() - 1);
            int pos = Character.getNumericValue(query);
            //error check for pos exceeding size
            int maxSize = data.getSize();
            assert pos <= maxSize : "Exceeded";
            data.unmarkDone(pos - 1);
            String msg = "Unmarked:" + "\n" + data.getEntry(pos - 1).toString();
            return msg;
        }

        if (input.contains("mark")) {
            char query = input.charAt(input.length() - 1);
            int pos = Character.getNumericValue(query);
            //error check for pos exceeding size
            int maxSize = data.getSize();
            assert pos <= maxSize : "Exceeded";
            data.markDone(pos - 1);
            String msg = "Marked:" + "\n" + data.getEntry(pos - 1).toString();
            return msg;
        }

        if (input.contains("delete")) {
            char query = input.charAt(input.length() - 1);
            int pos = Character.getNumericValue(query);
            //error check for pos exceeding size
            int maxSize = data.getSize();
            assert pos <= maxSize : "Exceeded";
            Task del = data.getEntry(pos - 1);
            data.removeEntry(pos - 1);
            String msg = "Deleted:" + "\n" + del.toString();
            return msg;
        }

        if (input.contains("todo ")) {
            Task todo = new ToDo();
            String description = input.replace("todo ", "");
            try {
                todo.formatDescription(description);
            } catch (DukeExceptions e) {
                return e.getMessage();
            }
            data.addEntry(todo);
            return String.format("Now you have %d tasks in the list", data.getSize());
        }

        if (input.contains("event ")) {
            Task event = new Event();
            String description = input.replace("event ", "");
            try {
                event.formatDescription(description);
            } catch (DukeExceptions e) {
                return e.getMessage();
            }
            data.addEntry(event);
            return String.format("Now you have %d tasks in the list", data.getSize());
        }

        if (input.contains("deadline ")) {
            Task deadline = new Deadline();
            String description = input.replace("deadline ", "");
            try {
                deadline.formatDescription(description);
            } catch (DukeExceptions e) {
                return e.getMessage();
            }

            data.addEntry(deadline);
            return String.format("Now you have %d tasks in the list", data.getSize());
        }

        if (input.contains("find ")) {
            String key = input.replace("find ", "");
            ArrayList<Task> matches = this.data.findEntry(key);
            String matched = "";

            for (int i = 0; i < matches.size(); i++) {
                String msg = String.format("%d. %s\n", i + 1, matches.get(i).toString());
                matched += msg;
            }
            return matched;
        }
        return "I do not understand your instructions...";
    }
}
