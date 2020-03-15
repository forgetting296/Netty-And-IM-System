package com.shusaku.client.sender;

import com.shusaku.client.imclient.ClientSession;
import com.shusaku.common.bean.User;
import com.shusaku.common.bean.msg.ProtoMsg;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public abstract class BaseSender {

    private Logger logger = LoggerFactory.getLogger(BaseSender.class);

    private User user;
    private ClientSession session;

    public boolean isConnected() {
        if(session == null) {
            logger.info("session is null");
            return false;
        }
        return session.isConnection();
    }

    public boolean isLogin() {
        if(session == null) {
            logger.info("session is null");
            return false;
        }
        return session.isLogin();
    }

    public void sendMessage(ProtoMsg.Message message) {

    }

    protected void sendSucced(ProtoMsg.Message message) {
        logger.info("发送成功");
    }

    protected void sendFailed(ProtoMsg.Message message) {
        logger.info("发送失败");
    }

}
