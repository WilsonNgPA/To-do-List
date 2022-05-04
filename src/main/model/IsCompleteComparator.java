package model;

import java.util.Comparator;

// Represents a comparator that compares task by completion status
public class IsCompleteComparator implements Comparator<Task> {
    @Override
    // REQUIRES: o1 != null and o2 != null
    // EFFECTS: returns -1 if o1 is not complete and o2 is complete, 1 if o1 is complete and o2 is complete. Otherwise,
    //          compare by their deadline instead and returns -1 if o1 deadline is less than o2 deadline,
    //          0 if their deadline is equal and 1 if o1 deadline is greater than o2 deadline.
    public int compare(Task o1, Task o2) {
        int compareByIsComplete = Boolean.compare(o1.isCompleted(), o2.isCompleted());
        if (compareByIsComplete != 0) {
            return compareByIsComplete;
        }
        return o1.getDeadline().compareTo(o2.getDeadline());
    }
}
