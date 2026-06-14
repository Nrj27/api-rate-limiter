package com.example.ratelimiter;


import java.util.concurrent.ConcurrentHashMap;

public class ApiRateLimiter {

    private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    public boolean isRequestAllowed(String apiKey) {
        TokenBucket bucket = buckets.computeIfAbsent(apiKey, key -> new TokenBucket());
        return bucket.tryConsume();
    }
}