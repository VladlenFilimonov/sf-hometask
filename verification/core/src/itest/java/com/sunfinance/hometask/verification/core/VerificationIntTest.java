package com.sunfinance.hometask.verification.core;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.sunfinance.hometask.verification.core.consumer.VerificationConsumer;
import com.sunfinance.hometask.verification.core.service.generator.CodeGenerator;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.IdGenerator;

import java.math.BigInteger;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;
import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT;
import static org.mockito.Mockito.when;

@DirtiesContext
@DatabaseTearDown(value = "classpath:dbunit/empty_dataset.xml", type = DELETE_ALL)
public class VerificationIntTest extends BaseIntegrationTest {

    private final static UUID VERIFICATION_ID = UUID.fromString("8633e6f8-3b48-4130-af61-28a39bd58b34");
    private final static BigInteger TEST_CODE = BigInteger.valueOf(1234567890L);

    @Autowired
    private VerificationConsumer verificationConsumer;

    @Autowired
    private IdGenerator idGeneratorMock;

    @Autowired
    private CodeGenerator<BigInteger> codeGenerator;

    @Autowired
    private VerificationMockListener verificationMockListener;

    @Autowired
    private Clock clock;

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/verification_created.xml", assertionMode = NON_STRICT)
    public void shouldCreateVerificationSuccessfully() throws JSONException, InterruptedException {
        when(idGeneratorMock.generateId())
                .thenReturn(VERIFICATION_ID);
        when(codeGenerator.generateCode())
                .thenReturn(TEST_CODE);
        when(clock.instant())
                .thenReturn(Clock.systemUTC().instant());
        var event = readFile("/message/verificationCreateEvent.json");
        var expectedResult = readFile("/message/verificationResult.json");
        var result = verificationConsumer.listenCreate(event);
        JSONAssert.assertEquals(expectedResult, result, false);

        TimeUnit.SECONDS.sleep(1L);

        var expectedEvent = readFile("/message/verificationCreatedEvent.json");
        var createdEvent = verificationMockListener.getCreatedEvent();
        JSONAssert.assertEquals(expectedEvent, createdEvent, false);
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/verification_confirmation_setup.xml")
    @ExpectedDatabase(value = "classpath:dbunit/verification_confirmed.xml", assertionMode = NON_STRICT)
    public void shouldConfirmVerificationSuccessfully() throws JSONException, InterruptedException {
        when(clock.instant())
                .thenReturn(Clock.fixed(Instant.parse("2022-01-01T10:10:10.00Z"), ZoneId.of("UTC")).instant());
        when(clock.getZone())
                .thenReturn(Clock.systemUTC().getZone());
        var event = readFile("/message/verificationConfirmEvent.json");
        var expectedResult = readFile("/message/verificationResult.json");
        var result = verificationConsumer.listenConfirm(event);
        JSONAssert.assertEquals(expectedResult, result, false);

        TimeUnit.SECONDS.sleep(1L);

        var expectedEvent = readFile("/message/verificationConfirmedEvent.json");
        var confirmedEvent = verificationMockListener.getConfirmedEvent();
        JSONAssert.assertEquals(expectedEvent, confirmedEvent, false);
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/verification_confirmation_setup.xml")
    @ExpectedDatabase(value = "classpath:dbunit/verification_failed.xml", assertionMode = NON_STRICT)
    public void shouldFailConfirmationAndIncrementAttempt_whenCodeNotEquals() throws JSONException, InterruptedException {
        when(clock.instant())
                .thenReturn(Clock.fixed(Instant.parse("2022-01-01T10:10:10.00Z"), ZoneId.of("UTC")).instant());
        when(clock.getZone())
                .thenReturn(Clock.systemUTC().getZone());
        var event = readFile("/message/verificationConfirmWrongCodeEvent.json");
        var expectedResult = readFile("/message/VerificationInvalidCodeResult.json");
        var result = verificationConsumer.listenConfirm(event);
        JSONAssert.assertEquals(expectedResult, result, false);

        TimeUnit.SECONDS.sleep(1L);

        var expectedEvent = readFile("/message/verificationConfirmFailedEvent.json");
        var confirmFailedEvent = verificationMockListener.getConfirmationFailedEvent();
        JSONAssert.assertEquals(expectedEvent, confirmFailedEvent, false);
    }
}
