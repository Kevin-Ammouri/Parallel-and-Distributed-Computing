package Lab2;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Mergesort_ES implements Callable<Integer[]> {
    public final int START_SEQ = 50000;
    public static ExecutorService ES = Executors.newCachedThreadPool();
    public Integer arr[];
    public int low;
    public int high;

    public Mergesort_ES(Integer[] arr, int low, int high) {
        this.arr = arr;
        this.low = low;
        this.high = high;
    }

    public Integer[] call() throws Exception {
        int size = high-low;

        if (size < 1) {
            return arr;
        }

        int mid = (high+low)/2;

        if (size < START_SEQ) {
            Mergesort.sort(arr, low, high);
            return arr;
        }


        Future<Integer[]> firstExec = ES.submit(new Mergesort_ES(arr, low, mid));
        Future<Integer[]> secondExec = ES.submit(new Mergesort_ES(arr, mid+1, high));

        Integer[] firstSort = firstExec.get();
        Integer[] secondSort = secondExec.get();

        Mergesort.merge(arr, low, mid, high);

        return arr;
    }
}
