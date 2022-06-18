package com.surya.miniproject.pool;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    // class contains the thread pool for this project
    public static ExecutorService executorService = Executors.newFixedThreadPool(8);
}
