package model;

import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Represents a task which is identified by its name. Each task has a description, deadline (if any), priority, and
// a boolean which represents whether the task is completed or not
public class Task implements Writable {
    private String name;
    private String description;
    private LocalDateTime deadline;
    private int priority;
    private boolean completed;

    // REQUIRES: name != null, description != null, deadline != null, 0 <= priority <= 5, completed != null
    // EFFECTS: name of task, description, deadline, priority, and complete set to respective specified parameters.
    //          Priority is a positive integer between 0 and 5 inclusive. If specified priority is not within range,
    //          set priority to 0.
    public Task(String name, String description, LocalDateTime deadline, int priority, boolean completed) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        if (priority >= 0 && priority <= 5) {
            this.priority = priority;
        } else {
            this.priority = 0;
        }
        this.completed = completed;
    }

    // REQUIRES: name != null, description != null, deadline != null, 0 <= priority <= 5
    // EFFECTS: name of task, description, deadline, and priority set to respective specified parameters. Priority is
    //          a positive integer between 0 and 5 inclusive. If specified priority is not within range, set priority
    //          to 0. Initialize completed to be false.
    public Task(String name, String description, LocalDateTime deadline, int priority) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        if (priority >= 0 && priority <= 5) {
            this.priority = priority;
        } else {
            this.priority = 0;
        }
        this.completed = false;
    }

    // REQUIRES: name != null, description != null, 0 <= priority <= 5
    // EFFECTS: since no deadline entered, set deadline to be LocalDateTime.MAX which is the
    //          largest possible future date.
    public Task(String name, String description, int priority) {
        this(name, description, LocalDateTime.MAX, priority);
    }

    // REQUIRES: name != null, description != null
    // EFFECTS: since no deadline entered, set deadline to be LocalDateTime.MAX which is the
    //          largest possible future date. since no priority is entered, set priority to be 0
    public Task(String name, String description) {
        this(name, description, LocalDateTime.MAX, 0);
    }

    public String getName() {
        return name;
    }

    // REQUIRES: name != null
    // EFFECTS: changes name of task to be the new name specified in parameters
    public void setName(String name) {
        EventLog.getInstance().logEvent(new Event("Task name changed: " + getName() + " -> " + name));
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    // REQUIRES: description != null
    // EFFECTS: changes description of task to be the new description specified in parameters
    public void setDescription(String description) {
        EventLog.getInstance().logEvent(new Event("Task \"" + getName() + "\" description changed: "
                + getDescription() + " -> " + description));
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    // REQUIRES: deadline != null
    // EFFECTS: changes deadline of task to be the new deadline specified in parameters
    public void setDeadline(LocalDateTime deadline) {
        EventLog.getInstance().logEvent(new Event("Task \"" + getName() + "\" deadline changed: "
                + getDeadline() + " -> " + deadline));
        this.deadline = deadline;
    }

    public int getPriority() {
        return priority;
    }

    // REQUIRES: 0 <= priority <= 5
    // EFFECTS: if priority is between 0 and 5 inclusive, change priority to be the new deadline specified in
    //          parameter. otherwise, do nothing.
    public void setPriority(int priority) {
        if (priority >= 0 && priority <= 5) {
            EventLog.getInstance().logEvent(new Event("Task \"" + getName() + "\" priority changed: "
                    + getPriority() + " -> " + priority));
            this.priority = priority;
        }
    }

    public boolean isCompleted() {
        return completed;
    }

    // REQUIRES: completed != null;
    // EFFECTS: changes completed of task to be true or false as specified in parameters
    public void setCompleted(boolean completed) {
        EventLog.getInstance().logEvent(new Event("Task \"" + getName() + "\" completed status changed: "
                + isCompleted() + " -> " + completed));
        this.completed = completed;
    }

    // EFFECTS: returns a string representation of task
    public String toString() {
        String deadlineInString;
        if (getDeadline().equals(LocalDateTime.MAX)) {
            deadlineInString = "None";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            deadlineInString = getDeadline().format(formatter);
        }
        String toString = "Task Name: " + getName() + "\n";
        toString += "\tDescription: " + getDescription() + "\n";
        toString += "\tDeadline: " + deadlineInString + "\n";
        toString += "\tPriority: " + getPriority() + "\n";
        toString += "\tCompleted: " + isCompleted() + "\n";
        return toString;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("taskName", getName());
        json.put("taskDescription", getDescription());
        String deadlineInString;
        if (getDeadline().equals(LocalDateTime.MAX)) {
            deadlineInString = "None";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            deadlineInString = getDeadline().format(formatter);
        }
        json.put("deadline", deadlineInString);
        json.put("priority", getPriority());
        json.put("completed", isCompleted());
        return json;
    }
}
