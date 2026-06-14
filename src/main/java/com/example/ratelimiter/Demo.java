package com.example.ratelimiter;

public class Demo {
    public static void main(String[] args) throws InterruptedException {

        ApiRateLimiter limiter = new ApiRateLimiter();

        String user = "user-1";

        // Burst test
        for (int i = 1; i <= 105; i++) {
            boolean allowed = limiter.isRequestAllowed(user);
            System.out.println("Request " + i + " -> " + allowed);
        }
    }
}