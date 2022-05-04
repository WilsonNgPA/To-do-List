package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDateTime;
import java.util.ArrayList;

// Represents a To-Do List having an arraylist of tasks
public class ToDoList implements Writable {
    ArrayList<Task> tasks;      // list of tasks
    String name;

    // EFFECTS: Initializes list of tasks to be an empty arraylist and initialize listName to specified parameter
    public ToDoList(String listName) {
        tasks = new ArrayList<>();
        this.name = listName;
    }

    // EFFECTS: Initializes list of tasks to be an empty arraylist and initialize listName to be empty string
    public ToDoList() {
        this("");
    }

    public String getName() {
        return this.name;
    }

    // REQUIRES: name != null or empty
    public void setName(String name) {
        this.name = name;
        EventLog.getInstance().logEvent(new Event("List name set to: " + name));
    }

    // EFFECT: return size of to-do list
    public int getSize() {
        return tasks.size();
    }

    // EFFECTS: returns true if to-do list is empty, false otherwise
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    // REQUIRES: task != null
    // EFFECTS: adds task to to-do list if there is no task with the same name in to-do list. returns true if
    //          successful, false otherwise
    public boolean addTask(Task task) {
        if (containsTask(task.getName())) {
            return false;
        }
        tasks.add(task);
        EventLog.getInstance().logEvent(new Event("Added task: " + task.getName()));
        return true;
    }

    // REQUIRES: task != null
    // EFFECTS: remove task specified by task name from to-do list if to-do list is not empty and a task with
    //          the same name exist inside the to-do list. returns ArrayList of integers
    //          representing the index of tasks removed, if no task removed, return empty ArrayList
    public ArrayList<Integer> removeTask(String taskName) {
        if (isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<Integer> indexArray = new ArrayList<>();
        ArrayList<Task> taskToRemove = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getName().equals(taskName)) {
                indexArray.add(tasks.indexOf(t));
                EventLog.getInstance().logEvent(new Event("Removed task: " + t.getName()));
                taskToRemove.add(t);
            }
        }
        tasks.removeAll(taskToRemove);
        return indexArray;
    }

    // EFFECTS: remove all completed tasks from to-do list if list is not empty, returns ArrayList of integers
    //          representing the index of tasks removed, if no task removed, return empty ArrayList
    public ArrayList<Integer> removeAllCompletedTasks() {
        if (isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<Integer> indexArray = new ArrayList<>();
        ArrayList<Task> taskToRemove = new ArrayList<>();
        for (Task t : tasks) {
            if (t.isCompleted()) {
                indexArray.add(tasks.indexOf(t));
                EventLog.getInstance().logEvent(new Event("Removed completed task: " + t.getName()));
                taskToRemove.add(t);
            }
        }
        tasks.removeAll(taskToRemove);
        return indexArray;
    }

    // EFFECTS: remove all overdue tasks from to-do list if list is not empty, returns ArrayList of integers
    //          representing the index of tasks removed, if no task removed, return empty ArrayList
    public ArrayList<Integer> removeAllOverdueTasks() {
        if (isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<Integer> indexArray = new ArrayList<>();
        ArrayList<Task> taskToRemove = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getDeadline().isBefore(LocalDateTime.now())) {
                indexArray.add(tasks.indexOf(t));
                EventLog.getInstance().logEvent(new Event("Removed overdue task: " + t.getName()));
                taskToRemove.add(t);
            }
        }
        tasks.removeAll(taskToRemove);
        return indexArray;
    }

    // EFFECTS: removes all tasks from the to-do list
    public void removeAllTasks() {
        tasks.clear();
        EventLog.getInstance().logEvent(new Event("Removed all tasks"));
    }

    // REQUIRES: taskName != null
    // EFFECTS: returns true if task specified by name exists inside the to-do list, false if list is empty or task
    //          does not exist inside to-do list
    public boolean containsTask(String taskName) {
        if (isEmpty()) {
            return false;
        }
        for (Task t : tasks) {
            if (t.getName().equals(taskName)) {
                return true;
            }
        }
        return false;
    }

    // REQUIRES: taskName != null
    // EFFECTS: return task specified by task name from to-do list if list is not empty and a task with the same name
    //          exist inside the to-do list
    public Task getTask(String taskName) {
        if (isEmpty()) {
            return null;
        }
        for (Task t : tasks) {
            if (t.getName().equals(taskName)) {
                return t;
            }
        }
        return null;
    }

    // REQUIRES: index != null
    // EFFECTS: return task specified by index from to-do list if list is not empty and a task exist at the specified
    //          index in the to-do list
    public Task getTask(int index) {
        if (isEmpty()) {
            return null;
        }
        if (index >= getSize()) {
            return null;
        }
        return tasks.get(index);
    }

    // REQUIRES: taskName != null
    // EFFECTS: return index of task specified by task name from to do list, -1 if list is empty or not found
    public int getTaskIndex(String taskName) {
        if (isEmpty()) {
            return -1;
        }
        for (Task t : tasks) {
            if (t.getName().equals(taskName)) {
                return tasks.indexOf(t);
            }
        }
        return -1;
    }

    // EFFECTS: sorts tasks in to-do list by their priority, descending order
    public void sortByPriority() {
        tasks.sort(new PriorityComparator());
        EventLog.getInstance().logEvent(new Event("Sorted tasks by priority"));
    }

    // EFFECTS: sorts task in to-do list by their deadline, ascending order
    public void sortByDeadline() {
        tasks.sort(new DeadlineComparator());
        EventLog.getInstance().logEvent(new Event("Sorted tasks by deadline"));
    }

    // EFFECTS: sorts task in to-do list by their name, ascending order
    public void sortByName() {
        tasks.sort(new NameComparator());
        EventLog.getInstance().logEvent(new Event("Sorted tasks by name"));
    }

    // EFFECTS: sorts task in to-do list first by their completion status, and then by deadline in ascending order
    public void sortByIsCompleted() {
        tasks.sort(new IsCompleteComparator());
        EventLog.getInstance().logEvent(new Event("Sorted tasks by completed status"));
    }

    // EFFECTS: returns a string representation of the to-do list
    public String toString() {
        if (tasks.isEmpty()) {
            return "To-Do List is empty";
        } else {
            StringBuilder toStringBuilder = new StringBuilder();
            for (Task t : tasks) {
                toStringBuilder.append(t.toString());
                toStringBuilder.append("\n");
            }
            return toStringBuilder.toString();
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("listName", getName());
        json.put("tasks", tasksToJson());
        EventLog.getInstance().logEvent(new Event("To-do list saved"));
        return json;
    }

    // EFFECTS: returns tasks in this to-do list as a JSON array
    private JSONArray tasksToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Task t : tasks) {
            jsonArray.put(t.toJson());
        }

        return jsonArray;
    }
}
