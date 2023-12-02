package com.alpha53.virtualteacher.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ConfirmationToken {

    private int id;

    private String token;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    private String userEmail;

    public ConfirmationToken(String userEmail) {
        this.token = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusMinutes(10);
        this.userEmail = userEmail;
    }
}
