class ThreadA extends Thread{
    public void run(){
        for (int i = 1; i <= 5; i++){
            System.out.println("Thread A: "+i);
        }
    }
}
class ThreadB extends Thread{
    public void run(){
        for(char letter = 'A'; letter <= 'E'; letter++){
            System.out.println("Thread B: "+letter);
        }
    }
}

public class Main {
    public static void main(String[] args) {

        ThreadA threadA = new ThreadA();
        ThreadB threadB = new ThreadB(); 

        threadA.start();
        try{
            threadA.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        threadB.start();
    }
}
