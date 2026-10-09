package com.guilherdrk.backend.dto;

import java.time.Instant;

public record PingResponse(
        String status,
        Instant timestamp) {
    public static PingResponse created() {
        return new PingResponse("ok", Instant.now());
    }
}
