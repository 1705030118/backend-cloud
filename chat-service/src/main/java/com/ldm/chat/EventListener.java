package com.ldm.chat;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class EventListener {

    @Autowired
    /**
     * 客户端连接
     * @param client
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        System.out.println("建立连接");

    }
    /**
     * 客户端断开
     * @param client
     */
    @OnDisconnect
    public void onDisConnect(SocketIOClient client) {
        System.out.println("关闭连接");
    }

}