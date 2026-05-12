package com.astridback.api.infrastructure.services;

import com.astridback.api.application.services.Base64Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.function.Function;

public class Base64ServiceImpl<T> implements Base64Service<T> {

    private final Function<String, T> decoder;

    public Base64ServiceImpl(Function<String, T> decoder) {
        this.decoder = decoder;
    }

    @Override
    public Optional<T> decode(String payload) {
        if (payload == null || payload.isBlank()) {
            return Optional.empty();
        }

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(payload);
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

            T result = decoder.apply(decodedString);

            if (result == null) {
                return Optional.empty();
            }

            return Optional.of(result);

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}