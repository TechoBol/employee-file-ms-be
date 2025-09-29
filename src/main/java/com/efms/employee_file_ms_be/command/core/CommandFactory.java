package com.efms.employee_file_ms_be.command.core;

import org.springframework.context.ApplicationContext;

public abstract class CommandFactory {

    public <CMD extends Command> CMD createCommand(Class<CMD> commandClass) {
        return getApplicationContext().getBean(commandClass);
    }

    protected abstract ApplicationContext getApplicationContext();
}
