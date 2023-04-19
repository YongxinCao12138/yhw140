package lcr;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Here, every message transfer between processes is asynchronous.
 * It can be imagined that a process is a dead M data structure, with N threads operating on these M data structures,
 * Due to congestion in the network, the delay in packet arrival is caused.
 * The thread execution order and sequence here exactly simulate the packets in the network.
 * So there's no need to do too careful synchronization processing, just add synchronization blocks to the methods of manipulating data.
 *
 * Due to each operation being performed on a different thread, the program runs very fast!
 *
 */
public class LCR {

    public static void main(String[] args) {
        // processor num
        int num_processor = 10;
        startRandom(num_processor);
    }

    /**
     * random data simulate
     * @return one of threads
     */
    public static Process startRandom(int num_processor) {
        // init thread, generate random uid
        List<Process> processes = new LinkedList<>();
        for (int i = 1; i <= num_processor; i++) {
            processes.add(new Process(i));
        }

        //build a circle
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

        // print circle
        System.out.print("struct: ");
        for (int i=1; i<=num_processor; i++){
            System.out.print(p.getMyId()+" -> ");
            p=p.getNextProcess();
        }
        System.out.println(p.getMyId());

        // start simulation
        for (int i = 0; i < num_processor-1; i++) {
            p.start();
            p = p.getNextProcess();
        }

        return p;
    }


}
