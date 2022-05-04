package persistence;

import model.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkTask(String taskName, String taskDescription, LocalDateTime deadline, int priority, boolean completed, Task task) {
        assertEquals(taskName, task.getName());
        assertEquals(taskDescription, task.getDescription());
        assertEquals(deadline, task.getDeadline());
        assertEquals(priority, task.getPriority());
        assertEquals(completed, task.isCompleted());
    }
}
