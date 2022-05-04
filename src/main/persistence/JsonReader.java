package persistence;

import model.Task;
import model.ToDoList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

// This class references code from JsonSerializationDemo repo
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
// Represents a reader that reads to-do list from JSON data stored in file
public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads ToDoList from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ToDoList read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseToDoList(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach((contentBuilder::append));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses ToDoList from JSON object and returns it
    private ToDoList parseToDoList(JSONObject jsonObject) {
        String name = jsonObject.getString("listName");
        ToDoList list = new ToDoList(name);
        addTasks(list, jsonObject);
        return list;
    }

    // MODIFIES: list
    // EFFECTS: parses tasks from JSON object and adds them to ToDoList
    private void addTasks(ToDoList list, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("tasks");
        for (Object json : jsonArray) {
            JSONObject nextTask = (JSONObject) json;
            addTask(list, nextTask);
        }
    }

    // MODIFIES: list
    // EFFECTS: parses task from JSON object and adds it to ToDoList
    private void addTask(ToDoList list, JSONObject jsonObject) {
        String taskName = jsonObject.getString("taskName");
        String taskDescription = jsonObject.getString("taskDescription");
        String readDeadline = jsonObject.getString("deadline");
        LocalDateTime deadline;
        if (readDeadline.equals("None")) {
            deadline = LocalDateTime.MAX;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            deadline = LocalDateTime.parse(readDeadline, formatter);
        }
        int priority = jsonObject.getInt("priority");
        boolean completed = jsonObject.getBoolean("completed");
        list.addTask(new Task(taskName, taskDescription, deadline, priority, completed));
    }


}
