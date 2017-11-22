package com.ai.routing.services;

import com.ai.routing.model.BinPsp;
import com.ai.routing.model.Point;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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


    @Test
    public void getChartDataWhenNoHistoryAvailable() {
        List<Point> chartData = service.getChartData(BIN1_PSP1, 5);
        List<Point> expectedData = new ArrayList<>();

        expectedData.add(new Point(0f, 1.0f));
        expectedData.add(new Point(0.25f, 1.0f));
        expectedData.add(new Point(0.5f, 1.0f));
        expectedData.add(new Point(0.75f, 1.0f));
        expectedData.add(new Point(1.0f, 1.0f));

        assertSameSets(expectedData, chartData);
    }

    private void assertSameSets(List<Point> expectedData, List<Point> chartData) {
        assertEquals(expectedData.size(), chartData.size());

        float EPSILON = 0.001f;
        float xExpected, xActual;
        float yExpected, yActual;

        for (int i = 0; i < expectedData.size(); i++) {
            xActual = chartData.get(i).getX();
            xExpected = expectedData.get(i).getX();
            assertEquals(xExpected, xActual, EPSILON);

            yActual = chartData.get(i).getY();
            yExpected = expectedData.get(i).getY();
            assertEquals(yExpected, yActual, EPSILON);
        }
    }

    @Test
    public void getChartDataWhenHistoryAvailable() {
        service.postResult(BIN1_PSP1, true);
        service.postResult(BIN1_PSP1, true);
        service.postResult(BIN1_PSP1, false);

        List<Point> chartData = service.getChartData(BIN1_PSP1, 5);
        List<Point> expectedData = new ArrayList<>();

        expectedData.add(new Point(0f, 0f));
        expectedData.add(new Point(0.25f, 0.5625f));
        expectedData.add(new Point(0.5f, 1.5f));
        expectedData.add(new Point(0.75f, 1.6875f));
        expectedData.add(new Point(1.0f, 0f));

        assertSameSets(expectedData, chartData);
    }
}
