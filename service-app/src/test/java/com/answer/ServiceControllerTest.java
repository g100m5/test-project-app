package com.answer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServiceControllerTest extends BaseTest {

    @Test
    public void validateResponseData() throws Exception {
        validateResponseData("/ask?question=", TestUtils.randomString(10));
    }

    @Test
    public void checkEndpointShouldReturn200status() throws Exception {
        checkOkResponse("/ask");
    }

    @Test
    public void checkTestAlwaysFailStatusNotCorrectURLProvided() throws Exception {
        checkOkResponse("/not/correct/url");
    }
}
