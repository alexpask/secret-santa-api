package org.santa.service.impl;

import org.santa.model.entities.Participant;
import org.santa.model.entities.SecretSanta;
import org.santa.repository.ParticipantRepository;
import org.santa.service.VerificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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

        // Now let's check if everyone has verified
        SecretSanta secretSanta = real.getSecretSanta();
        long unverifiedCount = secretSanta
                .getParticipants()
                .stream()
                .filter(p -> !p.isEmailVerified())
                .count();

        if (unverifiedCount == 0) {

            assignSantas(secretSanta);
        }
    }

    @Async
    void assignSantas(SecretSanta secretSanta) {

        List<Participant> participants = secretSanta.getParticipants();
        Collections.shuffle(participants);
        Participant first = participants.get(0);
        participants.add(first);

        for (int idx = 0; idx < participants.size() - 1; idx++) {

            Participant participant = participants.get(idx);
            Participant buysFor = participants.get(idx + 1);
            participant.setBuysGiftFor(buysFor);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(participant.getEmail());
            message.setSubject("Time for your Secret Santa!!!");

            message.setText("Hey there " + participant.getName() +
                    " you will be buying a gift for " + buysFor.getName());

            sender.send(message);
        }

        participants.remove(participants.size() - 1);
        participantRepository.saveAll(participants);
    }
}
