# High Performance API Rate Limiter

A thread-safe, in-memory API Rate Limiter implemented in Java 21 using the Token Bucket algorithm.

## Overview

Modern applications often experience burst traffic caused by real-time search, polling, retries, and concurrent user activity. To protect backend services from excessive load while maintaining a smooth user experience, this project implements a Token Bucket Rate Limiter.

Each API key is assigned an independent token bucket that controls how many requests can be processed within a given period. The implementation allows short bursts of traffic while enforcing a sustainable request rate over time.

## Features

* Token Bucket rate-limiting algorithm
* Maximum bucket capacity of 100 tokens
* Token regeneration at 2 tokens per second
* Thread-safe concurrent request handling
* Independent bucket per API key
* O(1) request processing time
* Pure Java implementation with no external dependencies
* Built using Java 21 and Maven

## How It Works

Each API key owns its own token bucket.

When a request arrives:

1. Retrieve the bucket associated with the API key.
2. Refill tokens based on the elapsed time since the last request.
3. Cap the bucket at the maximum capacity of 100 tokens.
4. Consume one token if available.
5. Allow the request if a token was consumed; otherwise reject it.

### Example

A new API key starts with 100 tokens.

```java
isRequestAllowed("user-api-key");
```

Result:

```text
true
```

The bucket now contains 99 tokens.

If 100 requests are made immediately, the bucket becomes empty.

The next request returns:

```text
false
```

As time passes, tokens regenerate at a rate of 2 tokens per second and requests can be accepted again.

## Design Decisions

### Why Token Bucket?

The Token Bucket algorithm allows temporary bursts of traffic while maintaining a controlled long-term request rate.

Compared to a fixed-window approach, it provides a smoother user experience and prevents sudden request spikes from overwhelming backend services.

### Why System.nanoTime()?

`System.nanoTime()` provides more precise time measurements than `System.currentTimeMillis()`.

Since token regeneration depends on elapsed time, higher precision improves accuracy when calculating how many tokens should be added back to the bucket.

### Why ConcurrentHashMap?

`ConcurrentHashMap` provides thread-safe access to buckets without requiring global synchronization.

The `computeIfAbsent()` method ensures that only one bucket is created for each API key, even when multiple threads access it simultaneously.

### Why Bucket-Level Synchronization?

Synchronization is performed on individual buckets instead of the entire rate limiter.

This allows requests for different API keys to execute concurrently, improving throughput and reducing contention.

For example:

* Requests for User A synchronize only with User A's bucket.
* Requests for User B can continue independently.

### Thread Safety

The implementation guarantees that:

* Multiple threads cannot consume the same token simultaneously.
* Token counts remain consistent under concurrent access.
* Bucket creation is race-condition free.
* Rate limits cannot be exceeded due to concurrent requests.

## Project Structure

```text
api-rate-limiter/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/example/ratelimiter/
│   │           ├── ApiRateLimiter.java
│   │           ├── TokenBucket.java
│   │           └── Constants.java
│   │
│   └── test/
│       └── java/
│           └── com/example/ratelimiter/
│               └── ApiRateLimiterTest.java
│
├── pom.xml
├── README.md
└── .gitignore
```

## Complexity Analysis

| Operation          | Complexity   |
| ------------------ | ------------ |
| Bucket Lookup      | O(1) Average |
| Token Refill       | O(1)         |
| Request Validation | O(1)         |

### Space Complexity

O(N)

Where N is the number of unique API keys stored in memory.

## Performance Characteristics

* Supports bursts of up to 100 immediate requests per API key.
* Automatically throttles traffic once the bucket is exhausted.
* Regenerates tokens at a steady rate of 2 tokens per second.
* Minimizes lock contention by synchronizing only per bucket.
* Suitable for high-concurrency environments.

## Prerequisites

* Java 21
* Maven 3.8+

## Build

```bash
mvn clean compile
```

## Run Tests

```bash
mvn test
```

## Future Enhancements

* Automatic cleanup of inactive buckets to reduce memory consumption.
* Configurable bucket capacity and refill rates.
* Metrics and monitoring integration.
* Distributed rate limiting using Redis.
* Support for Sliding Window and Leaky Bucket algorithms.
* Per-user and per-endpoint rate limiting policies.
* Virtual Thread optimizations using Java 21.

## Conclusion

This project demonstrates a scalable, thread-safe, and efficient implementation of the Token Bucket rate-limiting algorithm using Java 21. By combining precise token regeneration, per-key isolation, and bucket-level synchronization, the solution provides reliable protection against excessive traffic while maintaining high throughput and a smooth user experience.
