package com.sunfinance.hometask.verification.core.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<VerificationEntity, Long> {

    Optional<VerificationEntity> findByVerificationId(String uuid);

}
