public class Process implements Comparable<Process> {
    private String id;
    private int arrivalTime;
    private int[] burstTimes;
    private int[] ioTimes;
    private int currentBurst;
    private int currentIO;
    private int responseTime;
    private int waitingTime;
    private int turnaroundTime;
    private int currentBurstIndex;
    private int currentIOIndex;

    //Constructor

    public Process(String id, int[] burstTimes, int[] ioTimes, int arrivalTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTimes = burstTimes;
        this.ioTimes = ioTimes;
        this.currentBurstIndex = 0;
        this.currentIOIndex = 0;
    }


    //Getters

    public int getArrivalTime() {
        return arrivalTime;
    }

    public String getId() {
        return id;
    }

    public int[] getBurstTimes() {
        return burstTimes;
    }

    public int[] getIoTimes() {
        return ioTimes;
    }

    public int getCurrentBurst() {
        return currentBurst;
    }

    public int getCurrentIO() {
        return currentIO;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }


    //Setters

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBurstTimes(int[] burstTimes) {
        this.burstTimes = burstTimes;
    }

    public void setIoTimes(int[] ioTimes) {
        this.ioTimes = ioTimes;
    }

    public void setCurrentBurst() {
        this.currentBurst = this.burstTimes[this.currentBurstIndex];
    }

    public void setCurrentIO() {
        this.currentIO = ioTimes[this.currentIOIndex];
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public void incrementCurrentBurstIndex() { this.currentBurstIndex++; }

    public void incrementCurrentIOIndex() { this.currentIOIndex++; }

    @Override
    public int compareTo(Process process) {
        int compareArrivalTime = ((Process) process).getArrivalTime();
        return this.arrivalTime - compareArrivalTime;
    }
}