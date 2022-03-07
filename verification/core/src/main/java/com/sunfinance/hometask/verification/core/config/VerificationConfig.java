package com.sunfinance.hometask.verification.core.config;

import com.sunfinance.hometask.api.event.verification.VerificationConfirmationEvent;
import com.sunfinance.hometask.api.event.verification.VerificationCreateEvent;
import com.sunfinance.hometask.verification.core.config.properties.VerificationProperties;
import com.sunfinance.hometask.verification.core.rules.BusinessRule;
import com.sunfinance.hometask.verification.core.rules.VerificationConfirmationBusinessRules;
import com.sunfinance.hometask.verification.core.rules.VerificationCreationBusinessRules;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties
public class VerificationConfig {

    @Bean
    @Validated
    @ConfigurationProperties(prefix = "verification")
    public VerificationProperties verificationProperties() {
        return new VerificationProperties();
    }

    @Bean
    public VerificationCreationBusinessRules verificationCreationBusinessRules(List<BusinessRule<VerificationCreateEvent>> rules) {
        var sortedRules = rules.stream()
                               .sorted(Comparator.comparing(BusinessRule::getOrder))
                               .collect(Collectors.toList());
        return new VerificationCreationBusinessRules(sortedRules);
    }

    @Bean
    public VerificationConfirmationBusinessRules verificationConfirmationBusinessRules(List<BusinessRule<VerificationConfirmationEvent>> rules) {
        var sortedRules = rules.stream()
                               .sorted(Comparator.comparing(BusinessRule::getOrder))
                               .collect(Collectors.toList());
        return new VerificationConfirmationBusinessRules(sortedRules);
    }
}
