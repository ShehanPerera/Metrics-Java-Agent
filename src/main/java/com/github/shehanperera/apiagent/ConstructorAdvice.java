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
import org.wso2.carbon.metrics.core.Timer;

/**
 * Instrument BalConnectorCallback constructor
 */
public class ConstructorAdvice {

    /**
     * Instrument BalConnectorCallback constructor after it run we can start timer from here
     * and get request size
     *
     * @param contextBallerina  ballerina context form constructor
     * @param contextTimer      newly defined Timer Context
     * @param isClientConnector newly defined boolean to set if  ClientConnector
     *                          <p>
     *                          ContextTimer used for get running time of backend and isClientConnector is used to
     *                          identify whether request set to the backend or not.
     */
    @Advice.OnMethodExit
    public static void exitFromConstructor(@Advice.Argument(0) Context contextBallerina,
                                           @Advice.FieldValue(value = "contextTimer", readOnly = false)
                                                   Timer.Context contextTimer,
                                           @Advice.FieldValue(value = "isClientConnector", readOnly = false)
                                                   boolean isClientConnector) {

        try {
            if ("ClientConnector".equals(contextBallerina.actionInfo.getConnectorInfo().getName())) {
                MetricServer metricServer = MetricServer.getInstance();
                metricServer.getBackendRequestsSize().update(contextBallerina.getCarbonMessage().getFullMessageLength());
                contextTimer = metricServer.getBackendResponsesTime().start();
                isClientConnector = true;
            }
        } catch (Throwable e) {
            isClientConnector = false;
        }
    }
}
