public class MLFQ {
    public static void main(String[] args) {
        //instantiate processes
        Process P1 = new Process("P1", new int[]{4, 5, 6, 7, 6, 4, 5, 4}, new int[]{15, 31, 26, 24, 41, 51, 16}, 0);
        Process P2 = new Process("P2", new int[]{9, 11, 15, 12, 8, 11, 9, 10, 7}, new int[]{28, 22, 15, 12, 8, 11, 9, 10}, 0);
        Process P3 = new Process("P3", new int[]{24, 12, 6, 17, 11, 22, 18}, new int[]{28, 21, 27, 21, 54, 31}, 0);
        Process P4 = new Process("P4", new int[]{15, 14, 16, 18, 14, 13, 16, 15}, new int[]{35, 41, 45, 51, 61, 54, 61}, 0);
        Process P5 = new Process("P5", new int[]{6, 5, 15, 4, 7, 4, 6, 10, 3 }, new int[]{22, 21, 31, 26, 31, 18, 21, 33}, 0);
        Process P6 = new Process("P6", new int[]{22, 27, 25, 11, 19, 18, 6, 6}, new int[]{38, 41, 29, 26, 32, 22, 26}, 0);
        Process P7 = new Process("P7", new int[]{4, 7, 6, 5, 4, 7, 6, 5, 6, 9}, new int[]{36, 31, 32, 41, 42, 39, 33, 34, 21}, 0);
        Process P8 = new Process("P8", new int[]{5, 4, 6, 4, 6, 5, 4, 6, 6}, new int[]{14, 33, 31, 31, 37, 21, 19, 11}, 0);

        ReadyQueue<Process> readyQueue = new ReadyQueue<Process>();

        //add processes to ready queue
        readyQueue.enqueue(P1);
        readyQueue.enqueue(P2);
        readyQueue.enqueue(P3);
        readyQueue.enqueue(P4);
        readyQueue.enqueue(P5);
        readyQueue.enqueue(P6);
        readyQueue.enqueue(P7);
        readyQueue.enqueue(P8);


        //Main loop

            //curTime = current time

            //timeQuantumCounter = 0

            //curProc = currently executing process

            //curBurst = currently executing process's current cpu burst

            //curIO = currentIO time of a process

            //curIOTimeIndex = index of the current IO (within IOtimes array) for a process




            //loop through all priority queues > curProc.priority

                //if there exists a process with priority > curProc.priority and arrival time < curTime

                    //preemptProc = process with earliest arrival time < curTime

                    //save curProc's state (PCB)

                    //curProc = preemptProc


            //if curProc.priority == 1 or curProc.priority == 2 do RR scheduling

                //if curBurst > 0

                    //if timeQuantumCounter < curProc's time quantum

                        //decrement curBurst

                        //increment timeQuantumCounter

                    //if timeQuantumCounter == curProc's time quantum and curBurst != 0

                        //decrease curProc's priority queue

                        //curProc's new arrival time = curTime

                        //save curProc's state

                        //curProcess = next process in priority queue

                        //reset timeQuantumCounter

                //if curBurst = 0

                    //reset timeQuantumCounter

                    //add curProc to BLOCKING queue and go out to IO

                    //curProc = next process in priority queue = curProc.priority


                //if curProc.priority == 3 do FCFS scheduling




            //loop through BLOCKING queue

                //decrement I/O time of every process

                //if a process's curIO == 0

                    //increment curIOTimeIndex for that process

                    //process's new arrival time = curTime




    }
}
