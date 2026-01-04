package com.efms.employee_file_ms_be.command.core;

import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class CommandFactoryProducer implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CommandFactory commandFactory() {
        return new CommandFactory() {
            @Override
            protected ApplicationContext getApplicationContext() {
                return applicationContext;
            }
        };
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}