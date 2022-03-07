package com.sunfinance.hometask.verification.rest;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@DirtiesContext
public class VerificationCreateControllerIntTest extends BaseIntegrationTest {

    private final static UUID VERIFICATION_ID = UUID.fromString("8633e6f8-3b48-4130-af61-28a39bd58b34");

    @Test
    public void createVerificationSuccess() {
        prepareWebClient("/verification/createEmailVerification.json")
                .post(getBaseUrl() + "/verifications")
                .then()
                .statusCode(200)
                .body("id", equalTo(VERIFICATION_ID.toString()));
    }

    @Test
    public void confirmVerificationSuccess() {
        prepareWebClient("/verification/verificationConfirm.json")
                .post(getBaseUrl() + "/verifications/8633e6f8-3b48-4130-af61-28a39bd58b34/confirm")
                .then()
                .statusCode(204);
    }

    private RequestSpecification prepareWebClient(String request) {
        return given().contentType(ContentType.JSON)
                      .accept(ContentType.JSON)
                      .header(HttpHeaders.USER_AGENT, "test-agent")
                      .body(readFile(request))
                      .when();
    }
}