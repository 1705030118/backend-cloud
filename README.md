# backend-cloud
基于Spring Cloud的APP后端
## 项目难点（如何解决）
### 安全性
#### redis安全
在redis部署到阿里云服务器上，redis监听的6379端口对外开放，然后第二天就收到了阿里的警告短信，6379端口被强制关闭，redis数据库被清空
- 解决方法

在redis的配置文件中指定监听的IP地址，并且增加redis的密码访问限制
### Elasticsearch分词效果
### 线程池参数设置
### 数据一致性
#### 读写分离 主从库一致性问题
##### 串行化问题
这里有一个非常重要的一点，就是从库同步主库数据的过程是串行化的，也就是说主库上并行的操作，在从库上会串行执行。所以这就是一个非常重要的点了，由于从库从主库拷贝日志以及串行执行SQL的特点，在高并发场景下，从库的数据一定会比主库慢一些，是有延时的。所以经常出现，刚写入主库的数据可能是读不到的，要过几十毫秒，甚至几百毫秒才能读取到。
- 解决方法-并行复制

所谓并行复制，指的是从库开启多个线程，并行读取relay log中不同库的日志，然后并行重放不同库的日志，这是库级别的并行。
##### 数据丢失问题
如果主库突然宕机，然后恰好数据还没同步到从库，那么有些数据可能在从库上是没有的，有些数据可能就丢失了。
- 解决方法-半同步复制

这个所谓半同步复制，semi-sync复制，指的就是主库写入binlog日志之后，就会将强制此时立即将数据同步到从库，从库将日志写入自己本地的relay log之后，接着会返回一个ack给主库，主库接收到至少一个从库的ack之后才会认为写操作完成了。

#### Redis+MySQL 读写一致性问题

如果不能正确的处理 redis 和 mysql 的读写逻辑很容易出现脏读，从而导致造成一些不必要的风险，如在插入数据时，先写redis在写DB，如果失败会回滚，此时redis的数据是无效的数据从而造成脏读
- 读操作

在读取数据时，我们遵守先从redis中读取数据，如果redis不存在，再到DB中去读数据。
- 插入操作

在向数据库中插入数据时，我们只对数据库进行操作，不对redis进行数据cache。这样就避免了脏数据的cache，而这条记录当进行读操作时会cache进来。
- 更新操作

在更新操作上面，先对DB进行更新操作，然后设置删除记录的redis缓存。
遵循以上的三点能够很好的保证 Redis 和 Mysql 的数据读写一致性问题。
## 项目亮点
### 线程池优化
> 日志收集
```java
@Configuration
@EnableAsync
public class ExecutorConfig {
    private static Logger logger = LogManager.getLogger(ExecutorConfig.class.getName());

    @Bean
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(5);
        //配置最大线程数
        executor.setMaxPoolSize(10);
        //配置队列大小
        executor.setQueueCapacity(400);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-service-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
```
### @Scheduled(fixedRate)保证固定速度执行
为了保证fixedRate任务真的可以按照设置的速度执行，无疑需要引入异步执行模式，确保schedule调度的任务不会被***单线程***执行阻塞。
这里引入注解@EnableAsync和@Async。
- 遇到的问题

@Async使用的是SimpleAsyncTaskExecutor，每次请求新开线程，没有最大线程数设置.不是真的线程池，这个类不重用线程，每次调用都会创建一个新的线程
- 解决方法

使用自定义线程池，设置好对应的参数
## Done
### netty-socketio实现私聊和群聊
### 使用Sharding-JDBC实现MySQL读写分离
> 面对日益增加的系统访问量，数据库的吞吐量面临着巨大瓶颈。 对于同一时间有大量并发读操作和较少写操作类型的应用系统来说，将单一的数据库拆分为主库和从库，主库负责处理事务性的增删改操作，从库负责处理查询操作，能够有效的避免由数据更新导致的行锁，使得整个系统的查询性能得到极大的改善。 通过一主多从的配置方式，可以将查询请求均匀的分散到多个数据副本，能够进一步的提升系统的处理能力。 使用多主多从的方式，不但能够提升系统的吞吐量，还能够提升系统的可用性，可以达到在任何一个数据库宕机，甚至磁盘物理损坏的情况下仍然不影响系统的正常运行。
虽然读写分离可以提升系统的吞吐量和可用性，但同时也带来了数据不一致的问题，这包括多个主库之间的数据一致性，以及主库与从库之间的数据一致性的问题。并且，读写分离也带来了与数据分片同样的问题，它同样会使得应用开发和运维人员对数据库的操作和运维变得更加复杂。透明化读写分离所带来的影响，让使用方尽量像使用一个数据库一样使用主从数据库，是读写分离中间件的主要功能。
- 原理
> 读写分离，简单来说，就是将DML交给主数据库去执行，将更新结果同步至各个从数据库保持主从数据一致，DQL分发给从数据库去查询，从数据库只提供读取查询操作。读写分离特别适用于读多写少的场景下，通过分散读写到不同的数据库实例上来提高性能，缓解单机数据库的压力。

首先，主从复制涉及到三个线程，分别是binlog线程、I/O线程、SQL线程   
1、在主库上把数据更改记录到二进制日志（binary log）中  
2、备库将主库上的二进制日志复制到自己的中继日志（relay log）中    
3、备库读取中继日志中的事件，将其重放到备库数据库上
- 实现
```
spring.shardingsphere.datasource.names=master,slave

# 主数据源
spring.shardingsphere.datasource.master.type=com.alibaba.druid.pool.DruidDataSource
spring.shardingsphere.datasource.master.driver-class-name=com.mysql.cj.jdbc.Driver
spring.shardingsphere.datasource.master.url=jdbc:mysql://192.168.164.134:3306/test?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=GMT&useSSL=false
spring.shardingsphere.datasource.master.username=root
spring.shardingsphere.datasource.master.password=123456

# 从数据源
spring.shardingsphere.datasource.slave.type=com.alibaba.druid.pool.DruidDataSource
spring.shardingsphere.datasource.slave.driver-class-name=com.mysql.cj.jdbc.Driver
spring.shardingsphere.datasource.slave.url=jdbc:mysql://192.168.164.129:3306/test?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=GMT&useSSL=false
spring.shardingsphere.datasource.slave.username=root
spring.shardingsphere.datasource.slave.password=123456

# 读写分离配置
spring.shardingsphere.masterslave.load-balance-algorithm-type=round_robin
spring.shardingsphere.masterslave.name=dataSource
spring.shardingsphere.masterslave.master-data-source-name=master
spring.shardingsphere.masterslave.slave-data-source-names=slave

# 显示SQL
spring.shardingsphere.props.sql.show=true
spring.main.allow-bean-definition-overriding=true
```
```
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
    <version>4.0.0-RC1</version>
</dependency>
        
```
- 遇到的问题

Sharding-JDBC目前仅支持一主多从的结构  
Sharding-JDBC没有提供主从同步的实现，该功能需要自己额外搭建  
主库和从库的数据同步延迟导致的数据不一致问题需要自己去解决
### 搭建Redis Cluster集群
- 原理

Redis可以使用主从同步，从从同步。第一次同步时，主节点做一次bgsave，并同时将后续修改操作记录到内存buffer，待完成后将rdb文件全量同步到复制节点，复制节点接受完成后将rdb镜像加载到内存。加载完成后，再通知主节点将期间修改的操作记录同步到复制节点进行重放就完成了同步过程。
- 槽位定位算法

Redis Cluster将所有数据划分为16384个slots，每个节点负责其中一部分槽位。Cluster默认会对key值使用crc16算法进行hash得到一个整数值，然后用这个证书值对16384进行取模来得到具体槽位。

- 容错

Redis Cluster可以为每个主节点设置若干个从节点，单主节点故障时，集群会自动将其中某个从节点提升为主节点。如果某个主节点没有从节点，那么当它发生故障时，集群将完全处于不可用状态。不过Redis可以设置成允许部分节点故障。
- 可能下线与确定下线

Redis集群节点采用 Gossip协议来广播自己的状态以及自己对整个集群认知的改变。比
如一个节点发现某个节点失联了 (PFail)，它会将这条信息向整个集群广播，其它节点也就可
以收到这点失联信息。如果一个节点收到了某个节点失联的数量 (PFailCount)已经达到了集
群的大多数，就可以标记该节点为确定下线状态 (Fail)，然后向整个集群广播，强迫其它节
点也接收该节点已经下线的事实，并立即对该失联节点进行主从切换。
- 实现

修改redis.conf配置文件，安装Ruby环境，使用redis-trib.rb创建集群
```
#端口7000,7001,7002
port 7000

#默认ip为127.0.0.1，需要改为其他节点机器可访问的ip，否则创建集群时无法访问对应的端口，无法创建集群
bind 192.168.252.101

#redis后台运行
daemonize yes

#pidfile文件对应7000，7001，7002
pidfile /var/run/redis_7000.pid

#开启集群，把注释#去掉
cluster-enabled yes

#集群的配置，配置文件首次启动自动生成 7000，7001，7002          
cluster-config-file nodes_7000.conf

#请求超时，默认15秒，可自行设置 
cluster-node-timeout 10100    
        
#aof日志开启，有需要就开启，它会每次写操作都记录一条日志
appendonly yes
```
### 使用Spring的AOP实现日志管理
- 原理


对Controller层的请求进行打日志
### Redis的zset实现简单限流
> 时间复杂度： O(log(N)+M)， N 为有序集的基数，而 M 为被移除成员的数量。
  移除有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
```java
public class CacheServiceImpl implements CacheService {
    /**
     * 用户操作频率限制,如点赞、发布活动、评论
     * @param userId
     * @return
     */
    public boolean limitFrequency(int userId){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long nowTs=System.currentTimeMillis();
            int period=60,maxCount=5;
            String key="frequency:limit:"+userId;
            jedis.zadd(key,nowTs,""+nowTs);
            jedis.zremrangeByScore(key,0,nowTs-period*1000);
            return jedis.zcard(key)>maxCount;
        } finally {
            returnToPool(jedis);
        }
    }
}
```
### Redis实现session共享
> uni-app 中不支持读写 cookie，所以不能如传统的应用那样通过读取 cookie 来判断是否是登录状态。Redis存储token(使用UUID生成)
```
header: {  
    "Content-Type": "application/x-www-form-urlencoded",  
    "Token":res.data.token  
}
```
### Spring Task定时任务
```java
/**
 * 基于注解的定时器
 */
@Component
public class AdvanceNotice {
    /**
     * 启动时执行一次，之后每隔一个小时秒执行一次
     * 向相关用户发送短信
     */
    @Scheduled(fixedRate = 1000*3600) //从上一个任务开始到下一个任务开始的间隔，单位毫秒
    public void send() {
        System.out.println("print method 2");
    }
}
/**
* 在程序入口启动类添加@EnableScheduling，开启定时任务功能
*/
@SpringBootApplication
@EnableScheduling
@EnableEurekaClient
public class CacheServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheServiceApplication.class, args);
    }

}
```
### Elasticsearch实现搜索，日志收集
- 分词器的配置
```$xslt
{
    "analyzer": "standard",
    "text": "哪吒之魔童降世"
}
哪 吒 之 魔 童 降 世
```
```$xslt
{
    "analyzer": "ik_max_word",
    "text": "哪吒之魔童降世"
}
哪吒 之 魔 童 降世
```
### Nginx负载均衡、limit_req限制用户请求速率
> Nginx使用limit_req_zone对同一IP访问进行限流
```
http {
    // 轮询
	upstream server1 {    
        server  192.168.164.129:9999;
        server  192.168.164.134:9999;
       }
    // 区域名称为one（自定义），占用空间大小为10m，平均处理的请求频率不能超过每秒一次。
    limit_req_zone $binary_remote_addr zone=one:10m rate=1r/s;
    server {
        location /search/ {
            limit_req zone=one burst=5;// 缓冲队列的长度为5
        }
    }
}
```
### SQL优化
```
EXPLAIN SELECT COUNT(*) FROM t_activity
```
type：index
rows：119375
key：index_user_id
```$xslt
EXPLAIN SELECT COUNT(*) FROM t_activity WHERE publish_time>'2019-09-18 06:26:00'
```
type：range
rows：59687
key：index_publish_time
time：0.064
### Linux命令
- 查找文件

find / -name nginx.conf	
- 查看文件内容	
cat nginx.conf（一次性显示整个文件内容）

- 查看文件内容（倒序）

tail -n 2 nginx.conf（查看文件的最后2行内容）
- 查看文件内容

less nginx.conf（搜索功能）
- 查看文件内容

more -3 nginx.conf（一次显示3行）
- 查看占用端口的进程

netstat -anp | grep 3306 | awk '{print $7}'
- 查看当前路径

pwd
- 生成一个空文件

touch test.conf
- mkdir test

创建一个目录
- vim常用命令

查看并替换：%s/oldword/newword/g

查找： /word

删除多行：9,10d(按u撤销)

批量注释：3,4s#^#//#g