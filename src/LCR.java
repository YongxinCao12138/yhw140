import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 这里进程间每次传递消息都是异步的，可以这么想象：进程就是死的M个数据结构，N个线程在操作这M个数据结构，
 * 由于网络中会出现拥塞而导致包到达的延迟，这里的线程执行顺序和先后顺序，刚好模拟了网络中的包，
 * 所以不用做太仔细的同步处理，仅仅在操作数据的方法上加上同步块即可。
 *
 *由于每次操作都是在不同线程上进行，程序运行速度很快！
 *
 */
public class LCR {

    public static void main(String[] args) {
        /**
         * 集群个数
         */
        int num_processor = 10;
        startRandom(num_processor);
    }

    /**
     * 随机数据，定量测试
     */
    public static Process startRandom(int num_processor) {
        /**
         * 初始化进程，uid随机生成
         */
        List<Process> processes = new LinkedList<>();
        for (int i = 0; i < num_processor; i++) {
            processes.add(new Process(i));
        }

        /**
         * 随机构成环状
         */
        Random random = new Random();
        Process head = processes.remove(num_processor - 1);
        Process p = head;
        Process q = null;
        for (int i = num_processor - 1; i > 0; i--) {
            q = processes.remove(random.nextInt(i));
            p.addNextProcessor(q);
            p = q;
        }
        q.addNextProcessor(head);

        /**
         * 打印环状结构
         */
        System.out.print("struct: ");
        for (int i=0;i<num_processor;i++){
            System.out.print(p.getMyId()+" -> ");
            p=p.getNextProcess();
        }
        System.out.println(p.getMyId());

        /**
         * 开启线程模拟分布式环境下的LCR选举
         */
        for (int i = 0; i < num_processor-1; i++) {
            p.start();
            p = p.getNextProcess();
        }

        return p;
    }


}
