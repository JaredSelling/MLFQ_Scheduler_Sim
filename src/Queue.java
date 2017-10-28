import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Queue<Process extends Comparable<? super Process>> {

    private List<Process> list = new LinkedList<Process>();

    public void enqueue(Process item) {
        list.add(item);
    }

    public Process dequeue() {
        return list.remove(0);
    }

    public boolean hasProcesses() {
        return !list.isEmpty();
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() { return list.isEmpty(); }


    public Process getItem(int index) { return list.get(index);}

    public void sort() {
        Collections.sort(list);
    }

    public void remove(Process p) {
        list.remove(p);
    }

    public void clear() {
        list.clear();
    }



}
