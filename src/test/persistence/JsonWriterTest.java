package persistence;

import model.Task;
import model.ToDoList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {
    @Test
    void testWriterInvalidFile() {
        try {
            ToDoList list = new ToDoList("Test");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyToDoList() {
        try {
            ToDoList list = new ToDoList("Test");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyToDoList.json");
            writer.open();
            writer.write(list);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyToDoList.json");
            list = reader.read();
            assertEquals("Test", list.getName());
            assertEquals(0, list.getSize());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralToDoList() {
        try {
            ToDoList list = new ToDoList("Test");
            list.addTask(new Task("task 1", "description 1", LocalDateTime.MAX, 3));
            list.addTask(new Task("task 2", "description 2",
                    LocalDateTime.of(2022, 8, 8, 23, 59), 0, true));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralToDoList.json");
            writer.open();
            writer.write(list);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralToDoList.json");
            list = reader.read();
            assertEquals("Test", list.getName());
            assertEquals(2, list.getSize());
            checkTask("task 1", "description 1", LocalDateTime.MAX, 3,
                    false, list.getTask(0));
            checkTask("task 2", "description 2",
                    LocalDateTime.of(2022, 8, 8, 23, 59), 0,
                    true, list.getTask(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
