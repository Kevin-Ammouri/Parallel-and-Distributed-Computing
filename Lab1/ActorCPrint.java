/* Related to Task 3 */

public class ActorCPrint implements Runnable {
    private int loop;
    public My_number number;
    private ActorCount3 ac3;

    public ActorCPrint(ActorCount3 ac3, My_number number) {
        this.number = number;
        this.ac3 = ac3;
    }

    public void run() {
        Thread t = Thread.currentThread();
        Thread a = new Thread(ac3);
        a.start();
        try {
            synchronized (a) {
                System.out.println("Waiting for count to finish..");
                a.wait();
                number.print((int)t.getId());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
