### a gateway framework for learning

#### 1.function list
-[ ] server: Netty
-[ ] client:
    1. -[ ] netty client
    2. -[ ] OKHttpClient
-[ ] register center
    1. -[ ] Zookeeper
    2. -[ ] Nacos
-[ ] filters: request response global
-[ ] loadbalancer: Random RoundRobin WeightRoundRobin Hash
-[ ] route


```shell

### 网关
mokernetdeMac-mini:fgateway mokernet$ wrk -t 10 -c 10 -d 10 http://localhost:8887/api/user/35
Running 10s test @ http://localhost:8887/api/user/35
  10 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.20ms   10.29ms 244.42ms   99.01%
    Req/Sec   687.53    250.00     1.03k    76.19%
  7215 requests in 10.07s, 0.89MB read
Requests/sec:    716.46
Transfer/sec:     90.96KB

### 直连
mokernetdeMac-mini:fgateway mokernet$ wrk -t 10 -c 10 -d 10 http://localhost:9000/api/user/35
Running 10s test @ http://localhost:9000/api/user/35
  10 threads and 10 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     6.37ms   17.23ms 158.20ms   91.82%
    Req/Sec     1.71k   792.23     4.43k    67.75%
  169056 requests in 10.03s, 24.05MB read
Requests/sec:  16853.46
Transfer/sec:      2.40MB

```