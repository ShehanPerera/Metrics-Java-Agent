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

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.metrics.core.Timer;

import java.lang.instrument.Instrumentation;

/**
 * Metrics Java Agent for APIM-3.0
 */
public class Agent {

    private static final Logger logger = LoggerFactory.getLogger(Agent.class);

    public static void premain(String arguments, Instrumentation instrumentation) {

        logger.info("Premain");
        /* Create an agent to attach to ballerina backend and client side
        *
        * define new private fields to BalConnectorCallback class
        * contextTimer and isClientConnector
        *
        * Advice constructor for start Metrics timer, get request size
        * Advice done method for stop Metrics timer
        *
        */
        new AgentBuilder.Default()
                .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
                .type((ElementMatchers.nameEndsWith("BalConnectorCallback")))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .defineField("contextTimer", Timer.Context.class, Visibility.PRIVATE)
                        .defineField("isClientConnector", boolean.class, Visibility.PRIVATE)
                        .constructor(ElementMatchers.any())
                        .intercept(Advice.to(ConstructorAdvice.class))
                        .method(ElementMatchers.nameContains("done"))
                        .intercept(Advice.to(MethodListener.class))
                ).installOn(instrumentation);
        /* Create an agent to attach to ballerina backend and client side
        *
        * Define new private fields to ChannelInboundHandlerAdapter class
        * contextTimer and defaultHttpType
        *
        * Advice channelActive for start Metrics timer
        * Advice channelInactive for stop Metrics timer
        * Advice channelRead for get request size, response size and response status
        *
        */
        new AgentBuilder.Default()
                .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
                .type((ElementMatchers.nameEndsWith("ChannelInboundHandlerAdapter")))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .defineField("contextTimer", Timer.Context.class, Visibility.PRIVATE)
                        .defineField("defaultHttpType", String.class, Visibility.PRIVATE)
                        .method(ElementMatchers.nameEndsWith("channelActive"))
                        .intercept(Advice.to(ChannelActiveAdvice.class))
                        .method(ElementMatchers.nameEndsWith("channelInactive"))
                        .intercept(Advice.to(ChannelInactiveAdvice.class))
                        .method(ElementMatchers.nameEndsWith("channelRead"))
                        .intercept(Advice.to(ChannelReaderAdvice.class))
                ).installOn(instrumentation);

//TODO need remove this and find another way to get backend response size
        new AgentBuilder.Default()
                .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
                .type((ElementMatchers.nameEndsWith("DefaultBalCallback")))
                .transform(
                        new AgentBuilder.Transformer.ForAdvice()
                                .include(BackendResponseAdvice.class.getClassLoader())
                                .advice(ElementMatchers.nameEndsWith("done"), BackendResponseAdvice.class.getName())
                ).installOn(instrumentation);
    }
}


