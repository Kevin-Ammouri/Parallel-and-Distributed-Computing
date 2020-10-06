import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Task2 {
    Skiplist<Integer> pop1;
    Skiplist<Integer> pop2;

    public Task2(int elements, int MAX_VALUE, int mean, int stddev) {
        pop1 = new Skiplist<>();
        pop2 = new Skiplist<>();

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
        int mean;
        int stddev;

		public Task3T(int num_oper, double[] frac, int MAX_VALUE, int[] gauss, Skiplist<Integer> list, int pop) {
            this.num_oper = num_oper;
            this.frac = frac;
            this.list = list;
            this.pop = pop;
            this.MAX_VALUE = MAX_VALUE;
            mean = gauss[0];
            stddev = gauss[1];
		}

		@Override
		public void run() {
			for (int i = 0; i < num_oper; i++) {
                int opToDo = new Random().nextInt(100);
                int random_element;
                if (pop == 1) {
                    random_element = new Random().nextInt(MAX_VALUE);
                } else {
                    double gaussToAdd = new Random().nextGaussian() * stddev + mean;
                    random_element = (int) gaussToAdd;
                }

				if (opToDo < (int) (frac[0] * 100)) {          //ADD    
                    list.add(random_element);
				} else if (opToDo < ((int) (frac[1] * 100 + frac[0]*100))) {   //REMOVE
                    list.remove(random_element);
				} else {                                      //CONTAINS
                    list.contains(random_element);
				}
            }
		}
	}

	int NUM_THREADS;
    int operations = 10000;
    long duration;

	public Task3(int threads, double[] fractions, int MAX_VALUE, int[] gauss, Skiplist<Integer> list, int pop) {
		NUM_THREADS = threads;
        int num_op = operations / NUM_THREADS; 
        Task3T[] t3_threads = new Task3T[NUM_THREADS];
        
        long start = System.currentTimeMillis();
        for (int i = 0; i < NUM_THREADS; i++) {
            t3_threads[i] = new Task3T(num_op, fractions, MAX_VALUE, gauss, list, pop);
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

class Task10 {
	Skiplist<Integer> list;
	boolean producerAreWorking;
    ArrayList<LogContent<Integer>> logList;
    
    int num_oper;
    double[] frac;
    int pop;
    int MAX_VALUE;
    int mean;
    int stddev;
    long duration;

    class Producer extends Thread {
        public Producer() {}
        @Override
		public void run() {
            for (int i = 0; i < num_oper; i++) {
                int opToDo = new Random().nextInt(100);
                int random_element;
                if (pop == 1) {
                    random_element = new Random().nextInt(MAX_VALUE);
                } else {
                    double gaussToAdd = new Random().nextGaussian() * stddev + mean;
                    random_element = (int) gaussToAdd;
                }

				if (opToDo < (int) (frac[0] * 100)) {                           //ADD    
                    list.add(random_element);
				} else if (opToDo < ((int) (frac[1] * 100 + frac[0]*100))) {   //REMOVE
                    list.remove(random_element);
				} else {                                                        //CONTAINS
                    list.contains(random_element);
				}
            }
		}
    }

    class Consumer extends Thread {
        public Consumer() {}
        @Override
        public void run() {
            while (producerAreWorking && !list.queue.isEmpty()) {
                if (list.queue.peek() != null) {
                    LogContent<Integer> item = list.queue.remove();
                    logList.add(item);
                }
            }
        }
    }

    public Task10(Skiplist<Integer> list, int pop, int operations, int threads, double[] fractions, 
                                    int MAX_VALUE, int[] gauss, ArrayList<LogContent<Integer>> logList) {
        this.list = list;
        this.logList = logList;
		this.pop = pop;  
		num_oper = operations / (threads-1);
        frac = fractions;
		this.MAX_VALUE = MAX_VALUE;
        mean = gauss[0];
        stddev = gauss[1];
        

		Producer[] producers = new Producer[threads - 1];
		Consumer consumer = new Consumer();
        
        long start = System.currentTimeMillis();
        producerAreWorking = true;
		for (int i = 0; i < threads-1; i++) {
            producers[i] = new Producer();
            producers[i].start();
        }
        consumer.start();
        try {
            for (Producer producer : producers) {
                producer.join();
            }
        } catch (InterruptedException e) { e.printStackTrace(); }

        producerAreWorking = false;
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
        int elements = 100000;
        int operations = 10000;
        int MAX_VALUE = 100000;
        int mean = 50000;
        int stddev = 16666;
        double[] fractions = {0.5, 0.5, 0.0}; //Add, Remove, Contains
        int numberOfThreads = 2;
        int[] gauss_info = {mean, stddev};

        Task2 t2 = new Task2(elements, MAX_VALUE, mean, stddev);
        Skiplist<Integer> pop1 = t2.getList1();
		Skiplist<Integer> pop2 = t2.getList2();

        //System.out.println("Populated both skiplists..");
        
        

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

        

        Task3 t3_l1 = new Task3(numberOfThreads, fractions, MAX_VALUE, gauss_info, pop1, 1);
        long dur1 = t3_l1.getDuration();

        System.out.println("Completed task 3 for population 1");
        
        Task3 t3_l2 = new Task3(numberOfThreads, fractions, MAX_VALUE, gauss_info, pop2, 2);
        long dur2 = t3_l2.getDuration();

        System.out.println("Number of threads: " + numberOfThreads);
        System.out.println("Fractions: ");
        for (double fraction : fractions) {
            System.out.print(fraction + " ");
        }
        System.out.println("\nDuration for list 1 (uniform): " + dur1 + " ms\nDuration for list 2 (gaussian): " + dur2 + " ms");
        
        // ----------------------------------------------------------------------------------------- //
        // --------------------------------------- TASK 4-9 ------------------------------------------ //		
		// ----------------------------------------------------------------------------------------- //
		// 
        // We believe the skiplist implementation is linearisable. We now wish to test this hypothesis. 
        // We want to use System.nanoTime() to record the time at which the linearisation point was encountered. 
        // To do this, identify the linearisation points and sample the time by inserting a call to 
        // System.nanoTime() as close to the linearisation point as you can.
        /*
        System.out.println("> ----------- !! RESULTS FROM LOGMAP !! ----------- <");
        
        HashMap<Long, LogContent<Integer>> logMap = pop2.getLogMap();
        ArrayList<LogContent<Integer>> logList = new ArrayList<LogContent<Integer>>();
        
        for (LogContent<Integer> lm : logMap.values()) {
            logList.add(lm);
        }
        Collections.sort(logList, new SortByTimeStamp());

        int wrong = 0;
        int saved_value = Integer.MIN_VALUE;
        LogContent<Integer> saved_obj = null;

        for (LogContent<Integer> lm : logList) {
            if (saved_value == lm.value) {
                System.out.println(saved_obj.toString());
                System.out.println(lm.toString());
                long timediff = lm.timestamp - saved_obj.timestamp;
                System.out.println("Time difference: " + timediff);
                if (saved_obj.call.equals("remove") && lm.call.equals("contains")) {
                    wrong++;
                }
                if (saved_obj.call.equals("contains") && lm.call.equals("add")) {
                    wrong++;
                }
            } else {
                saved_value = lm.value;
                saved_obj = lm;
            }
        }

        System.out.println("Number of inconsistent operations (remove->contains) or (contains->add): " + wrong);
        */
        // ----------------------------------------------------------------------------------------- //
        // --------------------------------------- TASK 10 ------------------------------------------ //		
        // ----------------------------------------------------------------------------------------- //
        /*
        ArrayList<LogContent<Integer>> logList1 = new ArrayList<LogContent<Integer>>();
        ArrayList<LogContent<Integer>> logList2 = new ArrayList<LogContent<Integer>>();

        Task10 t10_l1 = new Task10(pop1, 1, operations, numberOfThreads, fractions, MAX_VALUE, gauss_info, logList1);
		Task10 t10_l2 = new Task10(pop2, 2, operations, numberOfThreads, fractions, MAX_VALUE, gauss_info, logList2);
        
		long dur1 = t10_l1.getDuration();
		long dur2 = t10_l2.getDuration();

        Collections.sort(logList1, new SortByTimeStamp());
        Collections.sort(logList2, new SortByTimeStamp());

        int wrong1 = 0;
        int saved_value = Integer.MIN_VALUE;
        LogContent<Integer> saved_obj = null;

        for (LogContent<Integer> lm : logList1) {
            if (saved_value == lm.value) {
                System.out.println(saved_obj.toString());
                System.out.println(lm.toString());
                long timediff = lm.timestamp - saved_obj.timestamp;
                System.out.println("Time difference: " + timediff);
                if (saved_obj.call.equals("remove") && lm.call.equals("contains")) {
                    wrong1++;
                }
                if (saved_obj.call.equals("contains") && lm.call.equals("add")) {
                    wrong1++;
                }
            } else {
                saved_value = lm.value;
                saved_obj = lm;
            }
        }

        int wrong2 = 0;
        saved_value = Integer.MIN_VALUE;
        saved_obj = null;

        for (LogContent<Integer> lm : logList2) {
            if (saved_value == lm.value) {
                System.out.println(saved_obj.toString());
                System.out.println(lm.toString());
                long timediff = lm.timestamp - saved_obj.timestamp;
                System.out.println("Time difference: " + timediff);
                if (saved_obj.call.equals("remove") && lm.call.equals("contains")) {
                    wrong2++;
                }
                if (saved_obj.call.equals("contains") && lm.call.equals("add")) {
                    wrong2++;
                }
            } else {
                saved_value = lm.value;
                saved_obj = lm;
            }
        }
        System.out.println("Number of inconsistent operations (remove->contains) or (contains->add) [LIST1]: " + wrong1);
        System.out.println("Number of inconsistent operations (remove->contains) or (contains->add) [LIST2]: " + wrong2);

        System.out.println("\nDuration for list 1 (uniform): " + dur1 + " ms\nDuration for list 2 (gaussian): " + dur2 + " ms");
        */
    }
}

class SortByTimeStamp implements Comparator<LogContent<Integer>> { 
    @Override
    public int compare(LogContent<Integer> o1, LogContent<Integer> o2) {
        if (o1.timestamp > o2.timestamp) {
            return 1;
        } else if (o1.timestamp < o2.timestamp) {
            return -1;
        } else {
            return 0;
        }
    }
}