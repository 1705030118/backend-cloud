# backend-cloud
基于Spring Cloud的APP 后端
## 项目难点（如何解决）
## 项目亮点
### 线程池优化
## Done
### MySQL读写分离
### 搭建Redis Cluster集群
### HashMap做内存标记
### 使用Spring的AOP实现日志管理
### Redis的zset
### Elasticsearch做
## TODO
## Linux命令
- 查找文件
find / -name nginx.conf
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
