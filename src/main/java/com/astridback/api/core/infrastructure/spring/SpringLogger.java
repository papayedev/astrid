package com.astridback.api.core.infrastructure.spring;

import com.astridback.api.core.application.ports.APILogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringLogger implements APILogger {
    private Logger logger = LoggerFactory.getLogger(SpringLogger.class);

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }

    @Override
    public void debug(String message) {
        logger.debug(message);
    }
}
