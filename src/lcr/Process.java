package lcr;

/**
 * 进程类
 *
 *
 */
public class Process extends Thread {
    /**
     *  myid = uid
     */
    private int myId;
    /**
     *
     */
    private volatile int sendId;
    /**
     * leaderId
     */
    private int leaderId;
    /**
     * true = leader
     * false = unknown
     */
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
     * 邻居进程
     */
    private Process nextProcess;



    /**
     * 传递选举信息（进程uid）
     *
     * @param inId
     */
     synchronized public void receive(int inId){
        if (inId > myId){   //向下传递
            sendId = inId;
            //nextProcess.receive(sendId);
           new Thread(() -> nextProcess.receive(sendId)).start();
            show(inId,"continue");
        }else if (inId == myId ){    //找到leader
            /**
             *      * 注意:这里有个多线程问题，也可以时模拟了网络通信会出现的问题（大sendID覆盖小的sendID最终有多个findLeader信息在集群传播）
             *      * 假设有6个线程，其中成功传递的有两个uid（成功传递代表，不被done掉！）
             *      * 线程A的uid大于线程B的uid，这时候若线程A的传递速度较快（CPU时间片执行时间内，传递到了线程B的数据栈上）
             *      * A线程经过B线程时，修改了B线程的sendID，这时候，就会有两个相同的sendID在环上，进而就会有双倍的success信号
             *      * 所以通过以下代码来阻断重复接收到的包
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
     * 传递leader（更新leader进程的uid）
     * @param leaderId
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
