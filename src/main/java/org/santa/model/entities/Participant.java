package org.santa.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Stores the details for a participant in secret santa.
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "participant")
public class Participant {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    private String name;

    private String email;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Column(name = "email_verification_code")
    private String emailVerificationCode;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "santa_secret_id")
    private SecretSanta secretSanta;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "buys_gift_for_id")
    private Participant buysGiftFor;
}
