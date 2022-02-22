package org.example;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Car implements Runnable {
    private static int CARS_COUNT;

    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;
    private CountDownLatch finishCountDownLatch;
    private Semaphore tunnelSemaphore;
    private CyclicBarrier startCyclicBarrier;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        finishCountDownLatch = countDownLatch;
    }

    public void setCyclicBarrier(CyclicBarrier cyclicBarrier) {
        startCyclicBarrier = cyclicBarrier;
    }

    public void setSemaphore(Semaphore semaphore) {
        tunnelSemaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            startCyclicBarrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            Stage nextStage = race.getStages().get(i);
            if (nextStage instanceof Tunnel) {
                try {
                    tunnelSemaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                nextStage.go(this);
                tunnelSemaphore.release();
            } else
                nextStage.go(this);
        }
        finishCountDownLatch.countDown();
    }
}
