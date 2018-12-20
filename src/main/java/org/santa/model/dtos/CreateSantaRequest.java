package org.santa.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Dto result from form to create a secret santa list.
 */
@Data
public class CreateSantaRequest {

    private List<Participant> participants;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Participant {

        private String name;
        private String email;
    }
}
