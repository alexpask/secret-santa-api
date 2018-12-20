package org.santa.service.impl;

import org.santa.exception.SantaException;
import org.santa.model.dtos.CreateSantaRequest;
import org.santa.model.entities.Participant;
import org.santa.model.entities.SecretSanta;
import org.santa.model.entities.User;
import org.santa.repository.ParticipantRepository;
import org.santa.repository.SecretSantaRepository;
import org.santa.repository.UsersRepository;
import org.santa.service.SantaService;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class SantaServiceImpl implements SantaService {

    private final UsersRepository usersRepository;
    private final SecretSantaRepository secretSantaRepository;
    private final ParticipantRepository participantRepository;

    public SantaServiceImpl(
            UsersRepository usersRepository,
            SecretSantaRepository secretSantaRepository,
            ParticipantRepository participantRepository) {

        this.usersRepository = usersRepository;
        this.secretSantaRepository = secretSantaRepository;
        this.participantRepository = participantRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SecretSanta create(String username, CreateSantaRequest createSantaRequest) {

        User user = usersRepository.getUserByUsername(username);

        if (secretSantaRepository.existsByCreator(user)) {

            throw new SantaException("This user already has a secret santa list");
        }

        SecretSanta secretSanta = new SecretSanta();
        secretSanta.setCreator(user);

        secretSantaRepository.save(secretSanta);

        List<Participant> participants = createSantaRequest
                .getParticipants()
                .stream()
                .map(p -> Participant
                        .builder()
                        .name(p.getName())
                        .email(p.getEmail())
                        .secretSanta(secretSanta)
                        .build())
                .collect(toList());

        participantRepository.saveAll(participants);

        secretSanta.setParticipants(participants);

        return secretSantaRepository.save(secretSanta);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SecretSanta getbyUsername(String username) {

        User user = usersRepository.getUserByUsername(username);

        return secretSantaRepository.getByCreator(user);
    }
}
