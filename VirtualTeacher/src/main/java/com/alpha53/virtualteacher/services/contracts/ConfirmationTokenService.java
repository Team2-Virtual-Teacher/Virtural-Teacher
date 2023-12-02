package com.alpha53.virtualteacher.services.contracts;

import com.alpha53.virtualteacher.models.ConfirmationToken;

public interface ConfirmationTokenService {

    void save(ConfirmationToken token);

    ConfirmationToken get(String token);

    void setConfirmedAt(String token);

    void delete(String token);
}
