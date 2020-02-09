package com.ldm.chat;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PushServer implements InitializingBean {
    @Resource
    private EventListener eventListener;
    @Override
    public void afterPropertiesSet() throws Exception {
        Configuration config = new Configuration();
        config.setPort(9092);

        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);
        config.setSocketConfig(socketConfig);
        config.setHostname("127.0.0.1");

        SocketIOServer server= new SocketIOServer(config);
        server.addListeners(eventListener);
        server.start();
        System.out.println("启动正常");

    }
}
