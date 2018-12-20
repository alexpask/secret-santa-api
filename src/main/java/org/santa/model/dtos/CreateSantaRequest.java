package org.santa.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Dto result from form to create a secret santa list.
 */
@Data
public class CreateSantaRequest {

    @Valid
    @Size(min = 3)
    private List<Participant> participants;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Participant {

        @NotEmpty
        private String name;

        @Email
        private String email;
    }
}
