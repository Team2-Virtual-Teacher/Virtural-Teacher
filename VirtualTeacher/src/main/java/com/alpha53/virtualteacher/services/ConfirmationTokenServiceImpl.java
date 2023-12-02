package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.models.ConfirmationToken;
import com.alpha53.virtualteacher.repositories.contracts.ConfirmationTokenDao;
import com.alpha53.virtualteacher.services.contracts.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final ConfirmationTokenDao confirmationTokenDao;

    @Override
    public void save(ConfirmationToken token) {
        confirmationTokenDao.save(token);
    }
    @Override

    public ConfirmationToken get(String token) {
        return confirmationTokenDao.findByToken(token);
    }
    @Override
    public void setConfirmedAt(String token) {
        confirmationTokenDao.updateConfirmedAt(token, LocalDateTime.now());
    }

    @Override
    public void delete(String token) {
        confirmationTokenDao.delete(token);
    }


}
