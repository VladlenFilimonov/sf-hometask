package com.sunfinance.hometask.verification.template;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class
)
@ActiveProfiles("itest")
public abstract class BaseIntegrationTest {

    @LocalServerPort
    protected Integer serverPort;

    protected static String readFile(String fileName) {
        try {
            var testResource = BaseIntegrationTest.class.getResource(fileName);
            assertNotNull(String.format("Test resource file '%s' not found.", fileName), testResource);
            var bytes = new FileInputStream(testResource.getFile()).readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected String getBaseUrl() {
        return "http://localhost:" + serverPort;
    }

}
