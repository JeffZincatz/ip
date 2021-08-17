import java.util.ArrayList;
import java.util.Scanner;

public class Duke {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        greet();
        add();

        // close scanner
        scanner.close();
    }

    /**
     * Print a message that is enclosed by 2 horizontal lines.
     *
     * @param message The message to be printed between 2 horizontal lines.
     */
    private static void printMessage(String message) {
        String horizontalLine = "____________________________________________________________";
        System.out.println(horizontalLine + "\n" + message + "\n" + horizontalLine);
    }

    /**
     * Greeting message of Duke.
     */
    static void greet() {
        String logo = " ____        _\n"
                + "|  _ \\ _   _| | _____\n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        printMessage(logo + "\nWelcome! I'm Duke\nWhat can I do for you?");
    }

    /**
     * Echos commands entered by the user, and exits when the user types bye.
     */
    static void echo() {
        String command = scanner.nextLine();
        while (!command.equals("bye")) {
            printMessage(command);
            command = scanner.nextLine();
        }
        printMessage("Bye. See you next time!");
    }

    /**
     * Add and store tasks entered and display them back with complete status when requested.
     */
    static void add() {
        String command;
        ArrayList<Task> list = new ArrayList<>();
        while (true) {
            command = scanner.nextLine();
            if (command.equals("bye")) {
                // end bot
                break;
            } else {
                // split command to check for actions
                String[] actions = command.split(" ", 2);
                switch (actions[0]) {
                    case "list":
                        // view list
                        printMessage(listToString(list));
                        break;
                    case "done":
                        done(command, list);
                        break;
                    case "todo":
                        todo(command, list);
                        break;
                    case "event":
                        event(command, list);
                        break;
                    case "deadline":
                        deadline(command, list);
                        break;
                    case "delete":
                        delete(command, list);
                        break;
                    default:
                        // Message for unrecognised task type
                        printMessage("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
                        break;
                }
            }
        }
        printMessage("Bye. See you next time!");
    }

    /**
     * Convert a list of tasks to a message string.
     *
     * @param list The list of tasks stored.
     * @return The string of messages.
     */
    private static String listToString(ArrayList<Task> list) {
        StringBuilder result = new StringBuilder();
        if (!list.isEmpty()) {
            result.append("Here are the tasks in your list:\n");
            result.append(1).append(". ").append(list.get(0));
            for (int i = 1; i < list.size(); i++) {
                result.append("\n").append(i + 1).append(". ").append(list.get(i));
            }
        } else {
            result.append("There is not task yet. Try to add a task first.");
        }
        return result.toString();
    }

    /**
     * Duke response message for done command
     * @param command command entered to console
     * @param list current list of tasks
     */
    private static void done(String command, ArrayList<Task> list) {
        try {
            int doneIndex = Integer.parseInt(command.substring(5));
            try {
                list.get(doneIndex - 1).markAsDone();
                printMessage("Nice! I've marked this task as done:\n\t" + list.get(doneIndex - 1));
            } catch (IndexOutOfBoundsException e) {
                // Task at doneIndex does not exist
                printMessage("Task " + doneIndex + " does not exist. Please check your task list!");

            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            // command done is not followed by a number
            printMessage("☹ OOPS!!! The index of a task done must be an integer.");
        }
    }

    /**
     * Duke response message for todo command
     * @param command command entered to console
     * @param list current list of tasks
     */
    private static void todo(String command, ArrayList<Task> list) {
        String[] todoSplit = command.split(" ", 2);
        String todoDescription;
        try {
            todoDescription = todoSplit[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            // no description
            printMessage("☹ OOPS!!! The description of a todo cannot be empty.");
            return;
        }

        Todo todo = new Todo(todoDescription);
        list.add(todo);
        printMessage("Got it. I've added this task:\n\t" + todo + "\nNow you have " + list.size() + " tasks in the list.");
    }

    /**
     * Duke response message for event command
     * @param command command entered to console
     * @param list current list of tasks
     */
    private static void event(String command, ArrayList<Task> list) {
        String[] eventSplit = command.split(" /at ");
        String at;
        try {
            at = eventSplit[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            // no /at found in command
            printMessage("☹ OOPS!!! The time of an event cannot be empty.");
            return;
        }
        String eventDescription;
        try {
            eventDescription = eventSplit[0].split("event ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            // no event description
            printMessage("☹ OOPS!!! The description of an event cannot be empty.");
            return;
        }
        Event event = new Event(eventDescription, at);
        list.add(event);
        printMessage("Got it. I've added this task:\n\t" + event + "\nNow you have " + list.size() + " tasks in the list.");
    }

    /**
     * Duke response message for deadline command
     * @param command command entered to console
     * @param list current list of tasks
     */
    private static void deadline(String command, ArrayList<Task> list) {
        String[] ddlSplit = command.split(" /by ");
        String by;
        try {
            by = ddlSplit[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            // no /by found in command
            printMessage("☹ OOPS!!! The time of a deadline cannot be empty.");
            return;
        }
        String ddlDescription;
        try {
            ddlDescription = ddlSplit[0].split("deadline ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            // no deadline description
            printMessage("☹ OOPS!!! The description of a deadline cannot be empty.");
            return;
        }
        Deadline deadline = new Deadline(ddlDescription, by);
        list.add(deadline);
        printMessage("Got it. I've added this task:\n\t" + deadline + "\nNow you have " + list.size() + " tasks in the list.");
    }

    /**
     * Duke response message for delete command
     * @param command command entered to console
     * @param list current list of tasks
     */
    private static void delete(String command, ArrayList<Task> list) {
        try {
            int deleteIndex = Integer.parseInt(command.substring(7));
            try {
                Task removed = list.remove(deleteIndex - 1);
                printMessage("Noted. I've removed this task:\n\t" + removed);
            } catch (IndexOutOfBoundsException e) {
                // Task at deleteIndex does not exist
                printMessage("Task " + deleteIndex + " does not exist. Please check your task list!");
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            // command delete is not followed by a number
            printMessage("☹ OOPS!!! The index of a task to be deleted must be an integer.");
        }
    }
}
