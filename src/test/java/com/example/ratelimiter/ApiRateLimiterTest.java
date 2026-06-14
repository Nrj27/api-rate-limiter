package com.example.ratelimiter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ApiRateLimiterTest {

    @Test
    void firstRequestShouldPass() {

        ApiRateLimiter limiter = new ApiRateLimiter();

        Assertions.assertTrue(limiter.isRequestAllowed("user-1"));
    }

    @Test
    void bucketShouldRejectWhenEmpty() {

        ApiRateLimiter limiter = new ApiRateLimiter();

        for (int i = 0; i < 100; i++) {
            Assertions.assertTrue(limiter.isRequestAllowed("user-1"));
        }

        Assertions.assertFalse(limiter.isRequestAllowed("user-1"));
    }

    @Test
    void differentUsersShouldHaveDifferentBuckets() {

        ApiRateLimiter limiter = new ApiRateLimiter();

        Assertions.assertTrue(limiter.isRequestAllowed("user-1"));

        Assertions.assertTrue(limiter.isRequestAllowed("user-2"));
    }
}