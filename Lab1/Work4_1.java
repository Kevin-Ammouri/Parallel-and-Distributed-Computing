import static java.lang.Thread.sleep;

class Producer implements Runnable {
    private Number obj;
    private Consumer consumer;

    public Producer(Number obj, Consumer consumer) {
        this.obj = obj;
        this.consumer = consumer;
    }

    public void run() {
        int pos = 0;
        while(pos < obj.size){
            obj.num[pos] = pos*5; //arbitrary number put into the buffer
            synchronized(consumer) {
                consumer.notify();
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            pos++;
        }
    }
}

class Consumer implements Runnable {
    private Number obj;

    public Consumer(Number obj) {
        this.obj = obj;
    }

    public void run() {
        int pos = 0;
        while(pos < obj.size) {
            while (obj.num[pos] == null) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            System.out.println("Printing Integer: " + obj.num[pos]);
            //obj.num[pos] = null;
            pos++;
        }
    }
}

class Number {
    public final int size = 100;
    public Integer[] num;

    public Number() {
        num = new Integer[size];
        for(int i = 0; i < size; i++) {
            num[i] = null;
        }
    }
}

/* This program relates to task 4.1 */
public class Work4_1 {
    public static void main(String[] args) {
        Number numberObject = new Number();

        Consumer consumer = new Consumer(numberObject);
        new Thread(consumer).start();

        Producer producer = new Producer(numberObject, consumer);
        new Thread(producer).start();
    }
}
