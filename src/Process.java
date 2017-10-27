
public class Process implements Comparable<Process> {
    private String id;
    private int arrivalTime;
    private int[] burstTimes;
    private int[] ioTimes;
    private int currentBurst;
    private int currentIO;
    private Integer responseTime;
    private Integer waitingTime;
    private Integer turnaroundTime;
    private int currentBurstIndex;
    private int currentIOIndex;
    private String state;
    private int totalBurstTime;
    private int priority;
    private int timeQuantum;

    //Constructor

    public Process(String id, int[] burstTimes, int[] ioTimes, int arrivalTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTimes = burstTimes;
        this.ioTimes = ioTimes;
        this.currentBurstIndex = 0;
        this.currentIOIndex = 0;
        this.state="READY";
        this.priority = 1;
        this.timeQuantum = 6;

        this.totalBurstTime = calculateTotalBurstTime();
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

    public int getBurstTimesSize() { return this.burstTimes.length-1;}

    public int[] getIoTimes() {
        return ioTimes;
    }

    public int getIOTimesSize() { return this.ioTimes.length-1;}

    public int getCurrentBurst() {
        return burstTimes[currentBurstIndex];
    }

    public int getCurrentIO() {
        return ioTimes[currentIOIndex];
    }

    public Integer getResponseTime() {
        return responseTime;
    }

    public Integer getWaitingTime() {
        return waitingTime;
    }

    public Integer getTurnaroundTime() {
        return turnaroundTime;
    }

    public String getState() {
        return state;
    }

    public int getCurrentBurstIndex() {
        return currentBurstIndex;
    }

    public int getCurrentIOIndex() {
        return currentIOIndex;
    }

    public int calculateTotalBurstTime() {
        int sum = 0;
        for(int i = 0; i<burstTimes.length; i++) {
            sum += burstTimes[i];
        }
        return sum;
    }

    public int getTotalBurstTime() {
        return totalBurstTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getTimeQuantum() {
        return timeQuantum;
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

    public void decrementCurrentBurst() { this.burstTimes[currentBurstIndex]--;}

    public void incrementCurrentBurst() {if(this.currentBurstIndex < this.burstTimes.length) this.burstTimes[currentBurstIndex]++; }

    public void incrementCurrentIO() {if(this.currentIOIndex < this.ioTimes.length) this.ioTimes[currentIOIndex]++;}

    public void decrementCurrentIO() { this.ioTimes[currentIOIndex]--;}

    public void setCurrentIO() {
        this.currentIO = ioTimes[this.currentIOIndex];
    }

    public void setResponseTime(int responseTime) {
        if(this.responseTime == null) this.responseTime = responseTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        if(this.turnaroundTime == null) {
            this.turnaroundTime = turnaroundTime;
        }
    }

    public void incrementCurrentBurstIndex() {if(this.currentBurstIndex < this.burstTimes.length-1) {this.currentBurstIndex++;} }

    public void incrementCurrentIOIndex() { if(this.currentIOIndex < this.ioTimes.length-1) {this.currentIOIndex++;} }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isComplete() {
        for(int i : this.burstTimes) {
            if(i != 0) {
                return false;
            }
        }
        for(int j : this.ioTimes) {
            if(j != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean executionComplete() {
        for(int i : this.burstTimes) {
            if(i != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean ioComplete() {
        for(int i : this.ioTimes) {
            if(i != 0) {
                return false;
            }
        }
        return true;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void decrementTimeQuantum() {
            timeQuantum--;
    }

    public void setTimeQuantum() {
        switch(priority) {
            case 1:
                timeQuantum = 6;
                break;
            case 2:
                timeQuantum = 12;
                break;
            default:
                break;
        }
    }

    public void decreasePriority() {
        if(this.priority != 3) {
            this.priority++;
        }
    }

    @Override
    public int compareTo(Process comparedProc) {
        int compareArrivalTime = ((Process) comparedProc).getArrivalTime();
        return this.arrivalTime - compareArrivalTime;
    }
}
