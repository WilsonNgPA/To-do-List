package ui;

import model.Event;
import model.EventLog;
import model.Task;
import model.ToDoList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents To-Do List's main window frame
 */
public class ToDoListUI extends JFrame {
    private static final int WIDTH = 1100;
    private static final int HEIGHT = 600;
    private static final String[] removeMenu = {"Select to remove task/s", "by name", "all overdue tasks",
            "all completed tasks", "all tasks"};
    private static final String[] sortMenu = {"Select to sort tasks", "by name", "by deadline",
            "by priority", "by completed"};
    private static final String[] modifyOptions = {"name", "description", "deadline", "priority", "completed status"};
    private static final String[] completedOptions = {"Set task as completed", "Set task as not completed"};
    private ToDoList list;
    private ToDoListTableModel listOfTasksModel;
    private JTable listOfTasks;
    private JPanel main;
    private JWindow window;
    private JScrollPane scrollPane;
    private JPanel buttonPanel;
    private JComboBox<String> removeTaskMenu;
    private JComboBox<String> sortTaskMenu;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private String jsonStore;


    /**
     * MODIFIES: this
     * EFFECTS: Constructor sets up button panel and to-do list table
     */
    public ToDoListUI() {
        displayOnStartup();

        main = new JPanel();
        main.setLayout(new BorderLayout());

        addButtonPanel();
        addToDoList();

        setContentPane(main);
        setTitle("To-Do List");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new PrintEventLogOnClose());

        centreOnScreen();
        setVisible(true);
    }

    /**
     * MODIFIES: this
     * EFFECTS: Helper to add buttons and combo boxes
     */
    private void addButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 2));

        buttonPanel.add(new JButton(new AddAddTaskButton()));

        removeTaskMenu = new JComboBox<>(removeMenu);
        removeTaskMenu.setSelectedIndex(0);
        removeTaskMenu.addActionListener(new AddRemoveTaskMenu());
        buttonPanel.add(removeTaskMenu);

        buttonPanel.add(new JButton(new AddModifyButton()));

        sortTaskMenu = new JComboBox<>(sortMenu);
        sortTaskMenu.setSelectedIndex(0);
        sortTaskMenu.addActionListener(new AddSortTaskMenu());
        buttonPanel.add(sortTaskMenu);

        buttonPanel.add(new JButton(new AddSaveButton()));

        buttonPanel.add(new JButton(new AddLoadButton()));

        main.add(buttonPanel, BorderLayout.WEST);
    }

    /**
     * MODIFIES: this
     * Adds to-do list table
     */
    private void addToDoList() {
        list = new ToDoList();
        scrollPane = new JScrollPane();
        listOfTasksModel = new ToDoListTableModel(list);
        listOfTasks = new JTable(listOfTasksModel);
        scrollPane.setViewportView(listOfTasks);
        main.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Referenced from AlarmSystem Repo
     * https://github.students.cs.ubc.ca/CPSC210/AlarmSystem/blob/master/src/main/ca/ubc/cpsc210/alarm/ui/AlarmControllerUI.java
     * MODIFIES: this
     * EFFECTS: Helper to centre main application window on desktop
     */
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    /**
     * MODIFIES: this
     * EFFECTS: Display startup splash art
     */
    private void displayOnStartup() {
        window = new JWindow();
        window.getContentPane().add(
                new JLabel("", new ImageIcon("./data/startupimgresized.jpg"), SwingConstants.CENTER));
        window.setBounds(0, 0, 1200, 600);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.setVisible(false);
        window.dispose();
    }

    /**
     * Represents actions to be taken when user wants to add task to to-do list
     */
    private class AddAddTaskButton extends AbstractAction {

        AddAddTaskButton() {
            super("Add task");
        }

        // MODIFIES: this
        // EFFECTS: adds task to to-do list according to user input
        @SuppressWarnings("methodlength")
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog(null, "Enter task name",
                    "Task name?", JOptionPane.QUESTION_MESSAGE);
            String description = JOptionPane.showInputDialog(null,
                    "Enter task description", "Task description?", JOptionPane.QUESTION_MESSAGE);
            int priority = Integer.parseInt(JOptionPane.showInputDialog(null,
                    "Enter task priority from 0 to 5 inclusive", "Task priority (0-5)?",
                    JOptionPane.QUESTION_MESSAGE));
            String deadlineInput = JOptionPane.showInputDialog(null,
                    "Enter task deadline(yyyy-MM-dd HH:mm), if none enter none", "Task deadline?",
                    JOptionPane.QUESTION_MESSAGE);
            try {
                if (name != null && !name.isEmpty()) {
                    if (priority >= 0 && priority <= 5) {
                        if (deadlineInput.equals("none")) {
                            listOfTasksModel.addTask(new Task(name, description, priority));
                        } else {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                            LocalDateTime deadline = LocalDateTime.parse(deadlineInput, formatter);
                            listOfTasksModel.addTask(new Task(name, description, deadline, priority));
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Priority entered not in required format, add failed");
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Name entered cannot be null or empty, add failed");
                }
            } catch (DateTimeParseException err) {
                JOptionPane.showMessageDialog(null,
                        "Deadline not in specified format, add failed");
            }
        }
    }

    /**
     * Represents actions to be taken when user wants to remove task/s from to-do list
     */
    private class AddRemoveTaskMenu extends AbstractAction {

        AddRemoveTaskMenu() {
            super("Remove task");
        }

        // MODIFIES: this
        // EFFECTS: removes task from to-do list according to option selected by user
        @Override
        public void actionPerformed(ActionEvent e) {
            String selected = (String) removeTaskMenu.getSelectedItem();
            assert selected != null;
            if (selected.equals(removeMenu[1])) {
                String name = JOptionPane.showInputDialog(null, "Enter task name",
                        "Task name?", JOptionPane.QUESTION_MESSAGE);
                listOfTasksModel.removeTask(name);
            } else if (selected.equals(removeMenu[2])) {
                listOfTasksModel.removeAllOverdueTasks();
            } else if (selected.equals(removeMenu[3])) {
                listOfTasksModel.removeAllCompletedTasks();
            } else if (selected.equals(removeMenu[4])) {
                listOfTasksModel.removeAllTasks();
            }
            removeTaskMenu.setSelectedIndex(0);
        }
    }

    /**
     * Represents action to be taken when user wants to modify task in to-do list
     */
    private class AddModifyButton extends AbstractAction {

        AddModifyButton() {
            super("Modify task");
        }

        // MODIFIES: this
        // EFFECTS: modifies task in to-do list according to option selected by user
        @SuppressWarnings("methodlength")
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog(null, "Enter task name",
                    "Task name?", JOptionPane.QUESTION_MESSAGE);
            if (list.containsTask(name)) {
                int selected = JOptionPane.showOptionDialog(null, "Modify task ___?",
                        "Modify task", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, modifyOptions, modifyOptions[0]);
                switch (selected) {
                    case 0:
                        String newName = JOptionPane.showInputDialog(null, "Enter new task name",
                                "New task name?", JOptionPane.QUESTION_MESSAGE);
                        if (name != null && !name.isEmpty()) {
                            listOfTasksModel.modifyTaskName(name, newName);
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Name entered cannot be null or empty, modify failed");
                        }
                        break;
                    case 1:
                        String newDescription = JOptionPane.showInputDialog(null,
                                "Enter new task description", "New task description?",
                                JOptionPane.QUESTION_MESSAGE);
                        listOfTasksModel.modifyTaskDescription(name, newDescription);
                        break;
                    case 2:
                        String deadlineInput = JOptionPane.showInputDialog(null,
                                "Enter task deadline(yyyy-MM-dd HH:mm), if none enter none",
                                "Task deadline?", JOptionPane.QUESTION_MESSAGE);
                        try {
                            if (deadlineInput.equals("none")) {
                                listOfTasksModel.modifyTaskDeadline(name, LocalDateTime.MAX);
                            } else {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                LocalDateTime deadline = LocalDateTime.parse(deadlineInput, formatter);
                                listOfTasksModel.modifyTaskDeadline(name, deadline);
                            }
                        } catch (DateTimeParseException err) {
                            JOptionPane.showMessageDialog(null,
                                     "Deadline not in specified format, modify failed");
                        }
                        break;
                    case 3:
                        int newPriority = Integer.parseInt(JOptionPane.showInputDialog(null,
                                "Enter new task priority from 0 to 5 inclusive", "New task priority (0-5)?",
                                JOptionPane.QUESTION_MESSAGE));
                        if (newPriority >= 0 && newPriority <= 5) {
                            listOfTasksModel.modifyTaskPriority(name, newPriority);
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Priority entered not in required format, modify failed");
                        }
                        break;
                    case 4:
                        int newIsCompleted = JOptionPane.showOptionDialog(null, "Set tasks as?",
                                "Modify task completed status", JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, completedOptions, completedOptions[0]);
                        listOfTasksModel.modifyTaskIsCompleted(name, newIsCompleted == 0);
                        break;
                }
            }
        }
    }

    /**
     * Represents action to be taken when user wants to sort tasks in to-do list
     */
    private class AddSortTaskMenu extends AbstractAction {

        AddSortTaskMenu() {
            super("Sort tasks");
        }

        // MODIFIES: this
        // EFFECTS: sorts to-do list according to option selected by user
        @Override
        public void actionPerformed(ActionEvent e) {
            String selected = (String) sortTaskMenu.getSelectedItem();
            assert selected != null;
            if (selected.equals(sortMenu[1])) {
                listOfTasksModel.sortTasksByName();
            } else if (selected.equals(sortMenu[2])) {
                listOfTasksModel.sortTasksByDeadline();
            } else if (selected.equals(sortMenu[3])) {
                listOfTasksModel.sortTasksByPriority();
            } else if (selected.equals(sortMenu[4])) {
                listOfTasksModel.sortTasksByCompleted();
            }
            sortTaskMenu.setSelectedIndex(0);
        }
    }

    /**
     * Represents action to be taken when user wants to save to-do list
     */
    private class AddSaveButton extends AbstractAction {

        AddSaveButton() {
            super("Save list");
        }

        // MODIFIES: this
        // EFFECTS: saves to-do list to file
        @SuppressWarnings("methodlength")
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!list.isEmpty()) {
                String listName = JOptionPane.showInputDialog(null, "Enter list name",
                        "List name?", JOptionPane.QUESTION_MESSAGE);
                if (listName != null && !listName.isEmpty()) {
                    list.setName(listName);
                    try {
                        jsonStore = "./data/" + listName + "ToDoList.json";
                        jsonWriter = new JsonWriter(jsonStore);
                        jsonWriter.open();
                        jsonWriter.write(list);
                        jsonWriter.close();
                        JOptionPane.showMessageDialog(null,
                                "Saved " + list.getName() + " to " + jsonStore);
                    } catch (FileNotFoundException err) {
                        JOptionPane.showMessageDialog(null,
                                "Unable to write to file: " + jsonStore);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "List name entered cannot be null or empty, save failed");
                }
            } else {
                JOptionPane.showMessageDialog(null, "List is empty, save failed");
            }
        }
    }

    /**
     * Represents action to be taken when user wants to load to-do list
     */
    private class AddLoadButton extends AbstractAction {

        AddLoadButton() {
            super("Load list");
        }

        // MODIFIES: this
        // EFFECTS: loads to-do list from file
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String listName = JOptionPane.showInputDialog(null, "Enter list name to load from",
                        "List name?", JOptionPane.QUESTION_MESSAGE);
                jsonStore = "./data/" + listName + "ToDoList.json";
                jsonReader = new JsonReader(jsonStore);
                list = jsonReader.read();
                listOfTasksModel.loadList(list);
            } catch (IOException err) {
                JOptionPane.showMessageDialog(null, "Unable to read from file: " + jsonStore);
            }
        }
    }

    /**
     * Represents action to be taken when user closes the window
     */
    private static class PrintEventLogOnClose extends WindowAdapter {

        // EFFECTS: Prints event log on window closing and exits program
        @Override
        public void windowClosing(WindowEvent windowEvent) {
            for (Event e : EventLog.getInstance()) {
                System.out.println(e.toString());
            }
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new ToDoListUI();
    }
}
