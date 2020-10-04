import java.util.Random;
import java.lang.*;

class Task2 {
    Skiplist<Integer> pop1;
    Skiplist<Integer> pop2;
    public Task2(int elements, int MAX_VALUE) {
        pop1 = new Skiplist<>();
        pop2 = new Skiplist<>();
        
        int mean = 5000000;
		int stddev = 1666666;

        for (int i = 0; i < elements; i++) {
            int intToAdd = new Random().nextInt(MAX_VALUE);
			pop1.add(intToAdd);

			double gaussToAdd = new Random().nextGaussian() * stddev + mean; 
            pop2.add((int)gaussToAdd);
        }
    }
    
    public Skiplist<Integer> getList1() {
        return pop1;
    }

    public Skiplist<Integer> getList2() {
        return pop2;
    }
}

class Task3 {
	class Task3T extends Thread {
        int num_oper;
        double[] frac;
        Skiplist<Integer> list;
        int pop;
        int MAX_VALUE;

		public Task3T(int num_oper, double[] frac, int MAX_VALUE, Skiplist<Integer> list, int pop) {
            this.num_oper = num_oper;
            this.frac = frac;
            this.list = list;
            this.pop = pop;
            this.MAX_VALUE = MAX_VALUE;
		}

		@Override
		public void run() {
			for (int i = 0; i < num_oper; i++) {
                int opToDo = new Random().nextInt(100);
                int random_element;
                if (pop == 1) {
                    random_element = new Random().nextInt(MAX_VALUE);
                } else {
                    int mean = 5000000;
                    int stddev = 1666666;
                    double gaussToAdd = new Random().nextGaussian() * stddev + mean;
                    random_element = (int) gaussToAdd;
                }

				if (opToDo < (int) (frac[0] * 100)) {           //ADD    
                    list.add(random_element);
				} else if (opToDo < (int) (frac[1] * 100)) {    //REMOVE
                    list.remove(random_element);
				} else {//opToDo > frac[2]*100                  //CONTAINS
                    list.contains(random_element);
				}
            }
		}
	}

	int NUM_THREADS;
    int operations = 1000000;
    long duration;

	public Task3(int threads, double[] fractions, int MAX_VALUE, Skiplist<Integer> list, int pop) {
		NUM_THREADS = threads;
        int num_op = operations / NUM_THREADS; 
        Task3T[] t3_threads = new Task3T[NUM_THREADS];
        
        long start = System.currentTimeMillis();
        for (int i = 0; i < NUM_THREADS; i++) {
            t3_threads[i] = new Task3T(num_op, fractions, MAX_VALUE, list, pop);
            t3_threads[i].start();
        }

        try {
            for (Task3T t3_thread : t3_threads) {
                t3_thread.join();
            }
        } catch (InterruptedException e) {}

        long end = System.currentTimeMillis();

        duration = end-start;

    }
    
    public long getDuration() {
        return duration;
    }
}

/*  ---------------------------------------
*	
*   Main - running all the task from 2 - 11
*	
*   ----------------------------------------  */
public class Task {
	public static void main(String[] args) {
        // ----------------------------------------------------------------------------------------- //
        // --------------------------------------- TASK 2 ------------------------------------------ //		
        // ----------------------------------------------------------------------------------------- //
		// Call task 2 for generating two different random populations.
		// Population1: pop1 = size 10^7 elements with random values between 0 and 10^7
        // Population2: pop2 = size 10^7 elements with normaldist, mean 5 000 000. and stnd 1 666 666.
        System.out.println("Number of available processors: " + Runtime.getRuntime().availableProcessors());
        int elements = 10000000;
        int MAX_VALUE = 100000000;
        Task2 t2 = new Task2(elements, MAX_VALUE);
        Skiplist<Integer> pop1 = t2.getList1();
		Skiplist<Integer> pop2 = t2.getList2();

        System.out.println("Populated both skiplists..");
        // ----------------------------------------------------------------------------------------- //
        // --------------------------------------- TASK 3 ------------------------------------------ //		
        // ----------------------------------------------------------------------------------------- //
        // Call task 3 for addning, removing and checking if contains in our skiplists.
		// The proportions of operations deepends on the fractions. i.e. [10% add, 10% remove, 80% contains] etc....
		// The number of threads can be changed by just changing the number of thread variable.
        //
        // Example of using:
        // Calling task3 with both lists, different number of threads, and fracts. Further, plotting the result for evaluation.
        //
        // Task 3 Arguments:
        //                  int threads, 
        //                  double[] fractions, 
        //                  Skiplist<Integer> list, 
        //                  int listNumber

        double[] fractions = {0.1, 0.1, 0.8}; //Add, Remove, Contains
		int numberOfThreads = 10;

        Task3 t3_l1 = new Task3(numberOfThreads, fractions, MAX_VALUE, pop1, 1);
        long dur1 = t3_l1.getDuration();

        System.out.println("Completed task 3 for population 1");
        
        Task3 t3_l2 = new Task3(numberOfThreads, fractions, MAX_VALUE, pop2, 2);
        long dur2 = t3_l2.getDuration();

        System.out.println("Number of threads: " + numberOfThreads);
        System.out.println("Fractions: ");
        for (double fraction : fractions) {
            System.out.print(fraction + " ");
        }
        System.out.println("\nDuration for list 1 (uniform): " + dur1 + " ms\nDuration for list 2 (gaussian): " + dur2 + " ms");

        // ----------------------------------------------------------------------------------------- //
        // --------------------------------------- TASK 4 ------------------------------------------ //		
		// ----------------------------------------------------------------------------------------- //
		// 
        // We believe the skiplist implementation is linearisable. We now wish to test this hypothesis. 
        // We want to use System.nanoTime() to record the time at which the linearisation point was encountered. 
        // To do this, identify the linearisation points and sample the time by inserting a call to 
        // System.nanoTime() as close to the linearisation point as you can.

		// returns the current value of the system timer, in nanoseconds
        // System.out.println("time in nanoseconds = " + System.nanoTime());
    

    }
}
