package model;

import java.util.Comparator;

// Represents a comparator that compares task by name
public class NameComparator implements Comparator<Task> {

    @Override
    // REQUIRES: o1 != null and o2 != null
    // EFFECTS: returns -1 if o1 name is less than o2 name, 0 if their name is equal and 1 if o1 name is
    //          greater than o2 name.
    public int compare(Task o1, Task o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
