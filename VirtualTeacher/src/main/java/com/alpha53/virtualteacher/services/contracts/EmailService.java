package com.alpha53.virtualteacher.services.contracts;

public interface EmailService {
    void send(String to, String email, String subject);

    String buildConfirmationEmail(String name, String link);

    String buildReferralEmail(String firstName, String lastName, String registrationLink);
}
