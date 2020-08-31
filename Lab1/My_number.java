public class My_number {
    public int number;

    public My_number() {
        number = 0;
    }

    public void increment(int ThreadID) {
        //System.out.println(ThreadID);
        number++;
    }

    public void print(int ThreadID) {
        System.out.println("Variable count from thread: " + number);
    }
}
