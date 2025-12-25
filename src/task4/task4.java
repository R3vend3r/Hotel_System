package task4;

import java.time.LocalTime;

public class task4 extends Thread {
    private final int intervalSec;

    public task4(int intervalSec) {
        this.intervalSec = intervalSec;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Текущее время: " + LocalTime.now());
            try {
                Thread.sleep(intervalSec * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Поток прерван");
                break;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        task4 timer = new task4(1);
        timer.start();

        Thread.sleep(10000);
        timer.interrupt();
    }
}