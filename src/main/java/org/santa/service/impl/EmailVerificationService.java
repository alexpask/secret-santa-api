package org.santa.service.impl;

import org.santa.model.entities.Participant;
import org.santa.repository.ParticipantRepository;
import org.santa.service.VerificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Email-based implementation for participant email verification.
 */
@Service
public class EmailVerificationService implements VerificationService {

    private final JavaMailSender sender;
    private final ParticipantRepository participantRepository;
    private final String baseUrl;

    public EmailVerificationService(JavaMailSender sender,
                                    ParticipantRepository participantRepository,
                                    @Value("${santa.url}") String baseUrl) {
        this.sender = sender;
        this.participantRepository = participantRepository;
        this.baseUrl = baseUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String send(String address, String participantId) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(address);
        message.setSubject("Verify email for Secret Santa");

        UUID uuid = UUID.randomUUID();

        message.setText("Hello, please click the link verify your email " +
                baseUrl + "/api/verify/" + participantId + "/" + uuid.toString());

        sender.send(message);

        return uuid.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void verify(String participantId, String verificationCode)
            throws Exception {

        Optional<Participant> participant =
                participantRepository.findById(participantId);

        if (!participant.isPresent()) {

            throw new Exception();
        }

        Participant real = participant.get();

        final boolean verified = real
                .getEmailVerificationCode()
                .equals(verificationCode);

        if (verified) {

            real.setEmailVerified(true);
        }

        participantRepository.save(real);
    }
}
