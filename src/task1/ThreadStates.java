package task1;

public class ThreadStates {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(300);
                synchronized (ThreadStates.class) {
                    ThreadStates.class.wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Поток был прерван");
            }
        });

        System.out.println(thread.getState());

        thread.start();
        System.out.println(thread.getState());

        try {
            Thread.sleep(110);
            System.out.println(thread.getState());

            synchronized (ThreadStates.class) {
                Thread.sleep(200);
                System.out.println(thread.getState());
            }

            Thread.sleep(100);
            System.out.println(thread.getState());

            synchronized (ThreadStates.class) {
                ThreadStates.class.notify();
            }

            thread.join();
            System.out.println(thread.getState());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Главный поток был прерван");
        }
    }
}