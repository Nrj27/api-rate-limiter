package com.example.ratelimiter;

public class TokenBucket {

    private double tokens;
    private long lastRefillTime;

    public TokenBucket() {
        this.tokens = Constants.BUCKET_CAPACITY;
        this.lastRefillTime = System.nanoTime();
    }

    public synchronized boolean tryConsume() {
        refill();
        if (tokens >= 1) {
            tokens--;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.nanoTime();
        double elapsedSeconds = (now - lastRefillTime) / 1_000_000_000.0;
        double newTokens = elapsedSeconds * Constants.REFILL_RATE;
        tokens = Math.min(Constants.BUCKET_CAPACITY, tokens + newTokens);
        lastRefillTime = now;
    }

    public synchronized double getTokens() {
        refill();
        return tokens;
    }
}