package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    private Task t1, t2, t3, t4, t5, t6;

    @BeforeEach
    void runBefore() {
        t1 = new Task("task 1", "testing", -1);
        t2 = new Task("task 2", "testing testing");
        t3 = new Task("task 3", "testing deadline",
                LocalDateTime.of(2021, 12, 12, 23, 10), 5, true);
        t4 = new Task("task 4", "testing testing testing testing", 6);
        t5 = new Task("task 5", "", LocalDateTime.MAX, -3, true);
        t6 = new Task("task 5", "", LocalDateTime.MAX, 8, false);
    }

    @Test
    void testGetName() {
        assertEquals("task 1", t1.getName());
        assertEquals("task 2", t2.getName());
    }

    @Test
    void testSetName() {
        assertEquals("task 1", t1.getName());
        t1.setName("task task");
        assertEquals("task task", t1.getName());
    }

    @Test
    void testGetDescription() {
        assertEquals("testing", t1.getDescription());
        assertEquals("testing testing", t2.getDescription());
    }

    @Test
    void testSetDescription() {
        assertEquals("testing testing", t2.getDescription());
        t2.setDescription("testing testing testing");
        assertEquals("testing testing testing", t2.getDescription());
    }

    @Test
    void testGetDeadline() {
        assertEquals(LocalDateTime.MAX, t1.getDeadline());
        assertEquals(LocalDateTime.of(2021, 12, 12, 23, 10), t3.getDeadline());
    }

    @Test
    void testSetDeadline() {
        assertEquals(LocalDateTime.MAX, t1.getDeadline());
        t1.setDeadline(LocalDateTime.of(2022, 6, 20, 22, 15));
        assertEquals(LocalDateTime.of(2022, 6, 20, 22, 15), t1.getDeadline());
    }

    @Test
    void testGetPriority() {
        assertEquals(0, t1.getPriority());
        assertEquals(0, t2.getPriority());
        assertEquals(5, t3.getPriority());
        assertEquals(0, t4.getPriority());
        assertEquals(0, t5.getPriority());
        assertEquals(0, t6.getPriority());
    }

    @Test
    void testSetPriority() {
        assertEquals(0, t1.getPriority());
        t1.setPriority(3);
        assertEquals(3, t1.getPriority());
        t1.setPriority(-1);
        assertEquals(3, t1.getPriority());
        t1.setPriority(8);
        assertEquals(3, t1.getPriority());
    }

    @Test
    void testIsCompleted() {
        assertFalse(t1.isCompleted());
        assertFalse(t2.isCompleted());
        assertTrue(t3.isCompleted());
        assertTrue(t5.isCompleted());
        assertFalse(t6.isCompleted());
    }

    @Test
    void testSetCompleted() {
        assertFalse(t1.isCompleted());
        t1.setCompleted(true);
        assertTrue(t1.isCompleted());

        assertFalse(t2.isCompleted());
        t2.setCompleted(true);
        assertTrue(t2.isCompleted());
    }

    @Test
    void testToString() {
        assertEquals("Task Name: " + t1.getName() + "\n\tDescription: " + t1.getDescription() +
                "\n\tDeadline: None" + "\n\tPriority: " + t1.getPriority() + "\n\tCompleted: " +
                t1.isCompleted() + "\n", t1.toString());
        assertEquals("Task Name: " + t3.getName() + "\n\tDescription: " + t3.getDescription() +
                "\n\tDeadline: 2021-12-12 23:10" + "\n\tPriority: " + t3.getPriority() + "\n\tCompleted: " +
                t3.isCompleted() + "\n", t3.toString());
    }
}
