/* Related to Task 2 */

public class ActorCount implements Runnable {
    private int loop;
    public My_number number;

    public ActorCount(int loop, My_number number) {
        this.loop = loop;
        this.number = number;
    }

    public void run() {
        Thread t = Thread.currentThread();
        System.out.println("Thread " + t.getId() + ": Loops = " + loop);
        for(int i = 0; i < loop; i++) {
            number.increment((int)t.getId());
        }
    }
}