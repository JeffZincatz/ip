package duke.task;

/**
 * A task representation for Duke
 */
public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Get status icon of this task.
     *
     * @return "X" if this task is done, otherwise " ".
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Mark this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    @Override
    public String toString() {
        return "["+ this.getStatusIcon() +"]" + " " + this.description;
    }

    /**
     * Get the done status of this task
     *
     * @return true if this task is done, false otherwise
     */
    public boolean getDoneStatus() {
        return this.isDone;
    }
}