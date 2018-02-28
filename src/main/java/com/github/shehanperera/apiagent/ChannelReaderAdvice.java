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

import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import net.bytebuddy.asm.Advice;

/**
 * Instrumentation of channelRead method in ChannelInboundHandlerAdapter on netty-server
 * This can use for get Response size, Request size and API error rate
 */
public class ChannelReaderAdvice {

    /**
     * This used for get response,request size and api errors
     *
     * @param defaultHttpType newly defined boolean for set defaultHttpType
     * @param parameters      parameters of the channelRead method
     */
    @Advice.OnMethodExit
    static void exitMethod(@Advice.FieldValue(value = "defaultHttpType", readOnly = false) String defaultHttpType,
                           @Advice.AllArguments Object[] parameters) throws Exception {

        MetricServer metricServer = MetricServer.getInstance();
        // TODO need to find out way for check boolean of isIdleStateHandler without using metrics server
        boolean isIdleStateHandler = metricServer.getIdleStateHandler();
        try {
            if (parameters[1].getClass().getSimpleName().equals("DefaultHttpRequest")) {
                defaultHttpType = "request";
            }
            if (parameters[1].getClass().getSimpleName().equals("DefaultHttpResponse")) {
                DefaultHttpResponse defaultHttpResponse = (DefaultHttpResponse) parameters[1];
                defaultHttpType = "response";
                System.out.println(defaultHttpResponse);
                if (500 > defaultHttpResponse.status().code() && defaultHttpResponse.status().code() >= 400 && isIdleStateHandler) {
                    metricServer.getApi4xxErrorRate().mark();
                } else if (defaultHttpResponse.status().code() >= 500) {
                    metricServer.getApi5xxErrorRate().mark();
                }
            }
            if (defaultHttpType.equals("request")) {
                DefaultLastHttpContent request = (DefaultLastHttpContent) parameters[1];
                metricServer.getApiRequestsSize().update(request.content().readableBytes());
            }
            if (defaultHttpType.equals("response")) {
                DefaultLastHttpContent response = (DefaultLastHttpContent) parameters[1];
                //TODO need to check is first non needed response size is equals to 166(M13)
                //TODO in new release (M19) first non needed response size is 173 and we have to remove this condition
                int apiResponseSize = response.content().readableBytes();
                if (apiResponseSize != 173 && isIdleStateHandler) {
                    metricServer.getApiResponseSize().update(apiResponseSize);
                }
            }
        } catch (Exception e) {
            //Ignore
        }

    }
}
