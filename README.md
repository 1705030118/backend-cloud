# backend-cloud
基于Spring Cloud的APP后端
## 项目难点（如何解决）
- 安全性
- Elasticsearch分词效果
- 线程池参数设置
## 项目亮点
### 线程池优化
```
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
## Done
### netty-socketio实现私聊和群聊
### MySQL读写分离
### 搭建Redis Cluster集群
### 使用Spring的AOP实现日志管理
### Redis的zset实现简单限流
## 阅读zset源码
### Redis实现session共享
> uni-app 中不支持读写 cookie，所以不能如传统的应用那样通过读取 cookie 来判断是否是登录状态。Redis存储token(使用UUID生成)
`````$xslt
header: {  
    "Content-Type": "application/x-www-form-urlencoded",  
    "Token":res.data.token  
}
`````
### Spring Task定时任务
### Elasticsearch实现搜索，日志收集
## 分词器的配置
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
```
http {
	upstream server1 {    
        server  192.168.164.129:9999;
        server  192.168.164.134:9999;
       }
    limit_req_zone $binary_remote_addr zone=one:10m rate=1r/s;
    server {
        location /search/ {
            limit_req zone=one burst=5;
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