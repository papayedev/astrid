package com.astridback.api.application.services;

import java.util.Optional;

public interface Base64Service<T> {
    Optional<T> decode(String payload);
}
