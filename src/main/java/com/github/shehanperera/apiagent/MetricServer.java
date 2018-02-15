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

import org.wso2.carbon.config.ConfigurationException;
import org.wso2.carbon.metrics.core.Histogram;
import org.wso2.carbon.metrics.core.Level;
import org.wso2.carbon.metrics.core.MetricService;
import org.wso2.carbon.metrics.core.Metrics;
import org.wso2.carbon.metrics.core.Timer;

public class MetricServer {

    /*
    * Metrics singleton for use with ballerina gateway
    * Used Timer and Histogram for request size  
    *
    * */

    private static final MetricServer metricServer = new MetricServer();
    private MetricService metricService;
    private Timer responsesTime;
    private Histogram requestsSize;

    private MetricServer() {

        Metrics metrics;
        try {
            metrics = new Metrics(TestUtils.getConfigProvider("metrics.yaml"));
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
        metrics.activate();
        metricService = metrics.getMetricService();
        requestsSize = metricService.histogram(MetricService.name("API Manager", "Request Size"), Level.INFO);
        responsesTime = metricService.timer(MetricService.name("API Manager", "Time to response"), Level.INFO);
    }

    public static MetricServer getInstance() {

        return metricServer;
    }

    public Histogram getRequestsSize() {

        return requestsSize;
    }

    public Timer getResponsesTime() {

        return responsesTime;
    }

}