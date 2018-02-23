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

import net.bytebuddy.asm.Advice;
import org.wso2.carbon.messaging.CarbonMessage;

/**
 * Instrumentation of done method in DefaultBalCallback class
 *
 */

public class BackendResponseAdvice {

    /**
     * This used for get backend response size
     *
     * @param carbonMessage CarbonMessage parameter of done method
     *                      this can be use to get response size
     */
    @Advice.OnMethodEnter
    static void enter(@Advice.Argument(0) CarbonMessage carbonMessage) throws Exception {

        MetricServer metricServer = MetricServer.getInstance();
        if (!carbonMessage.isEmpty()) {
            metricServer.getBackendResponseSize().update(carbonMessage.getFullMessageLength());
        }
    }
}
