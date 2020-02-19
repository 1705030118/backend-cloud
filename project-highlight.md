## 2 项目亮点
### 2.1 线程池优化
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
### 2.2 @Scheduled(fixedRate)保证固定速度执行
为了保证fixedRate任务真的可以按照设置的速度执行，无疑需要引入异步执行模式，确保schedule调度的任务不会被***单线程***执行阻塞。
这里引入注解@EnableAsync和@Async。
- 遇到的问题

@Async使用的是SimpleAsyncTaskExecutor，每次请求新开线程，没有最大线程数设置.不是真的线程池，这个类不重用线程，每次调用都会创建一个新的线程
- 解决方法

使用自定义线程池，设置好对应的参数
### 2.3 微服务架构
