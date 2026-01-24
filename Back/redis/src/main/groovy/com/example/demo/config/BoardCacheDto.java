package com.example.demo.config;

import java.time.LocalDateTime;

public record BoardCacheDto(
        Long id,
        String title,
        LocalDateTime createdAt
) {}