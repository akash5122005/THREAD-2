import java.util.LinkedList;
import java.util.Queue;

class BoundedBuffer {
    private final Queue<Integer>buffer = new LinkedList<>();
    private final int maxSize;

    public BoundedBuffer(int size) {
        this.maxSize = size;
    }
    public synchronized void produce(int item) throws InterruptedException{
        while(buffer.size()== maxSize){
            wait();
        }
        buffer.add(item);
        System.out.println("Producer produced: "+item);
        notifyAll();
    }
    public synchronized int consume() throws InterruptedException{
        while(buffer.isEmpty()){
            wait();
        }
        int item = buffer.poll();
        System.out.println("Consumer consumed: " + item);
        notifyAll();
        return item;
    }
}
class Producer extends Thread{
    private final BoundedBuffer buffer;
    public Producer(BoundedBuffer buffer){
        this.buffer = buffer;
    }
    public void run(){
        for(int i = 1; i <= 10; i++){
            try
            {
                buffer.produce(i);
                Thread.sleep(500);
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}
class Consumer extends Thread{
    private final BoundedBuffer buffer;
    public Consumer(BoundedBuffer buffer){
        this.buffer = buffer;
    }
    public void run(){
        for(int i = 1; i <=10; i++){
            try
            {
                buffer.consume();
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}
public class Main{
    public static void main(String[] args) {
        BoundedBuffer buffer = new BoundedBuffer(5);
        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);
        producer.start();
        consumer.start();
        try{
            producer.join();
            consumer.join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
