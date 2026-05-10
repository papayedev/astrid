package com.astridback.api.core.application.ports;

public interface APILogger {
    void info(String message);

    void warn(String message);

    void error(String message);

    void debug(String message);
}
