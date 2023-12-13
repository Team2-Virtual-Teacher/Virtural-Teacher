package com.alpha53.virtualteacher.repositories.contracts;

import com.alpha53.virtualteacher.models.ConfirmationToken;

import java.time.LocalDateTime;

public interface ConfirmationTokenDao {

    ConfirmationToken findByToken(String token);

    void save(ConfirmationToken token);

    void updateConfirmedAt(String token, LocalDateTime now);

    void delete(String token);
}
