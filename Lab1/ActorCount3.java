/* Related to Task 3 */

public class ActorCount3 implements Runnable {
    private int loop;
    public My_number number;

    public ActorCount3(int loop, My_number number) {
        this.loop = loop;
        this.number = number;
    }

    public void run() {
        Thread t = Thread.currentThread();
        synchronized (t) {
            System.out.println("Count begins..");
            for(int i = 0; i < loop; i++) {
                number.increment((int)t.getId());
            }
            t.notify();
        }
    }
}