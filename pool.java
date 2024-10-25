import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

class ThreadPool{
    private final WorkerThread[] workers;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private volatile int currentTurn = 2;
    private int currentNumber = 1;


    public ThreadPool(int numThreads){
        workers = new WorkerThread[numThreads];
        for(int i=0;i<numThreads;i++){
            workers[i] = new WorkerThread(i+1, this);
            workers[i].start();
        }
    }
    private class WorkerThread extends Thread{
        private final int threadId;
        private final ThreadPool pool;

        public WorkerThread(int threadId, ThreadPool pool){
            this.threadId = threadId;
            this.pool = pool;
        }
        public void run() {
            while (true) {
                pool.printInOrder(threadId);
                if (currentNumber > 5) break;
            }
        }
    }
    public void printInOrder(int threadId){
        lock.lock();
        try{
            while(currentTurn != threadId && currentNumber <= 5){
                condition.await();
            }
            if(currentNumber > 5) return;

            System.out.println("Thread "+threadId+": "+currentNumber);

            if (threadId == 2){
                currentTurn = 1;
            } else  if(threadId == 1){
                currentTurn = 3;
            } else if(threadId == 3){
                currentTurn = 2;
                currentNumber++;
            }
            condition.signalAll();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
    public static void main(String[] args){
        new ThreadPool(3);
    }
}
