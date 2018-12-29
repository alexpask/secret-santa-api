package org.santa.service;

/**
 * Provides services to manage participant verification.
 */
public interface VerificationService {

    /**
     * Generates a verification code and send to specified
     * address.
     *
     * @param address Address to send verification to.
     * @param participantId uuid of participant. Used to
     *                      as part of the verification.
     * @return verification code.
     */
    String send(String address, String participantId);

    /**
     * Used to mark a participant as verified.
     *
     * @param participantId Participant to be verified.
     * @param verficationCode Code to verify participant.
     * @throws Exception If participant is not found.
     */
    void verify(String participantId, String verficationCode) throws Exception;
}
