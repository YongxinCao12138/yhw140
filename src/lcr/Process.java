package lcr;

public class Process extends Thread {
    // myid = uid
    private int myId;

    private volatile int sendId;

    private int leaderId;

     //true = leader
     //false = unknown

    private volatile boolean status;


    public Process getNextProcess() {
        return nextProcess;
    }

    public int getMyId() {
        return myId;
    }

    public void addNextProcessor(Process process){
        nextProcess = process;
    }

    // get leaderId util lead is found
    public synchronized int getLeaderId() {
        while (leaderId == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("server simulation failed");
            }
        }
        return leaderId;
    }

    public synchronized void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
        notify();
    }

    public Process(int uid) {
        this.myId = uid;
        this.sendId = uid;
        this.status = false;
    }

    /**
     * linked thread
     */
    private Process nextProcess;



    /**
     * 传递选举信息（进程uid）
     *
     * @param inId
     */
     synchronized public void receive(int inId){
        // pass down
         if (inId > myId){
            sendId = inId;
            //nextProcess.receive(sendId);
           new Thread(() -> nextProcess.receive(sendId)).start();
            show(inId,"continue");
        }else if (inId == myId ){
            //找到leader
            /**
            *  Note: There is a multithreading issue here, which can also simulate network communication issues
            *  (large sendIDs covering small sendIDs ultimately have multiple findLeader information propagating in the cluster)
            *   Assuming there are 6 threads, of which two uids are successfully passed (successful passing represents not being dropped by done!)
            *   If the uid of thread A is greater than that of thread B, the delivery speed of thread A is faster (within the CPU time slice execution time, it is delivered to the data stack of thread B)
            *   When thread A passes through thread B and modifies its sendID, there will be two identical sendIDs on the ring, resulting in a double success signal
            *   So use the following code to block duplicate received packets
            */
            if (status){
                show(inId,"repeated packet");
                return;
            }
            status=true;
            setLeaderId(myId);
            //nextProcess.updateLeader(leaderId);
            new Thread(() -> nextProcess.updateLeader(leaderId)).start();
            System.out.println(myId+": success ! I am th leader .");
        }else {
            show(inId,"done");
        }

    }

    /**
     * update leaderId
     */
    synchronized public void updateLeader(int leaderId){
        if (leaderId != myId ){
            status = true;
            setLeaderId(leaderId);
            //nextProcess.updateLeader(leaderId);
            new Thread(() -> nextProcess.updateLeader(leaderId)).start();
            System.out.println(myId+": success ! leader is the : "+ leaderId);
        }

    }

    /**
     * to print some information about this processor
     * @param inId
     * @param info
     */
    private void show(int inId,String info){
        System.out.println("uid : "+ myId  + " | inId : "+ inId + " | sendId " +sendId+ " | nexProcessor : "+ nextProcess.getMyId() + " | info :"+info);
    }


    @Override
    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nextProcess.receive(sendId);
    }
}
