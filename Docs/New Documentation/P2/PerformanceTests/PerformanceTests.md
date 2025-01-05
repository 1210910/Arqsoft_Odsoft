# Performance Testing Results: Monolith vs Microservices

This repository contains the performance test results of two architectures: **Monolith** and **Microservices**. The tests were conducted using **Apache JMeter** to measure response times, throughput, error rates, and resource utilization.

## Test Overview

### Objectives
The purpose of these tests was to:
1. Compare the <span style="color:lightblue">**throughput**</span> and <span style="color:lightblue">**latency**</span> of the monolith and microservices.
2. Evaluate the <span style="color:lightblue">**error rates**</span> and <span style="color:lightblue">**stability**</span> of each architecture.
3. Assess resource utilization in terms of <span style="color:lightblue">**bandwidth usage**</span>.

### Test Scenarios
1. <span style="color:lightblue">**Set Reader Auth Token**</span>: Simulates a thread setting an authentication token for a reader.
2. <span style="color:lightblue">**GET Books by ISBN**</span>: Simulates retrieving book details by ISBN.
3. <span style="color:lightblue">**Overall Performance**</span>: Aggregated results across all scenarios.

---

## Metrics and Results

### Monolith
| Metric                     | Value                     |
|----------------------------|---------------------------|
| <span style="color:lightblue">**Total Throughput**</span>       | 504.15 requests/sec       |
| <span style="color:lightblue">**Error Rate**</span>             | 0.789%                   |
| <span style="color:lightblue">**Average Response Time**</span>  | 1 ms                     |
| <span style="color:lightblue">**99th Percentile Latency**</span>| 5 ms                     |
| <span style="color:lightblue">**Received KB/sec**</span>        | 382.15                   |
| <span style="color:lightblue">**Sent KB/sec**</span>            | 242.75                   |

### Microservices
| Metric                     | Value                     |
|----------------------------|---------------------------|
| <span style="color:lightblue">**Total Throughput**</span>       | 2468.95 requests/sec      |
| <span style="color:lightblue">**Error Rate**</span>             | 0.000%                   |
| <span style="color:lightblue">**Average Response Time**</span>  | 1 ms                     |
| <span style="color:lightblue">**99th Percentile Latency**</span>| 10 ms                    |
| <span style="color:lightblue">**Received KB/sec**</span>        | 1891.15                  |
| <span style="color:lightblue">**Sent KB/sec**</span>            | 1204.34                  |

---

## Observations

### 1. <span style="color:lightblue">**Throughput**</span>
The microservices architecture demonstrates a **~5x increase in throughput**, making it more scalable under high load.

### 2. <span style="color:lightblue">**Error Rates**</span>
- The monolithic architecture had an **error rate of 1.578%** for the "GET Books by ISBN" operation, while the microservices architecture had **zero errors** across all scenarios.

### 3. <span style="color:lightblue">**Response Times**</span>
- Average response times are similar for both architectures, but the **99th percentile latency** is higher in the microservices setup (10 ms vs. 5 ms). This indicates occasional delays, likely caused by network or inter-service communication overhead.

### 4. <span style="color:lightblue">**Resource Utilization**</span>
- The microservices architecture requires higher bandwidth due to increased throughput, with **~4.9x more bandwidth usage** than the monolith.

---

## Conclusions

1. <span style="color:darkgreen">**Scalability**</span>:  
   Microservices significantly outperform the monolith in throughput and error rates, making them more suitable for handling large-scale traffic.

2. <span style="color:darkgreen">**Stability**</span>:  
   The microservices architecture exhibits no errors during the test, indicating superior reliability.

3. <span style="color:darkgreen">**Latency**</span>:  
   While average latencies are similar, the microservices' higher 95th and 99th percentile response times suggest opportunities for optimization.

4. <span style="color:darkgreen">**Bandwidth**</span>:  
   The microservices architecture requires greater bandwidth, which should be accounted for in infrastructure planning.

---

## Recommendations

1. <span style="color:lightblue">**Optimize Microservices Latency**</span>:
    - Investigate network and inter-service communication bottlenecks.

2. <span style="color:lightblue">**Monitor Bandwidth Usage**</span>:
    - Ensure sufficient network capacity to handle the increased resource demands of the microservices architecture.

3. <span style="color:lightblue">**Leverage Microservices Scalability**</span>:
    - Deploy the microservices architecture to production environments where high throughput and low error rates are critical.

