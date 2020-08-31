/* Related to Task 1 */

public class Actor implements Runnable {
    public String s;

    public Actor(String message) {
        s = message;
    }

    public void run() {
        Thread t = Thread.currentThread();
        System.out.println("Im thread nr " + t.getId() + ": " + s);
    }
}