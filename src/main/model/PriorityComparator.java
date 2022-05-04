package model;

import java.util.Comparator;

// Represents a comparator that compares task by priority
public class PriorityComparator implements Comparator<Task> {
    @Override
    // REQUIRES: o1 != null and o2 != null
    // EFFECTS: returns -1 if o1 priority is less than o2 deadline, 0 if their priority is equal and 1 if o1 deadline is
    //          greater than o2 priority.
    public int compare(Task o1, Task o2) {
        return o2.getPriority() - o1.getPriority();
    }
}
