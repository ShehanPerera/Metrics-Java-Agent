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
 *
 */
package com.github.shehanperera.apiagent;

import org.wso2.carbon.config.ConfigurationException;
import org.wso2.carbon.metrics.core.Histogram;
import org.wso2.carbon.metrics.core.Level;
import org.wso2.carbon.metrics.core.Meter;
import org.wso2.carbon.metrics.core.MetricService;
import org.wso2.carbon.metrics.core.Metrics;
import org.wso2.carbon.metrics.core.Timer;

/**
 * Metrics used for the APIM-3.0
 */
public class MetricServer {

    private static final MetricServer metricServer = new MetricServer();
    private MetricService metricService;
    private Timer backendResponsesTime;
    private Timer apiResponsesTime;
    private Meter backendErrorRate;
    private Meter apiErrorRate;
    private Histogram backendRequestsSize;
    private Histogram apiRequestsSize;
    private Histogram backendResponseSize;
    private Histogram apiResponseSize;

    private MetricServer() {

        Metrics metrics;
        try {
            metrics = new Metrics(TestUtils.getConfigProvider("metrics.yaml"));
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
        metrics.activate();
        metricService = metrics.getMetricService();
        backendErrorRate = metricService.meter(MetricService.name("API Manager ",
                "Backend Error Rate "), Level.INFO);
        apiErrorRate = metricService.meter(MetricService.name("API Manager ",
                "API Error Rate "), Level.INFO);
        backendRequestsSize = metricService.histogram(MetricService.name("API Manager ",
                "Backend Request Payload Size"), Level.INFO);
        apiRequestsSize = metricService.histogram(MetricService.name("API Manager ",
                "API Request Payload Size"), Level.INFO);
        backendResponseSize = metricService.histogram(MetricService.name("API Manager ",
                "Backend Response Payload Size"), Level.INFO);
        apiResponseSize = metricService.histogram(MetricService.name("API Manager ",
                "API Response Payload Size"), Level.INFO);
        backendResponsesTime = metricService.timer(MetricService.name("API Manager ",
                "Backend Calls"), Level.INFO);
        apiResponsesTime = metricService.timer(MetricService.name("API Manager ",
                "API Calls"), Level.INFO);
    }

    public static MetricServer getInstance() {

        return metricServer;
    }

    public Meter getBackendErrorRate() {

        return backendErrorRate;
    }

    public Meter getApiErrorRate() {

        return apiErrorRate;
    }

    public Histogram getBackendRequestsSize() {

        return backendRequestsSize;
    }

    public Histogram getApiRequestsSize() {

        return apiRequestsSize;
    }

    public Histogram getBackendResponseSize() {

        return backendRequestsSize;
    }

    public Histogram getApiResponseSize() {

        return apiResponseSize;
    }

    public Timer getBackendResponsesTime() {

        return backendResponsesTime;
    }

    public Timer getApiResponsesTime() {

        return apiResponsesTime;
    }

}
