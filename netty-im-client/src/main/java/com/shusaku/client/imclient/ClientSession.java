package com.shusaku.client.imclient;

import com.shusaku.common.bean.User;
import com.shusaku.common.bean.msg.ProtoMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Data
public class ClientSession {

    private static final Logger logger = LoggerFactory.getLogger(ClientSession.class);

    public static final AttributeKey<ClientSession> SESSION_KEY = AttributeKey.valueOf("SESSION_KEY");

    /**
     * 实现用户客户端会话管理的核心
     */
    private Channel channel;
    private User user;

    /**
     * 保存用户登陆之后的服务端sessionId
     */
    private String sessionId;

    /**
     * session中存储的session变量属性值
     */
    private Map<String,Object> map = new HashMap<>();

    private boolean isLogin = false;
    private boolean isConnection = false;

    public ClientSession(Channel channel) {
        this.channel = channel;
        this.sessionId = "-1";
        channel.attr(ClientSession.SESSION_KEY).set(this);
    }

    public boolean isLogin(){
        return this.isLogin;
    }

    public boolean isConnection() {
        return this.isConnection;
    }

    //登录成功之后,设置sessionId
    public static void loginSuccess(
            ChannelHandlerContext ctx, ProtoMsg.Message pkg) {
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        session.sessionId = pkg.getSessionId();
        session.isLogin = true;
        logger.info("登陆成功");
    }

    //获取session
    public static ClientSession getSession(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        return session;
    }

    public String getRemoteAddr() {
        return channel.remoteAddress().toString();
    }

    public ChannelFuture writeAndFlush(Object obj) {
        ChannelFuture future = channel.writeAndFlush(obj);
        return future;
    }

    public void writeAndClose(Object obj) {
        ChannelFuture future = channel.writeAndFlush(obj);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    public void close() {
        isConnection = false;

        ChannelFuture future = channel.close();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()) {
                    logger.info("关闭成功");
                }
            }
        });
    }
}
