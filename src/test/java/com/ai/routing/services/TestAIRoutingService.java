package com.ai.routing.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;

import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAIRoutingService {

    private AIRoutingService service;
    private final String BIN1 = "bin1";

    @PostConstruct
    private void setUp() {
        AIRoutingService service = new AIRoutingService();
    }

    @Test
    public void testPspSuggestedWhenThereIsNoHistory() {
        String result = service.suggestPSP(BIN1);

        assertNull(result);
    }
}
