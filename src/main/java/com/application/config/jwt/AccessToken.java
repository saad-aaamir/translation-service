package com.application.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken implements Serializable {

    @Id
    private String token;
    private String uuid;
    private boolean revoked;
    private boolean expired;
    private Instant createdAt;
    private Instant lastUpdatedAt;

    public static AccessToken create(String token, String uuid) {
        return AccessToken.builder()
                .token(token)
                .uuid(uuid)
                .revoked(false)
                .expired(false)
                .createdAt(Instant.now())
                .lastUpdatedAt(Instant.now())
                .build();
    }
}
