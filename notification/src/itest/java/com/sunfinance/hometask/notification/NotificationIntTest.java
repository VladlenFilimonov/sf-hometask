package com.sunfinance.hometask.notification;

import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.sunfinance.hometask.notification.consumer.NotificationConsumer;
import org.json.JSONException;
import org.junit.ClassRule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;
import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;

@DirtiesContext
@DatabaseTearDown(value = "classpath:dbunit/empty_dataset.xml", type = DELETE_ALL)
public class NotificationIntTest extends BaseIntegrationTest {

    @ClassRule
    public static WireMockClassRule wiremock = new WireMockClassRule(8085);
    @Autowired
    private NotificationConsumer notificationConsumer;
    @Autowired
    private NotificationMockListener mockListener;

    @PostConstruct
    private void start() {
        wiremock.start();
    }

    @PreDestroy
    private void stop() {
        wiremock.stop();
    }

    @Test
    @ExpectedDatabase(value = "classpath:dbunit/notification_created.xml", assertionMode = NON_STRICT)
    public void shouldSendEmailNotificationSuccessfully() throws JSONException, InterruptedException {
        wiremock.stubFor(post("/render")
                .willReturn(ok("test body")));
        var event = readFile("/message/verificationCreatedEvent.json");
        var expectedResult = readFile("/message/notificationResult.json");
        var result = notificationConsumer.listenCreate(event);
        JSONAssert.assertEquals(expectedResult, result, false);

        TimeUnit.SECONDS.sleep(1L);

        var expectedDispatchedEvent = readFile("/message/dispatchedEvent.json");
        var dispatcherEvent = mockListener.getDispatcherEvent();
        JSONAssert.assertEquals(expectedDispatchedEvent, dispatcherEvent, false);
    }
}
