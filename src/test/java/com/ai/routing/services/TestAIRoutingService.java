package com.ai.routing.services;

import com.ai.routing.model.BinPsp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAIRoutingService {

    private AIRoutingService service;
    private final String BIN1 = "bin1";
    private final String PSP1 = "psp1";
    private final String PSP2 = "psp2";
    private final BinPsp BIN1_PSP1 = new BinPsp(BIN1, PSP1);
    private final BinPsp BIN1_PSP2 = new BinPsp(BIN1, PSP2);

    @PostConstruct
    private void setUp() {
        service = new AIRoutingService();
    }

    @Test
    public void testPspSuggestedWhenThereIsNoHistory() {
        String result = service.suggestPSP(BIN1);

        assertNull(result);
    }

    @Test
    public void testPspSuggestedWhenSingleGoodProviderKnown() {
        service.postResult(BIN1_PSP1, true);
        String result = service.suggestPSP(BIN1);
        assertEquals(PSP1, result);
    }

    @Test
    public void testPspSuggestedWhenBadGoodProviderKnown() {
        service.postResult(BIN1_PSP1, false);
        String result = service.suggestPSP(BIN1);
        assertEquals(PSP1, result);
    }

    @Test
    public void testPspSuggestedWhenSimilarProviderAvailable() {
        service.postResult(BIN1_PSP1, true);
        service.postResult(BIN1_PSP1, true);
        service.postResult(BIN1_PSP2, true);
        service.postResult(BIN1_PSP2, true);

        Set<String> proposedProviders = new HashSet<>();
        String currentRecommendation;
        for (int i = 0; i < 10; i++) {
            currentRecommendation = service.suggestPSP(BIN1);
            proposedProviders.add(currentRecommendation);
        }

        assertTrue(proposedProviders.contains(PSP1));
        assertTrue(proposedProviders.contains(PSP2));
    }

    @Test
    public void testPspSuggestedWhenMuchSuperiorProviderAvailable() {
        for (int i = 0; i < 10; i++) {
            service.postResult(BIN1_PSP1, false);
        }

        for (int i = 0; i < 10; i++) {
            service.postResult(BIN1_PSP2, true);
        }

        Set<String> proposedProviders = new HashSet<>();
        String currentRecommendation;
        for (int i = 0; i < 10; i++) {
            currentRecommendation = service.suggestPSP(BIN1);
            proposedProviders.add(currentRecommendation);
        }

        assertFalse(proposedProviders.contains(PSP1));
        assertTrue(proposedProviders.contains(PSP2));
    }
}
