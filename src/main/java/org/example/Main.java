package org.example;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Main {
    public static final int CARS_COUNT = 4;

    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier startCyclicBarrier = new CyclicBarrier(CARS_COUNT);
        Semaphore tunnelSemaphore = new Semaphore((int) (CARS_COUNT / 2));
        CountDownLatch finishCountDownLatch = new CountDownLatch(CARS_COUNT);

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
            cars[i].setCyclicBarrier(startCyclicBarrier);
            cars[i].setSemaphore(tunnelSemaphore);
            cars[i].setCountDownLatch(finishCountDownLatch);
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        finishCountDownLatch.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
