package com.sunfinance.hometask.verification.core.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.time.Instant;

@Data
@Entity
@Table(name = "verification")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "verification_id", nullable = false, unique = true)
    private String verificationId;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotBlank
    @Column(name = "client_ip", nullable = false)
    private String clientIp;

    @NotBlank
    @Column(name = "client_agent", nullable = false)
    private String clientAgent;

    @NotBlank
    @Column(name = "subject_type", nullable = false)
    private String subjectType;

    @NotBlank
    @Column(name = "subject_identity", nullable = false)
    private String subjectIdentity;

    @NotNull
    @Column(name = "code", nullable = false)
    private BigInteger code;

    @Column(name = "confirmed", nullable = false)
    private boolean confirmed;

    @Column(name = "attempts", nullable = false)
    private int attempts;

}
