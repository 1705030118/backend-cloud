## 1 项目难点（如何解决）
### 1.1 安全性
#### 1.1.1 redis安全
在redis部署到阿里云服务器上，redis监听的6379端口对外开放，然后第二天就收到了阿里的警告短信，6379端口被强制关闭，redis数据库被清空
- 解决方法

在redis的配置文件中指定监听的IP地址，并且增加redis的密码访问限制
### 1.2 Elasticsearch分词效果
### 1.3 线程池参数设置
#### 1.3.1 默认值
- corePoolSize=1
- queueCapacity=Integer.MAX_VALUE
- maxPoolSize=Integer.MAX_VALUE
- keepAliveTime=60s
- allowCoreThreadTimeout=false
- rejectedExecutionHandler=AbortPolicy()
#### 1.3.2 如何来设置
- 需要根据几个值来决定
    - tasks ：每秒的任务数，假设为500~1000  
    - taskcost：每个任务花费时间，假设为0.1s  
    - responsetime：系统允许容忍的最大响应时间，假设为1s  
- 做几个计算
    - corePoolSize = 每秒需要多少个线程处理？ 
        - threadcount = tasks/(1/taskcost) =tasks*taskcout =  (500~1000)*0.1 = 50~100 个线程。corePoolSize设置应该大于50
        - 根据8020原则，如果80%的每秒任务数小于800，那么corePoolSize设置为80即可
    - queueCapacity = (coreSizePool/taskcost)*responsetime
        - 计算可得 queueCapacity = 80/0.1*1 = 80。意思是队列里的线程可以等待1s，超过了的需要新开线程来执行
        - 切记不能设置为Integer.MAX_VALUE，这样队列会很大，线程数只会保持在corePoolSize大小，当任务陡增时，不能新开线程来执行，响应时间会随之陡增。
    - maxPoolSize = (max(tasks)- queueCapacity)/(1/taskcost)
        - 计算可得 maxPoolSize = (1000-80)/10 = 92
        - （最大任务数-队列容量）/每个线程每秒处理能力 = 最大线程数
    - rejectedExecutionHandler：根据具体情况来决定，任务不重要可丢弃，任务重要则要利用一些缓冲机制来处理
    - keepAliveTime和allowCoreThreadTimeout采用默认通常能满足

以上都是理想值，实际情况下要根据机器性能来决定。如果在未达到最大线程数的情况机器cpu load已经满了，则需要通过升级硬件（呵呵）和优化代码，降低taskcost来处理。
### 1.4 数据一致性
#### 1.4.1 读写分离 主从库一致性问题
##### 1.4.1.1 串行化问题
这里有一个非常重要的一点，就是从库同步主库数据的过程是串行化的，也就是说主库上并行的操作，在从库上会串行执行。所以这就是一个非常重要的点了，由于从库从主库拷贝日志以及串行执行SQL的特点，在高并发场景下，从库的数据一定会比主库慢一些，是有延时的。所以经常出现，刚写入主库的数据可能是读不到的，要过几十毫秒，甚至几百毫秒才能读取到。
- 解决方法-并行复制

所谓并行复制，指的是从库开启多个线程，并行读取relay log中不同库的日志，然后并行重放不同库的日志，这是库级别的并行。
##### 1.4.1.1 数据丢失问题
如果主库突然宕机，然后恰好数据还没同步到从库，那么有些数据可能在从库上是没有的，有些数据可能就丢失了。
- 解决方法-半同步复制

这个所谓半同步复制，semi-sync复制，指的就是主库写入binlog日志之后，就会将强制此时立即将数据同步到从库，从库将日志写入自己本地的relay log之后，接着会返回一个ack给主库，主库接收到至少一个从库的ack之后才会认为写操作完成了。

#### 1.4.2 Redis+MySQL 读写一致性问题

如果不能正确的处理 redis 和 mysql 的读写逻辑很容易出现脏读，从而导致造成一些不必要的风险，如在插入数据时，先写redis在写DB，如果失败会回滚，此时redis的数据是无效的数据从而造成脏读
- 读操作

在读取数据时，我们遵守先从redis中读取数据，如果redis不存在，再到DB中去读数据。
- 插入操作

在向数据库中插入数据时，我们只对数据库进行操作，不对redis进行数据cache。这样就避免了脏数据的cache，而这条记录当进行读操作时会cache进来。
- 更新操作

在更新操作上面，先对DB进行更新操作，然后设置删除记录的redis缓存。
遵循以上的三点能够很好的保证 Redis 和 Mysql 的数据读写一致性问题。