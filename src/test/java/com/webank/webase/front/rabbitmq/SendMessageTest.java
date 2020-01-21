/**
 * Copyright 2014-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webank.webase.front.rabbitmq;

import com.webank.webase.front.rabbitmq.entity.BlockPushMessage;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

public class SendMessageTest extends BaseTest {

    @Autowired
    private RabbitMQPublisher rabbitMQPublisher;

    @Test
    public void testMsg() {
        Integer i = 0;
        while( i < 10) {
            i++;
            BlockPushMessage blockPushMessage = new BlockPushMessage();
            blockPushMessage.setBlockNumber(new BigInteger(i.toString()));
            rabbitMQPublisher.sendToTradeFinishedByString(blockPushMessage.toString());
        }
    }
}
