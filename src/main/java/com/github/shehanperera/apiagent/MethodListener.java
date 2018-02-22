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
import org.ballerinalang.bre.Context;
import org.wso2.carbon.messaging.CarbonMessage;
import org.wso2.carbon.metrics.core.Timer;

/**
 * Instrument done method in BalConnectorCallback
 */
public class MethodListener {

    /**
     * Instrumentation of done method in BalConnectorCallback after it exit.
     * This used for stop timer and find out backend response time and we can identify status of backend
     *
     * @param contextTimer      newly defined Timer Context to stop
     * @param isClientConnector newly defined boolean for check ClientConnector
     * @param carbonMessage     CarbonMessage parameter from done method
     */
    @Advice.OnMethodEnter
    public static void enterDoneMethod(@Advice.Argument(0) CarbonMessage carbonMessage,
                                          @Advice.FieldValue(value = "contextTimer") Timer.Context contextTimer,
                                          @Advice.FieldValue(value = "isClientConnector") boolean isClientConnector,
                                          @Advice.FieldValue(value = "context") Context context) {
        MetricServer metricServer = MetricServer.getInstance();
        if (isClientConnector) {
            if (carbonMessage.isFaulty()) {
                metricServer.getBackendErrorRate().mark();
            }
            contextTimer.stop();
        }
    }
}



