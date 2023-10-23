package com.hcc.task.service.impl;

import lombok.val;


import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.*;

public class ThreadTest {


    public static int test() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 100,
                10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture completableFuture = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                System.out.println(4/0);
            }catch (Exception e){
                e.printStackTrace();
            }

        }, executor);
//        CompletableFuture.allOf(completableFuture).get();
        return 1;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(ThreadTest.test());
    }
}
