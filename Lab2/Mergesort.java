package Lab2;

public class Mergesort {

    public static void main(String[] args) {
        int size = 1000000;
        String type = "ES"; //Possible values: SEQ, ES, LAM, RA

        /* Creating the unsorted list */
        Integer[] list = createList(size, false);
        int low = 0;
        int high = list.length-1;


        /* Sort array according to type */
        long start = System.currentTimeMillis();

        if (type == "SEQ") {
            sort(list, low, high);
        } else if (type == "ES") {
            Mergesort_ES mses = new Mergesort_ES(list, low, high);
            try {
                mses.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mses.ES.shutdown();
        } else if (type == "LAM") {

        } else if (type == "RA") {

        }
        long end = System.currentTimeMillis();
        long time_taken = end-start;

        /* Print total time taken */
        System.out.println("TOTAL TIME: " + time_taken + "ms");



    }

    public static void merge(Integer[] array, int low, int mid, int high) {
        int leftArray[] = new int[mid - low + 1];
        int rightArray[] = new int[high - mid];

        for (int i = 0; i < leftArray.length; i++)
            leftArray[i] = array[low + i];
        for (int i = 0; i < rightArray.length; i++)
            rightArray[i] = array[mid + i + 1];

        int leftIndex = 0;
        int rightIndex = 0;

        for (int i = low; i < high + 1; i++) {
            if (leftIndex < leftArray.length && rightIndex < rightArray.length) {
                if (leftArray[leftIndex] < rightArray[rightIndex]) {
                    array[i] = leftArray[leftIndex];
                    leftIndex++;
                } else {
                    array[i] = rightArray[rightIndex];
                    rightIndex++;
                }
            } else if (leftIndex < leftArray.length) {
                array[i] = leftArray[leftIndex];
                leftIndex++;
            } else if (rightIndex < rightArray.length) {
                array[i] = rightArray[rightIndex];
                rightIndex++;
            }
        }
    }

    public static void sort(Integer[] array, int low, int high) {
        if (high <= low) return;

        int mid = (low+high)/2;
        sort(array, low, mid);
        sort(array, mid+1, high);
        merge(array, low, mid, high);
    }

    public synchronized static void printArray(Integer[] arr, long ThreadID) {
        int n = arr.length;
        System.out.print("Thread #" + ThreadID + ": [");
        for (int i = 0; i < n; i++) {
            if (i == n-1) {
                System.out.print(arr[i]);
            } else {
                System.out.print(arr[i] + ", ");
            }
        }
        System.out.println("]");
    }

    public static Integer[] createList(int size, boolean random) {
        Integer[] list = new Integer[size];
        if (random) {
            for (int i = 0; i < size; i++) {
                list[i] = (int)(0 + Math.random() * ((size)+1));
            }
        } else {
            for (int i = 0; i < size; i++) {
                list[i] = size-i;
            }
        }

        return list;
    }
}