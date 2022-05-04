package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ToDoListTest {
    private ToDoList list;
    private Task t1, t2, t3, t4, t5;

    @BeforeEach
    void runBefore() {
        list = new ToDoList();
        t1 = new Task("task 1", "testing task 1",
                LocalDateTime.of(2025, 10, 12, 23, 10), 5);
        t2 = new Task("task 1", "Same but different task 1",
                LocalDateTime.of(2025, 10, 12, 23, 10), 5);
        t3 = new Task("task 3", "Different task");
        t4 = new Task("task 4", "Again different task", 3);
        t5 = new Task("task 5", "Definitely overdue", LocalDateTime.MIN, 2);
    }

    @Test
    void testSetName() {
        assertEquals("", list.getName());
        list.setName("test");
        assertEquals("test", list.getName());
    }

    @Test
    void testAddTask() {
        assertTrue(list.addTask(t1));
        assertFalse(list.addTask(t1));
        assertFalse(list.addTask(t2));
        assertTrue(list.addTask(t3));
        assertTrue(list.addTask(t4));
        assertTrue(list.addTask(t5));
    }

    @Test
    void testRemoveTask() {
        ArrayList<Integer> removedIndex = list.removeTask("task 1");
        assertEquals(0, removedIndex.size());

        list.addTask(t1);
        list.addTask(t3);
        list.addTask(t5);

        assertTrue(list.containsTask("task 1"));
        list.removeTask("task 1");
        assertFalse(list.containsTask("task 1"));

        assertTrue(list.containsTask("task 3"));
        list.removeTask("task 3");
        assertFalse(list.containsTask("task 3"));

        assertTrue(list.containsTask("task 5"));
        list.removeTask("task 5");
        assertFalse(list.containsTask("task 5"));

        assertEquals(0, list.getSize());
    }

    @Test
    void testRemoveAllCompletedTasks() {
        ArrayList<Integer> removedIndex = list.removeAllCompletedTasks();
        assertEquals(0, removedIndex.size());

        t1.setCompleted(true);
        t3.setCompleted(true);
        t4.setCompleted(true);
        list.addTask(t1);
        list.addTask(t3);
        list.addTask(t4);
        list.addTask(t5);
        list.removeAllCompletedTasks();
        assertFalse(list.containsTask("task 1"));
        assertFalse(list.containsTask("task 3"));
        assertFalse(list.containsTask("task 4"));
        assertTrue(list.containsTask("task 5"));
    }

    @Test
    void testRemoveAllOverdueTasks() {
        ArrayList<Integer> removedIndex = list.removeAllOverdueTasks();
        assertEquals(0, removedIndex.size());

        list.addTask(t1);
        list.addTask(t3);
        list.addTask(t4);
        list.addTask(t5);
        list.removeAllOverdueTasks();
        assertFalse(list.containsTask("task 5"));
        assertTrue(list.containsTask("task 1"));
        assertTrue(list.containsTask("task 3"));
        assertTrue(list.containsTask("task 4"));
    }

    @Test
    void testRemoveAllTasks() {
        list.addTask(t1);
        list.addTask(t3);
        list.addTask(t4);
        list.addTask(t5);
        list.removeAllTasks();
        assertFalse(list.containsTask("task 1"));
        assertFalse(list.containsTask("task 3"));
        assertFalse(list.containsTask("task 4"));
        assertFalse(list.containsTask("task 5"));
    }

    @Test
    void testGetTaskByName() {
        assertNull(list.getTask("something"));
        list.addTask(t1);
        assertEquals(t1, list.getTask("task 1"));
        assertNull(list.getTask("task 3"));
    }

    @Test
    void testGetTaskByIndex() {
        assertNull(list.getTask(0));
        list.addTask(t1);
        assertEquals(t1, list.getTask(0));
        assertNull(list.getTask(1));
    }

    @Test
    void testGetSize() {
        assertEquals(0, list.getSize());
        list.addTask(t1);
        assertEquals(1, list.getSize());
        list.addTask(t3);
        list.addTask(t4);
        assertEquals(3, list.getSize());
    }

    @Test
    void testGetIndexOfTask() {
        assertEquals(-1, list.getTaskIndex("task 1"));
        list.addTask(t1);
        assertEquals(0, list.getTaskIndex("task 1"));
        list.addTask(t3);
        list.addTask(t4);
        assertEquals(2, list.getTaskIndex("task 4"));
        assertEquals(-1, list.getTaskIndex("task 5"));
    }

    @Test
    void testSortByPriority() {
        list.addTask(t1);
        list.addTask(t3);
        list.addTask(t4);
        list.addTask(t5);
        assertEquals(0, list.getTaskIndex("task 1"));
        assertEquals(1, list.getTaskIndex("task 3"));
        assertEquals(2, list.getTaskIndex("task 4"));
        assertEquals(3, list.getTaskIndex("task 5"));
        list.sortByPriority();
        assertEquals(0, list.getTaskIndex("task 1"));
        assertEquals(1, list.getTaskIndex("task 4"));
        assertEquals(2, list.getTaskIndex("task 5"));
        assertEquals(3, list.getTaskIndex("task 3"));
    }

    @Test
    void testSortByDeadline() {
        list.addTask(t1);
        list.addTask(t3);
        list.addTask(t4);
        list.addTask(t5);
        assertEquals(0, list.getTaskIndex("task 1"));
        assertEquals(1, list.getTaskIndex("task 3"));
        assertEquals(2, list.getTaskIndex("task 4"));
        assertEquals(3, list.getTaskIndex("task 5"));
        list.sortByDeadline();
        assertEquals(0, list.getTaskIndex("task 5"));
        assertEquals(1, list.getTaskIndex("task 1"));
        assertEquals(2, list.getTaskIndex("task 3"));
        assertEquals(3, list.getTaskIndex("task 4"));
    }

    @Test
    void testSortByName() {
        list.addTask(t5);
        list.addTask(t4);
        list.addTask(t3);
        list.addTask(t1);
        assertEquals(0, list.getTaskIndex("task 5"));
        assertEquals(1, list.getTaskIndex("task 4"));
        assertEquals(2, list.getTaskIndex("task 3"));
        assertEquals(3, list.getTaskIndex("task 1"));
        list.sortByName();
        assertEquals(0, list.getTaskIndex("task 1"));
        assertEquals(1, list.getTaskIndex("task 3"));
        assertEquals(2, list.getTaskIndex("task 4"));
        assertEquals(3, list.getTaskIndex("task 5"));
    }

    @Test
    void testSortByIsCompleted() {
        list.addTask(t1);
        list.addTask(t3);
        list.addTask(t4);
        list.addTask(t5);
        assertEquals(0, list.getTaskIndex("task 1"));
        assertEquals(1, list.getTaskIndex("task 3"));
        assertEquals(2, list.getTaskIndex("task 4"));
        assertEquals(3, list.getTaskIndex("task 5"));
        list.getTask("task 1").setCompleted(true);
        list.getTask("task 4").setCompleted(true);
        list.sortByIsCompleted();
        assertEquals(0, list.getTaskIndex("task 5"));
        assertEquals(1, list.getTaskIndex("task 3"));
        assertEquals(2, list.getTaskIndex("task 1"));
        assertEquals(3, list.getTaskIndex("task 4"));
    }

    @Test
    void testToString() {
        assertEquals("To-Do List is empty", list.toString());
        list.addTask(t1);
        list.addTask(t3);
        list.addTask(t4);
        assertEquals("Task Name: " + t1.getName() + "\n\tDescription: " + t1.getDescription() +
                "\n\tDeadline: 2025-10-12 23:10" + "\n\tPriority: " + t1.getPriority() + "\n\tCompleted: " +
                t1.isCompleted() + "\n\nTask Name: " + t3.getName() + "\n\tDescription: " + t3.getDescription() +
                "\n\tDeadline: None" + "\n\tPriority: " + t3.getPriority() + "\n\tCompleted: " +
                t3.isCompleted() + "\n\nTask Name: " + t4.getName() + "\n\tDescription: " + t4.getDescription() +
                "\n\tDeadline: None" + "\n\tPriority: " + t4.getPriority() + "\n\tCompleted: " +
                t4.isCompleted() + "\n\n", list.toString());
    }

}
