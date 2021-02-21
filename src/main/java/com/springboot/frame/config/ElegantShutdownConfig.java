package com.springboot.frame.config;

import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 优雅停止springboot服务配置类
 *
 * @author yangguanghui6
 * @date 2021/2/21 17:08
 */
public class ElegantShutdownConfig implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {

    private Logger logger = LoggerFactory.getLogger(ElegantShutdownConfig.class);

    private volatile Connector connector;

    private final int waitTime = 10;

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        connector.pause();
        Executor executor = connector.getProtocolHandler().getExecutor();
        if (executor instanceof ThreadPoolExecutor) {
            try {
                ThreadPoolExecutor executors = (ThreadPoolExecutor) executor;
                executors.shutdown();
                if (!executors.awaitTermination(waitTime, TimeUnit.SECONDS)) {
                    logger.info("请尝试暴力关闭！");
                }
            } catch (InterruptedException e) {
                logger.error("服务关闭异常！");
                Thread.currentThread().interrupt();
            }
        }
    }


}
