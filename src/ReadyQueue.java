import java.util.LinkedList;

public class ReadyQueue<Process> {

    private LinkedList<Process> readyQueue = new LinkedList<Process>();

    public void enqueue(Process item) {
        readyQueue.addLast(item);
    }

    public Process dequeue() {
        return readyQueue.poll();
    }

    public boolean hasProcesses() {
        return !readyQueue.isEmpty();
    }

    public int size() {
        return readyQueue.size();
    }

    public boolean isEmpty() { return readyQueue.isEmpty(); }


    public Process getItem(int index) { return readyQueue.get(index);}

    public void printReadyQueue(ReadyQueue readyQueue) {

    }

}