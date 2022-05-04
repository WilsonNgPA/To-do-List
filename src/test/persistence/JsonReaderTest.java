package persistence;

import model.ToDoList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest{
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ToDoList list = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyToDoList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyToDoList.json");
        try {
            ToDoList list = reader.read();
            assertEquals("Test", list.getName());
            assertEquals(0, list.getSize());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralToDoList() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralToDoList.json");
        try {
            ToDoList list = reader.read();
            assertEquals("Test", list.getName());
            assertEquals(2, list.getSize());
            checkTask("task 5", "description 5",
                    LocalDateTime.of(2023, 12, 12, 22, 1), 2,
                    true, list.getTask(0));
            checkTask("task 3", "description 3",
                    LocalDateTime.MAX, 4,
                    false, list.getTask(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
