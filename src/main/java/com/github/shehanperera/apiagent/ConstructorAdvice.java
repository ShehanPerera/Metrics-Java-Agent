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

public class ConstructorAdvice {

    /**
     * Resource invocation logic.
     *
     * @param parameters        ballerina context form constructor
     * @param contextTimer      newly defined Timer Context to start
     * @param isClientConnector newly defined boolean to set if  ClientConnector
     */
    @Advice.OnMethodExit
    public static void exitFromConstructor(@Advice.AllArguments Object[] parameters,
                                           @Advice.FieldValue(value = "contextTimer", readOnly = false) Timer.Context contextTimer,
                                           @Advice.FieldValue(value = "isClientConnector", readOnly = false) boolean isClientConnector)
            throws Exception {

        try {
            Context contextBal = (Context) parameters[0];
            if ("ClientConnector".equals(contextBal.actionInfo.getConnectorInfo().getName())) {
                MetricServer metricServer = MetricServer.getInstance();
                metricServer.getRequestsSize().update(contextBal.getCarbonMessage().getFullMessageLength());
                contextTimer = metricServer.getResponsesTime().start();
                isClientConnector = true;
            }
        } catch (Throwable e) {
            isClientConnector = false;
        }
    }

}