package ui;

import model.Task;
import model.ToDoList;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Referenced from https://stackoverflow.com/questions/7378013/connect-a-list-of-objects-to-a-jtable
 * Represents the JTable model used when creating a JTable to store a ToDoList
 */
public class ToDoListTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Task", "Description", "Deadline", "Priority", "Completed?"};
    private ToDoList list;

    public ToDoListTableModel(ToDoList list) {
        this.list = list;
    }

    // MODIFIES: this, list
    // EFFECTS: adds a task to to-do list and updates table to reflect change in to-do list.
    public void addTask(Task task) {
        list.addTask(task);
        fireTableRowsInserted(list.getSize() - 1, list.getSize() - 1);
    }

    // MODIFIES: this, list
    // EFFECTS: removes a task from to-do list and updates table to reflect change in to-do list.
    public void removeTask(String taskName) {
        ArrayList<Integer> removedIndex = list.removeTask(taskName);
        Collections.sort(removedIndex);
        for (int i = removedIndex.size() - 1; i >= 0; i--) {
            fireTableRowsDeleted(removedIndex.get(i), removedIndex.get(i));
        }
    }

    // MODIFIES: this, list
    // EFFECTS: removes all completed tasks from to-do list and updates table to reflect change in to-do list.
    public void removeAllCompletedTasks() {
        ArrayList<Integer> removedIndex = list.removeAllCompletedTasks();
        Collections.sort(removedIndex);
        for (int i = removedIndex.size() - 1; i >= 0; i--) {
            fireTableRowsDeleted(removedIndex.get(i), removedIndex.get(i));
        }
    }

    // MODIFIES: this, list
    // EFFECTS: removes all overdue tasks from to-do list and updates table to reflect change in to-do list.
    public void removeAllOverdueTasks() {
        ArrayList<Integer> removedIndex = list.removeAllOverdueTasks();
        Collections.sort(removedIndex);
        for (int i = removedIndex.size() - 1; i >= 0; i--) {
            fireTableRowsDeleted(removedIndex.get(i), removedIndex.get(i));
        }
    }

    // MODIFIES: this, list
    // EFFECTS: removes all tasks from to-do list and updates table to reflect change in to-do list.
    public void removeAllTasks() {
        fireTableRowsDeleted(0, list.getSize() - 1);
        list.removeAllTasks();
    }

    // MODIFIES: this, list, list.getTask(taskName)
    // EFFECTS: modifies the task specified by taskName in to-do list by changing its name to newName and updates
    //          table to reflect change in to-do list.
    public void modifyTaskName(String taskName, String newName) {
        int index = list.getTaskIndex(taskName);
        list.getTask(taskName).setName(newName);
        fireTableRowsUpdated(index, index);
    }

    // MODIFIES: this, list, list.getTask(taskName)
    // EFFECTS: modifies the task specified by taskName in to-do list by changing its description to newDescription
    //          and updates table to reflect change in to-do list.
    public void modifyTaskDescription(String taskName, String newDescription) {
        int index = list.getTaskIndex(taskName);
        list.getTask(taskName).setDescription(newDescription);
        fireTableRowsUpdated(index, index);
    }

    // MODIFIES: this, list, list.getTask(taskName)
    // EFFECTS: modifies the task specified by taskName in to-do list by changing its deadline to newDeadline
    //          and updates table to reflect change in to-do list.
    public void modifyTaskDeadline(String taskName, LocalDateTime newDeadline) {
        int index = list.getTaskIndex(taskName);
        list.getTask(taskName).setDeadline(newDeadline);
        fireTableRowsUpdated(index, index);
    }

    // MODIFIES: this, list, list.getTask(taskName)
    // EFFECTS: modifies the task specified by taskName in to-do list by changing its priority to newPriority
    //          and updates table to reflect change in to-do list.
    public void modifyTaskPriority(String taskName, int newPriority) {
        int index = list.getTaskIndex(taskName);
        list.getTask(taskName).setPriority(newPriority);
        fireTableRowsUpdated(index, index);
    }

    // MODIFIES: this, list, list.getTask(taskName)
    // EFFECTS: modifies the task specified by taskName in to-do list by changing its isCompleted to newIsCompleted
    //          and updates table to reflect change in to-do list.
    public void modifyTaskIsCompleted(String taskName, boolean newIsCompleted) {
        int index = list.getTaskIndex(taskName);
        list.getTask(taskName).setCompleted(newIsCompleted);
        fireTableRowsUpdated(index, index);
    }

    // MODIFIES: this, list
    // EFFECTS: sorts the tasks in to-do list by name and updates table to reflect change in to-do list
    public void sortTasksByName() {
        list.sortByName();
        fireTableRowsUpdated(0, list.getSize() - 1);
    }

    // MODIFIES: this, list
    // EFFECTS: sorts the tasks in to-do list by deadline and updates table to reflect change in to-do list
    public void sortTasksByDeadline() {
        list.sortByDeadline();
        fireTableRowsUpdated(0, list.getSize() - 1);
    }

    // MODIFIES: this, list
    // EFFECTS: sorts the tasks in to-do list by priority and updates table to reflect change in to-do list
    public void sortTasksByPriority() {
        list.sortByPriority();
        fireTableRowsUpdated(0, list.getSize() - 1);
    }

    // MODIFIES: this, list
    // EFFECTS: sorts the tasks in to-do list by completed status and updates table to reflect change in to-do list
    public void sortTasksByCompleted() {
        list.sortByIsCompleted();
        fireTableRowsUpdated(0, list.getSize() - 1);
    }

    // MODIFIES: this
    // EFFECTS: loads a list and updates table to reflect new list
    public void loadList(ToDoList list) {
        this.list = list;
        fireTableRowsInserted(0, list.getSize() - 1);
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public int getRowCount() {
        return list.getSize();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = list.getTask(rowIndex);
        switch (columnIndex) {
            case 0:
                return task.getName();
            case 1:
                return task.getDescription();
            case 2:
                String deadlineInString;
                if (task.getDeadline().equals(LocalDateTime.MAX)) {
                    deadlineInString = "None";
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    deadlineInString = task.getDeadline().format(formatter);
                }
                return deadlineInString;
            case 3:
                return task.getPriority();
            case 4:
                return task.isCompleted();
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
            case 2:
                return String.class;
            case 3:
                return Integer.class;
            case 4:
                return Boolean.class;
        }
        return null;
    }


}
