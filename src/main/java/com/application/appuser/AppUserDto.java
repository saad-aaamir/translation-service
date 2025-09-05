package com.application.appuser;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {

    private Integer id;
    private String email;
    private boolean active;
    private String role;
    private Instant createdAt;
    private Instant lastUpdatedAt;

}
