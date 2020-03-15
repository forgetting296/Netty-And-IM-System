package com.shusaku.client.protobuilder;

import com.shusaku.client.imclient.ClientSession;
import com.shusaku.common.bean.msg.ProtoMsg;

public class BaseBuilder {

    protected ProtoMsg.HeadType type;
    private long seqId;
    private ClientSession session;

    public BaseBuilder(ProtoMsg.HeadType type, ClientSession session) {
        this.type = type;
        this.session = session;
    }

    /**
     * 构建消息 基础部分
     */
    public ProtoMsg.Message buildCommand(long seqId) {
        this.seqId = seqId;
        ProtoMsg.Message message = ProtoMsg.Message.newBuilder()
                .setSessionId(session.getSessionId())
                .setType(type)
                .setSequence(seqId)
                .build();
        return message;
    }
}
