/*
 * Copyright 2018 Shehan Perera
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.shehanperera.apiagent;

import io.netty.channel.ChannelHandlerContext;
import net.bytebuddy.asm.Advice;
import org.wso2.carbon.metrics.core.Timer;

/**
 * Instrumentation of channelInactive method in ChannelInboundHandlerAdapter on netty-server
 * This can use for get API calls , Response time
 */

public class ChannelInactiveAdvice {

    /**
     * This used for get response,request size and api errors
     *
     * @param contextTimer newly defined Timer context in ChannelInboundHandlerAdapter Class
     * @param ctx          ChannelHandlerContext parameter of the channelInactive method
     */
    @Advice.OnMethodEnter
    static void enterMethod(@Advice.FieldValue(value = "contextTimer") Timer.Context contextTimer,
                            @Advice.Argument(0) ChannelHandlerContext ctx) throws Exception {

        if (ctx.name().equals("idleStateHandler")) {
            contextTimer.stop();
        }
    }
}
