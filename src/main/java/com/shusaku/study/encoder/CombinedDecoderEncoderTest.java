package com.shusaku.study.encoder;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * @program: Java8Test
 * @description: 继承CombinedChannelDuplexHandler这个类　泛型中传入Decoder和Encoder　直接放到ｐｉｐｅｌｉｎｅ中就可以
 * @author: Shusaku
 * @create: 2020-03-11 15:27
 */
public class CombinedDecoderEncoderTest extends CombinedChannelDuplexHandler<InBoundTest,Integer2Byte> {

    public CombinedDecoderEncoderTest() {
        super(new InBoundTest(),new Integer2Byte());
    }

}
