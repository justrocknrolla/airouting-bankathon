package com.ai.routing.services;

import com.ai.routing.model.*;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AIRoutingService {

    private DataStorage storage = new DataStorage();

    private Float calculateExpectedProbability(String bin, String psp) {
        BetaDistribution beta = this.getDecisionDistribution(new BinPsp(bin, psp));
        return (float) beta.sample();
    }

    private BetaDistribution getDecisionDistribution(BinPsp binPsp) {
        History record = storage.getHistory(binPsp);
        return new BetaDistribution(record.getSuccessCount() + 1, record.getFailCount() + 1);
    }

    public String suggestPSP(String bin) {
        Set<String> availablePSPs = this.getPSPs(bin);
        if (availablePSPs.isEmpty()) {
            return null;
        }
        String bestPSP = "";
        Float maxProbability = -1.0f;
        Float expectedProbability;
        for (String psp : availablePSPs) {
            expectedProbability = this.calculateExpectedProbability(bin, psp);
            if (expectedProbability >= maxProbability) {
                bestPSP = psp;
                maxProbability = expectedProbability;
            }
        }
        return bestPSP;
    }

    private Set<String> getPSPs(String bin) {
        return storage.getAllPsps(bin);
    }

    public void postResult(BinPsp binPsp, boolean result) {
        storage.postResult(binPsp, result);
    }

    public List<Point> getChartData(BinPsp binPsp, int numberOfPoints) {
        List<Point> chartData = new ArrayList<>(numberOfPoints);
        BetaDistribution beta = this.getDecisionDistribution(binPsp);

        float x, y;
        float step = 1.0f / (numberOfPoints - 1);
        for (int i = 0; i < numberOfPoints; i++) {
            x = i * step;
            // dirty hack as library produce improper zeros
            if (beta.getAlpha() == 1 && beta.getBeta() == 1) {
                y = 1.0f;
            } else {
                try {
                    y = (float) beta.density(x);
                } catch (NumberIsTooSmallException ex) {
                    y = 0.0f;
                }
            }
            chartData.add(new Point(x, y));
        }

        return chartData;
    }

    public Stats getStats(BinPsp binPsp) {
        return null;
    }
}
