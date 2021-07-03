### a gateway framework for learning

#### 1.function list
- [x] server: Netty
- [x] client:
    1. -[x] netty client
    2. -[x] OKHttpClient
- [ ] register center
    1. -[ ] Zookeeper
    2. -[ ] Nacos
- [ ] filters: 
    request 
    response 
    global 
    内置的： 熔断限流 权限认证等
- [ ] loadbalancer: Random RoundRobin WeightRoundRobin Hash
- [ ] route 
    1. 代理的路径存储
    2. 通过断言条件配置： 不同路径走不同的filter
- [ ] SPI扩展
- [ ] 注解方式使用
    代理的后端服务可以通过注解，部分请求走网关，也可以全部走网关
 
- [ ] 页面配置过滤器及断言条件


### 2，spring boot starter

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