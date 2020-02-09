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
limit_req_zone $binary_remote_addr zone=ttlsa_com:10m rate=1r/s;
server {
    location /www.ttlsa.com/ {
        limit_req zone=ttlsa_com burst=5;
    }
}
```
