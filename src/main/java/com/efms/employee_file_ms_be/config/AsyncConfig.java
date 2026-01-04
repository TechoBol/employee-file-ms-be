package com.efms.employee_file_ms_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author Josue Veliz
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "historyTaskExecutor")
    public Executor historyTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("history-async-");
        executor.initialize();
        return executor;
    }
}
