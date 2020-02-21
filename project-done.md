## 3. Done
### 3.1 netty-socketio实现私聊和群聊
> netty-socketio是一个开源的、基于Netty的、Java版的即时消息推送项目
#### 3.1.2 netty组件
- Channel
> 一个 Channel 在它的生命周期内只能注册与一个 EventLoop
- EventLoop
> 用于处理连接的生命周期中所发生的事件,一个 EventLoop 在它的生命周期内只能与一个Thread绑定。
- BossEventLoopGroup和WorkerEventLoopGroup
> 通常是一个单线程的 EventLoop，EventLoop 维护着一个注册了 ServerSocketChannel 的 Selector 实例,所以通常可以将 BossEventLoopGroup 的线程数参数为 1。

BossEventLoop 只负责处理连接，故开销非常小，连接到来，马上按照策略将 SocketChannel 转发给 WorkerEventLoopGroup，WorkerEventLoopGroup 会由 next 选择其中一个 EventLoop 来将这 个SocketChannel 注册到其维护的 Selector 并对其后续的 IO 事件进行处理。
- ChannelFuture-异步通知
> netty中所有的I/O操作都是异步的

Netty 为异步非阻塞，即所有的 I/O 操作都为异步的，因此，我们不能立刻得知消息是否已经被处理了。Netty 提供了 ChannelFuture 接口，通过该接口的 addListener() 方法注册一个 ChannelFutureListener，当操作执行成功或者失败时，监听就会自动触发返回结果。
- ChannelHandler

ChannelHandler 为 Netty 中最核心的组件，它充当了所有处理入站和出站数据的应用程序逻辑的容器。ChannelHandler 主要用来处理各种事件，这里的事件很广泛，比如可以是连接、数据接收、异常、数据转换等。
##### 3.1.2.1 总结
1、NioEventLoopGroup 实际上就是个线程池，一个 EventLoopGroup 包含一个或者多个 EventLoop  
2、一个 EventLoop 在它的生命周期内只和一个 Thread 绑定  
3、所有有 EnventLoop 处理的 I/O 事件都将在它专有的 Thread 上被处理  
4、一个 Channel 在它的生命周期内只注册于一个 EventLoop  
5、每一个 EventLoop 负责处理一个或多个 Channel
### 3.2 使用Sharding-JDBC实现MySQL读写分离
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
### 3.3 搭建Redis Cluster集群
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
### 3.4 使用Spring的AOP实现日志管理
#### 3.4.1 原理
为了能够更好地将系统级别的代码抽离出来，去掉与对象的耦合，就产生了面向AOP（面向切面）
#### 3.4.2 实现
> 对Controller层的请求进行打日志
- 静态代理

通过代理类我们实现了将日志代码集成到了目标类，但从上面我们可以看出它具有很大的局限性：需要固定的类编写接口（或许还可以接受，毕竟有提倡面向接口编程），需要实现接口的每一个函数（不可接受），同样会造成代码的大量重复，将会使代码更加混乱。
- 动态代理

它的好处是可以为我们生成任何一个接口的代理类，并将需要增强的方法织入到任意目标函数。但它仍然具有一个局限性，就是只有实现了接口的类，才能为其实现代理。
- CGLB

它的好处理时可以为我们生成任何一个接口的代理类，并将需要增强的方法织入到任意目标函数。但它仍然具有一个局限性，就是只有实现了接口的类，才能为其实现代理。
当然CGLIB也具有局限性，对于无法生成子类的类（final类），肯定是没有办法生成代理子类的。
### 3.5 Redis的zset实现简单限流
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
### 3.6 Redis实现session共享
> uni-app 中不支持读写 cookie，所以不能如传统的应用那样通过读取 cookie 来判断是否是登录状态。Redis存储token(使用UUID生成)
```
header: {  
    "Content-Type": "application/x-www-form-urlencoded",  
    "Token":res.data.token  
}
```
### 3.7 Spring Task定时任务
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
### 3.8 Elasticsearch实现搜索，日志收集
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
### 3.9 Nginx负载均衡、limit_req限制用户请求速率
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
### 3.10 SQL优化
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
### 3.11 Linux命令
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