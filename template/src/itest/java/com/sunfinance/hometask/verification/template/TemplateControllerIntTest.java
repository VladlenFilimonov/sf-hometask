package com.sunfinance.hometask.verification.template;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

import static io.restassured.RestAssured.given;

@DirtiesContext
public class TemplateControllerIntTest extends BaseIntegrationTest {

    @Test
    public void shouldRenderEmailTemplate() {
        prepareWebClient("/template/emailSlugRequest.json")
                .post(getBaseUrl() + "/templates/render")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldRenderMobileTemplate() {
        prepareWebClient("/template/mobileSlugRequest.json")
                .post(getBaseUrl() + "/templates/render")
                .then()
                .statusCode(200);
    }

    private RequestSpecification prepareWebClient(String request) {
        return given().contentType(ContentType.JSON)
                      .accept(ContentType.JSON)
                      .body(readFile(request))
                      .when();
    }
}