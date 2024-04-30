package com.artem.umbrella.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RequestCounterService {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static synchronized void increment() {
        counter.incrementAndGet();
    }

    public static synchronized int get() {
        return counter.get();
    }
}
