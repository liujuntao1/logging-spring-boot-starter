package com.logging.autoconfigure;

import com.logging.aspect.SysLogAspect;
import com.logging.filter.TraceFilter;
import com.logging.service.SysLogService;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author ljt
 * @date 2026/1/21
 * @description
 */
@Configuration
@MapperScan("com.logging.mapper")
@EnableAspectJAutoProxy // 开启 AOP 支持
public class LogAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean // 只有当容器中没有这个 Bean 时才创建，方便用户自定义
    public SysLogService sysLogService() {
        return new SysLogService();
    }

    @Bean
    @ConditionalOnMissingBean // 只有当容器中没有这个 Bean 时才创建，方便用户自定义
    public SysLogAspect logAspect() {
        return new SysLogAspect();
    }

    @Bean
    @ConditionalOnMissingBean // 只有当容器中没有这个 Bean 时才创建，方便用户自定义
    public TraceFilter TraceFilter() {
        return new TraceFilter();
    }


    @Bean("logExecutor")
    @ConditionalOnMissingBean(name = "logExecutor")
    public Executor logExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(500);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("sys-log-");
        executor.setTaskDecorator(runnable -> {
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            return () -> {
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                try {
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            };
        });
        executor.initialize();
        return executor;
    }

}