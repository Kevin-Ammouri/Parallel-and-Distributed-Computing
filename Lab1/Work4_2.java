import java.util.concurrent.Semaphore;


class Actor_Semaphore implements Runnable {
    Numbers obj;
    public int countTo;

    public Actor_Semaphore(Numbers obj, int countTo) {
        this.obj = obj;
        this.countTo = countTo;
    }

    public void run() {
        for(int i = 0; i < countTo; i++) {
            try {
                obj.semaphores.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            obj.N++;
            obj.semaphores.release();
        }
    }

}

class Numbers {
    public final Semaphore semaphores = new Semaphore(1, true);

    public int N;

    public Numbers() {
        N = 0;
    }
}

public class Work4_2 {

    public static void main(String[] args) {
        Numbers numbers = new Numbers();

        int numberOfThreads = 8;
        int countTo = 10000/numberOfThreads;

        Thread[] threads = new Thread[numberOfThreads];
        for(int i = 0; i < numberOfThreads; i++) {
            Actor_Semaphore as = new Actor_Semaphore(numbers, countTo);
            threads[i] = new Thread(as);
            threads[i].start();
        }

        for(int i = 0; i < numberOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(numbers.N);
    }
}
