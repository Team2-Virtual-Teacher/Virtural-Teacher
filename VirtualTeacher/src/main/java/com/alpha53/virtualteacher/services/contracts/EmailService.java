package com.alpha53.virtualteacher.services.contracts;

import java.io.ByteArrayOutputStream;

public interface EmailService {
    void send(String to, String email, String subject, ByteArrayOutputStream pdfStream, String pdfFileName);

    String generateConfirmationEmail(String name, String link);

    String generateReferralEmail(String firstName, String lastName, String registrationLink);

    String generateGraduationEmail(String firstName, String course);
}
