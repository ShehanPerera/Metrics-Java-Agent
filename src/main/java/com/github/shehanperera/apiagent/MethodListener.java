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
import org.wso2.carbon.metrics.core.Timer;

public class MethodListener {

    /**
     * Resource invocation logic.
     *
     * @param contextTimer     newly defined Timer Context to stop
     * @param isClientConnector newly defined boolean for check ClientConnector
     */
    @Advice.OnMethodExit
    public static void exitFromDoneMethod(@Advice.FieldValue(value = "contextTimer") Timer.Context contextTimer,
                                          @Advice.FieldValue(value = "isClientConnector") boolean isClientConnector)
            throws Exception {

        if (isClientConnector) {
            contextTimer.stop();
        }
    }
}


