/* Corresponds to answers for tasks 1 through 3 */

public class Work13 {
    public static void main(String[] args) {
        //task1();
        /*
        try {
            task2();
        } catch (InterruptedException ie) {
            System.out.println("ie");
        }
        */
        //task3();
    }

    public static void task1() {
        int threads = 5;
        for(int i = 0; i < threads; i++) {
            Actor a = new Actor("Hello world");
            Thread thread = new Thread(a);
            thread.start();
        }
    }

    public static void task2() throws InterruptedException {
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Threads available: " + cores);

        int total = 1000000;
        int numberOfThreads = 8;
        int loops = total/numberOfThreads;
        My_number num = new My_number();
        Thread[] threads = new Thread[numberOfThreads];
        for(int i = 0; i < numberOfThreads; i++) {
            ActorCount ac = new ActorCount(loops, num);
            threads[i] = new Thread(ac);
            threads[i].start();
        }

        for(int i = 0; i < numberOfThreads; i++) {
            threads[i].join();
        }

        System.out.println("Variable count: " + num.number);

    }

    public static void task3() {
        int total = 1000000;
        My_number num = new My_number();

        ActorCount3 ac = new ActorCount3(total, num);
        ActorCPrint acp = new ActorCPrint(ac, num);
        Thread thread = new Thread(acp);
        thread.start();
    }

}