public class MLFQ {
    public static void main(String[] args) {
        //instantiate processes
        Process P1 = new Process("P1", new int[]{4, 5, 6, 7, 6, 4, 5, 4}, new int[]{15, 31, 26, 24, 41, 51, 16}, 0);
        Process P2 = new Process("P2", new int[]{9, 11, 15, 12, 8, 11, 9, 10, 7}, new int[]{28, 22, 21, 12, 8, 11, 9, 10}, 0);
        Process P3 = new Process("P3", new int[]{24, 12, 6, 17, 11, 22, 18}, new int[]{28, 21, 27, 21, 54, 31}, 0);
        Process P4 = new Process("P4", new int[]{15, 14, 16, 18, 14, 13, 16, 15}, new int[]{35, 41, 45, 51, 61, 54, 61}, 0);
        Process P5 = new Process("P5", new int[]{6, 5, 15, 4, 7, 4, 6, 10, 3}, new int[]{22, 21, 31, 26, 31, 18, 21, 33}, 0);
        Process P6 = new Process("P6", new int[]{22, 27, 25, 11, 19, 18, 6, 6}, new int[]{38, 41, 29, 26, 32, 22, 26}, 0);
        Process P7 = new Process("P7", new int[]{4, 7, 6, 5, 4, 7, 6, 5, 6, 9}, new int[]{36, 31, 32, 41, 42, 39, 33, 34, 21}, 0);
        Process P8 = new Process("P8", new int[]{5, 4, 6, 4, 6, 5, 4, 6, 6}, new int[]{14, 33, 31, 31, 27, 21, 19, 11}, 0);

        Queue<Process> readyQueue = new Queue<Process>();
        Queue<Process> blockingQueue = new Queue<>();
        Queue<Process> completeQueue = new Queue<>();


        int curTime = 0;

        boolean preemptFlag = false;

        //add processes to ready queue
        readyQueue.enqueue(P1);
        readyQueue.enqueue(P2);
        readyQueue.enqueue(P3);
        readyQueue.enqueue(P4);
        readyQueue.enqueue(P5);
        readyQueue.enqueue(P6);
        readyQueue.enqueue(P7);
        readyQueue.enqueue(P8);

        //get first process
        Process curProc = readyQueue.dequeue();

        displayInfo(readyQueue, blockingQueue, completeQueue, curProc, curTime);


        //Main loop
        while ((readyQueue.hasProcesses() || blockingQueue.hasProcesses()) && curTime < 279) {

            curTime++;

            //decrement ioBurst of every process in blocking queue
            if (blockingQueue.hasProcesses()) {
                //new queue to hold items to remove from the blocking queue
                Queue<Process> toRemove = new Queue<>();
                for (int x = 0; x < blockingQueue.size(); x++) {
                    //decrement current io burst if it is greater than 0
                    if (blockingQueue.getItem(x).getCurrentIO() > 0) {
                        blockingQueue.getItem(x).decrementCurrentIO();
                    }
                    //increment io index and mark process to be removed from blocking queue if its io burst = 0
                    if (blockingQueue.getItem(x).getCurrentIO() <= 0) {
                        blockingQueue.getItem(x).incrementCurrentIOIndex();
                        toRemove.enqueue(blockingQueue.getItem(x));
                        //if process has completed all cpu and i/o bursts, it is complete
                        if (blockingQueue.getItem(x).isComplete()) {
                            blockingQueue.getItem(x).setTurnaroundTime(curTime);
                            blockingQueue.getItem(x).setWaitingTime(blockingQueue.getItem(x).getTurnaroundTime() - blockingQueue.getItem(x).getTotalBurstTime());
                            completeQueue.enqueue(blockingQueue.getItem(x));
                            System.out.println(blockingQueue.getItem(x).getId() + " HAS COMPLETED");
                        } else {
                            blockingQueue.getItem(x).setState("READY");
                            blockingQueue.getItem(x).setArrivalTime(curTime);
                            readyQueue.enqueue(blockingQueue.getItem(x));
                            if (curProc == null) {
                                curProc = readyQueue.dequeue();
                                curProc.setResponseTime(curTime);
                            }
                        }
                    }
                }
                //remove from the blocking queue all processes that are in toRemove
                for (int y = 0; y < toRemove.size(); y++) {
                    blockingQueue.remove(toRemove.getItem(y));
                }
            }


            //check for preempting processes
            Queue<Process> preemptCandidates = new Queue<Process>();

            //loop through ready queue and find all candidates for preemption
            for (int i = 0; i < readyQueue.size(); i++) {
                if (readyQueue.getItem(i).getPriority() < curProc.getPriority() && readyQueue.getItem(i).getArrivalTime() <= curTime) {
                    preemptCandidates.enqueue(readyQueue.getItem(i));
                    preemptFlag = true;
                } else {
                    preemptFlag = false;
                }
            }


            //do RR scheduling with tq = 6 if current process is a priority1
            //do RR scheduling with tq = 12 if current process is a priority2
            if (curProc.getPriority() == 1 || curProc.getPriority() == 2) {
                if (curProc.getCurrentBurst() > 0 && curProc.getTimeQuantum() > 0) {
                    //decrement the current burst
                    curProc.decrementCurrentBurst();
                    //decrement remaining time quantum
                    curProc.decrementTimeQuantum();
                }
                //handle process that completes its current burst
                if (curProc.getCurrentBurst() == 0 && curProc.getTimeQuantum() >= 0) {
                    if(curProc.isComplete()) {
                        readyQueue.remove(curProc);
                        completeQueue.enqueue(curProc);
                        if(readyQueue.hasProcesses()) {
                            curProc = getNextProcess(readyQueue, curProc, curTime);
                            readyQueue.remove(curProc);
                        }

                    } else {
                        //add process to blocking queue
                        curProc.resetTimeQuantum();
                        curProc.incrementCurrentBurstIndex();
                        blockingQueue.enqueue(curProc);
                        System.out.println("*************************************************");
                        System.out.println("AFTER " + curProc.getId() + " COMPLETES CURRENT BURST");
                        System.out.println("*************************************************");
                        //get next process from ready queue
                        curProc = getNextProcess(readyQueue, curProc, curTime);
                        readyQueue.remove(curProc);
                        displayInfo(readyQueue, blockingQueue, completeQueue, curProc, curTime);
                        //curProc = readyQueue.dequeue();
                    }

                }
/*                //handle process that completes current burst and time quantum simultaneously
                if(curProc.getCurrentBurst() == 0 && curProc.getTimeQuantum() == 0) {

                }*/
                //handle process that gets preempted for time quantum
                if (curProc.getCurrentBurst() > 0 && curProc.getTimeQuantum() == 0 && curProc.getPriority() < 3) {
                    //decrease priority
                    curProc.decreasePriority();
                    //update time quantum to match lowered priority
                    curProc.resetTimeQuantum();
                    //update arrival time
                    curProc.setArrivalTime(curTime);
                    //add process back into ready queue
                    readyQueue.enqueue(curProc);
                    System.out.println("*************************************************");
                    System.out.println("BEFORE " + curProc.getId() + "IS PREEMPTED FOR TIME QUANTUM");
                    System.out.println("*************************************************");
                    displayInfo(readyQueue, blockingQueue, completeQueue, curProc, curTime);
                    //get next process
                    curProc = getNextProcess(readyQueue, curProc, curTime);
                    readyQueue.remove(curProc);
                    System.out.println("*************************************************");
                    System.out.println("AFTER PREEMPTION FOR TIME QUANTUM");
                    System.out.println("*************************************************");

                    displayInfo(readyQueue, blockingQueue, completeQueue, curProc, curTime);
                    //curProc = readyQueue.dequeue();
                }


            } else if (curProc.getPriority() == 3) {
                //TODO IMPLEMENT FCFS
                if(curProc.getCurrentBurst() > 0) {
                    curProc.decrementCurrentBurst();
                }
                if(curProc.getCurrentBurst() == 0) {
                    curProc.incrementCurrentBurstIndex();
                    blockingQueue.enqueue(curProc);
                    if(readyQueue.hasProcesses()) {
                        curProc = getNextProcess(readyQueue, curProc, curTime);
                        readyQueue.remove(curProc);
                    }
                    displayInfo(readyQueue, blockingQueue, completeQueue, curProc, curTime);
                }
                if(curProc.isComplete()) {
                    completeQueue.enqueue(curProc);
                    readyQueue.remove(curProc);
                }
            }

            if (preemptFlag) {
                //sort preemptCandidates queue by arrival time, earliest will preempt
                preemptCandidates.sort();
                //update arrival time
                curProc.setArrivalTime(curTime);
                //reset process's time quantum
                curProc.resetTimeQuantum();
                //place current process back in ready queue
                readyQueue.enqueue(curProc);
                System.out.println("*************************************************");
                System.out.println("BEFORE " + curProc.getId() + " IS PREEMPTED");
                System.out.println("*************************************************");
                displayInfo(readyQueue, blockingQueue, completeQueue, curProc, curTime);
                //preemption
                curProc = preemptCandidates.dequeue();
                readyQueue.remove(curProc);
                System.out.println("*************************************************");
                System.out.println("AFTER PREEMPTION");
                System.out.println("*************************************************");
                displayInfo(readyQueue, blockingQueue, completeQueue, curProc, curTime);

                preemptCandidates.clear();
                preemptFlag = false;
            }

            displayInfo(readyQueue, blockingQueue, completeQueue, curProc, curTime);
            //add curProc to BLOCKING queue and go out to IO

            //curProc = next process in priority queue = curProc.priority


            //if curProc.priority == 3 do FCFS scheduling


            //loop through BLOCKING queue

            //decrement I/O time of every process

            //if a process's curIO == 0

            //increment curIOTimeIndex for that process

            //process's new arrival time = curTime


/*            //output status info
            System.out.println("Current Time:   " + curTime);
            System.out.println("Now running:    " + curProc.getId());
            System.out.println("Priority: " + curProc.getPriority());
            System.out.println("Current burst: " + curProc.getCurrentBurst());
            System.out.println("Current burst index: " + curProc.getCurrentBurstIndex() + "/" + curProc.getBurstTimesSize());
            System.out.println("Current io burst index: " + curProc.getCurrentIOIndex() + "/" + curProc.getIOTimesSize());
            System.out.println("------------------------------------");
            System.out.println("Ready Queue:    Process    Burst    ArrivalTime    CurBurstIndex    CurIOIndex    Priority");
            for (int i = 0; i < readyQueue.size(); i++) {
                System.out.println("                " + readyQueue.getItem(i).getId() + "         " + readyQueue.getItem(i).getCurrentBurst() +
                        "        " + readyQueue.getItem(i).getArrivalTime() + "              " + readyQueue.getItem(i).getCurrentBurstIndex() +
                        "/" + readyQueue.getItem(i).getBurstTimesSize() + "              " + readyQueue.getItem(i).getCurrentIOIndex() + "/" + readyQueue.getItem(i).getIOTimesSize()
                        + "           " + readyQueue.getItem(i).getPriority()
                );
            }
            System.out.println("------------------------------------");
            System.out.println("Now in I/O:     Process    Remaining I/O time    curIOIndex    curBurstIndex    curBurst");
            for (int j = 0; j < blockingQueue.size(); j++) {
                System.out.println("                " + blockingQueue.getItem(j).getId() + "         " + blockingQueue.getItem(j).getCurrentIO() +
                        "                     " + blockingQueue.getItem(j).getCurrentIOIndex() + "/" + blockingQueue.getItem(j).getIOTimesSize() +
                        "          " + blockingQueue.getItem(j).getCurrentBurstIndex() + "/" + blockingQueue.getItem(j).getBurstTimesSize() + "              " + blockingQueue.getItem(j).getCurrentBurst()
                );

            }
            System.out.println("------------------------------------");
            System.out.println("------------------------------------");*/
        }

    }

    public static Process getNextProcess(Queue<Process> readyQueue, Process curProc, int curTime) {
        Queue<Process> candidateProcs = new Queue<>();

        //look for priority 1 processes first
        for (int i = 0; i < readyQueue.size(); i++) {
            if (readyQueue.getItem(i).getPriority() == 1 && readyQueue.getItem(i).getArrivalTime() <= curTime) {
                candidateProcs.enqueue(readyQueue.getItem(i));
            }
        }
        //if no priority 1s found, look for priority 2s
        if (candidateProcs.isEmpty()) {
            for (int j = 0; j < readyQueue.size(); j++) {
                if (readyQueue.getItem(j).getPriority() == 2 && readyQueue.getItem(j).getArrivalTime() <= curTime) {
                    candidateProcs.enqueue(readyQueue.getItem(j));
                }
            }
        }
        //if no priority 2s found, look for priority 3s
        if (candidateProcs.isEmpty()) {
            for (int k = 0; k < readyQueue.size(); k++) {
                if (readyQueue.getItem(k).getPriority() == 3 && readyQueue.getItem(k).getArrivalTime() <= curTime) {
                    candidateProcs.enqueue(readyQueue.getItem(k));
                }
            }
        }

        candidateProcs.sort();

        return candidateProcs.dequeue();
    }

    public static void displayInfo(Queue<Process> readyQueue, Queue<Process> blockingQueue, Queue<Process> completeQueue, Process curProc, int curTime) {
        //output status info
        System.out.println("Current Time:   " + curTime);
        System.out.println("Now running:    " + curProc.getId());
        System.out.println("Priority: " + curProc.getPriority());
        System.out.println("Time quantum remaining: " + curProc.getTimeQuantum());
        System.out.println("Current burst: " + curProc.getCurrentBurst());
        System.out.println("Current burst index: " + curProc.getCurrentBurstIndex() + "/" + curProc.getBurstTimesSize());
        System.out.println("Current io burst index: " + curProc.getCurrentIOIndex() + "/" + curProc.getIOTimesSize());
        System.out.println("------------------------------------");
        System.out.println("Ready Queue:    Process    Burst    ArrivalTime    CurBurstIndex    CurIOIndex    Priority");
        for (int i = 0; i < readyQueue.size(); i++) {
            System.out.println("                " + readyQueue.getItem(i).getId() + "         " + readyQueue.getItem(i).getCurrentBurst() +
                    "        " + readyQueue.getItem(i).getArrivalTime() + "              " + readyQueue.getItem(i).getCurrentBurstIndex() +
                    "/" + readyQueue.getItem(i).getBurstTimesSize() + "              " + readyQueue.getItem(i).getCurrentIOIndex() + "/" + readyQueue.getItem(i).getIOTimesSize()
                    + "           " + readyQueue.getItem(i).getPriority()
            );
        }
        System.out.println("------------------------------------");
        System.out.println("Now in I/O:     Process    Remaining I/O time    curIOIndex    curBurstIndex    curBurst");
        for (int j = 0; j < blockingQueue.size(); j++) {
            System.out.println("                " + blockingQueue.getItem(j).getId() + "         " + blockingQueue.getItem(j).getCurrentIO() +
                    "                     " + blockingQueue.getItem(j).getCurrentIOIndex() + "/" + blockingQueue.getItem(j).getIOTimesSize() +
                    "          " + blockingQueue.getItem(j).getCurrentBurstIndex() + "/" + blockingQueue.getItem(j).getBurstTimesSize() + "              " + blockingQueue.getItem(j).getCurrentBurst()
            );

        }
        System.out.println("------------------------------------");
        System.out.println("Complete:      Process");
        for(int k=0; k<completeQueue.size(); k++) {
            System.out.println("               " + completeQueue.getItem(k).getId());
        }
        System.out.println("------------------------------------");
        System.out.println("------------------------------------");
    }
}