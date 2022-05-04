package model;

import java.util.Comparator;

// Represents a comparator that compares task by deadline
public class DeadlineComparator implements Comparator<Task> {
    @Override
    // REQUIRES: o1 != null and o2 != null
    // EFFECTS: returns -1 if o1 deadline is less than o2 deadline, 0 if their deadline is equal and 1 if o1 deadline is
    //          greater than o2 deadline.
    public int compare(Task o1, Task o2) {
        return o1.getDeadline().compareTo(o2.getDeadline());
    }
}
