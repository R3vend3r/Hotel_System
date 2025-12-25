package task3;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class task3 {
    private static final int BUFFER_SIZE = 5;
    private static final Queue<Integer> buffer = new LinkedList<>();
    private static final Object lock = new Object();

    public static void main(String[] args) {
        // Поток-производитель
        Thread producer = new Thread(() -> {
            Random random = new Random();
            while (true) {
                synchronized (lock) {
                    while (buffer.size() == BUFFER_SIZE) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    int num = random.nextInt(100);
                    buffer.add(num);
                    System.out.println("Производитель добавил: " + num);
                    lock.notifyAll();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Поток-потребитель
        Thread consumer = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    while (buffer.isEmpty()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    int num = buffer.poll();
                    System.out.println("Потребитель взял: " + num);
                    lock.notifyAll();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        producer.start();
        consumer.start();
    }
}