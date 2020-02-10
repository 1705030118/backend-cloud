# backend-cloud
基于Spring Cloud的APP 后端
## 项目难点（如何解决）
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
### MySQL读写分离
### 搭建Redis Cluster集群
### HashMap做内存标记
### 使用Spring的AOP实现日志管理
### Redis的zset实现简单限流
### Elasticsearch实现搜索，日志收集
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
## TODO
### Linux命令
- 查找文件
find / -name nginx.conf
- 查看文件内容
- cat nginx.conf（一次性显示整个文件内容）
- 查看文件内容（倒序）
- tail -n 2 nginx.conf（查看文件的最后2行内容）
- 查看文件内容
less nginx.conf（搜索功能）
- 查看文件内容
more -3 nginx.conf（一次显示3行）
