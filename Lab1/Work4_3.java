import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

/**
 * Dining Philosopher problem, task 4.3
 */

class Chopstick {
    public Semaphore chopstick = new Semaphore(1);

    public Chopstick() {

    }

    public void TakeChopstick() {
        try {
            chopstick.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void ReleaseChopstick() {
        chopstick.release();
    }

    public boolean isAvailable() {
        return chopstick.availablePermits() > 0;
    }
}

class Philosopher implements Runnable {
    public int philosopherID; //id goes from 1-5
    public Chopstick leftChopstick;
    public Chopstick rightChopstick;

    public Philosopher(int ID, Chopstick left, Chopstick right) {
        philosopherID = ID;
        leftChopstick = left;
        rightChopstick = right;
    }

    public void run() {
        while(true) {
            if (philosopherID != 5) {
                leftChopstick.TakeChopstick();
                System.out.println("Philosopher #" + philosopherID + " grabs the left chopstick");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                rightChopstick.TakeChopstick();
                System.out.println("Philosopher #" + philosopherID + " grabs the right chopstick");
            } else {
                rightChopstick.TakeChopstick();
                System.out.println("Philosopher #" + philosopherID + " grabs the right chopstick");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                leftChopstick.TakeChopstick();
                System.out.println("Philosopher #" + philosopherID + " grabs the left chopstick");
            }
            System.out.println("Philosopher #" + philosopherID + " eats");
            rightChopstick.ReleaseChopstick();
            System.out.println("Philosopher #" + philosopherID + " releases the right chopstick");
            leftChopstick.ReleaseChopstick();
            System.out.println("Philosopher #" + philosopherID + " releases the left chopstick");
        }
    }
}

public class Work4_3 {

    public static final int CHOPSTICKS = 5;
    public static final int PHILOSOPHERS = 5;

    public static void main(String[] args) {
        Thread[] PhilThread = new Thread[PHILOSOPHERS];
        Chopstick[] chopsticks = new Chopstick[CHOPSTICKS];

        for(int i = 0; i < CHOPSTICKS; i++) {
            chopsticks[i] = new Chopstick();
        }

        for(int i = 0; i < PHILOSOPHERS; i++) {
            Philosopher ps = new Philosopher(i+1, chopsticks[(i+1) % PHILOSOPHERS], chopsticks[i]);
            PhilThread[i] = new Thread(ps);
            PhilThread[i].start();
        }

        for(int i = 0; i < PHILOSOPHERS; i++) {
            try {
                PhilThread[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
