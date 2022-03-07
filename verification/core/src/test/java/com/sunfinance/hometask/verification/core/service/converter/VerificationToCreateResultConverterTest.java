package com.sunfinance.hometask.verification.core.service.converter;

import com.sunfinance.hometask.api.event.verification.VerificationCreateResult;
import com.sunfinance.hometask.verification.core.aggregate.Verification;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class VerificationToCreateResultConverterTest {

    private final static UUID VERIFICATION_ID = UUID.fromString("8633e6f8-3b48-4130-af61-28a39bd58b34");

    private final VerificationToCreateResultConverter converter = new VerificationToCreateResultConverter();

    @Test
    public void shouldConvertSuccessfully() {
        var verification = Verification.builder()
                                       .id(VERIFICATION_ID)
                                       .build();
        var expectedResult = VerificationCreateResult.builder()
                                                     .id(VERIFICATION_ID)
                                                     .build();

        var result = converter.convert(verification);
        assertEquals(expectedResult, result);
    }

}