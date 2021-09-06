package duke;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.Todo;



/**
 * A parser to parse Duke commands.
 */
public class Parser {
    /**
     * Get the action specified by the command.
     * <p>
     * A valid action can be list, done, todo, event, deadline, delete.
     *
     * @param command The command to be parsed.
     * @return The action of the command.
     */
    public String getCommandAction(String command) {
        return command.split(" ", 2)[0];
    }

    /**
     * Get the task index of done and delete action.
     *
     * @param command The command to be parsed.
     * @return The index of the task of the command, -1 if is invalid action.
     */
    public int getCommandActionIndex(String command) throws DukeException {
        switch (getCommandAction(command)) {
        case "done":
            return Integer.parseInt(command.substring(5));
        case "delete":
            return Integer.parseInt(command.substring(7));
        default:
            throw new DukeException("Invalid action. Index cannot be parsed from the command.");
        }
    }

    /**
     * Get the relevant task information from the command.
     *
     * @param command The command to be parsed.
     * @return A map of task information from the command.
     */
    public Task commandToTask(String command) throws DukeException {
        switch (getCommandAction(command)) {
        case "todo":
            String[] todoDetails = command.split(" ", 2);
            assert todoDetails.length == 2 : "Invalid todo format";
            String todoDescription;
            try {
                todoDescription = todoDetails[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                // no description
                String message = "☹ OOPS!!! The description of a todo cannot be empty.";
                throw new DukeException(message);
            }
            return new Todo(todoDescription);
        case "event":
            String[] eventDetails = command.split(" /at ");
            assert eventDetails.length == 2 : "Invalid event format";
            LocalDate at;
            try {
                at = this.stringToLocalDate(eventDetails[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                // no /at found in command
                String message = "☹ OOPS!!! The time of an event cannot be empty.";
                throw new DukeException(message);
            }
            String eventDescription;
            try {
                eventDescription = eventDetails[0].split("event ")[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                // no event description
                String message = "☹ OOPS!!! The description of an event cannot be empty.";
                throw new DukeException(message);
            }
            return new Event(eventDescription, at);
        case "deadline":
            String[] deadlineDetails = command.split(" /by ");
            assert deadlineDetails.length == 2 : "Invalid deadline format";
            LocalDate by;
            try {
                by = this.stringToLocalDate(deadlineDetails[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                // no /by found in command
                String message = "☹ OOPS!!! The time of a deadline cannot be empty.";
                throw new DukeException(message);
            }
            String deadlineDescription;
            try {
                deadlineDescription = deadlineDetails[0].split("deadline ")[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                // no deadline description
                String message = "☹ OOPS!!! The description of a deadline cannot be empty.";
                throw new DukeException(message);
            }
            return new Deadline(deadlineDescription, by);
        default:
            throw new DukeException("Invalid action. Task cannot be parsed from the command.");
        }
    }

    /**
     * Parse a string of time into LocalDate.
     *
     * @param str The time string.
     * @return The LocalDate corresponding to the string.
     * @throws DukeException The exception that contains the message to be printed.
     */
    public LocalDate stringToLocalDate(String str) throws DukeException {
        try {
            return LocalDate.parse(str);
        } catch (DateTimeParseException e) {
            String message = "The time format is invalid. Please use the format YYYY-MM-DD";
            throw new DukeException(message);
        }
    }

    /**
     * Format LocalDate into MMMM d yyyy format.
     *
     * @param localDate The LocalDate to be formatted.
     * @return The formatted LocalDate string.
     */
    public String formatLocalDate(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
    }
}
