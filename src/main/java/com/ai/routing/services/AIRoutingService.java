package com.ai.routing.services;

import com.ai.routing.model.BinPsp;
import com.ai.routing.model.DataStorage;
import com.ai.routing.model.History;
import com.ai.routing.model.Point;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AIRoutingService {

    private DataStorage storage = new DataStorage();

    private Float calculateExpectedProbability(String bin, String psp) {
        BetaDistribution beta = this.getDecisionDistribution(bin, psp);
        return (float) beta.sample();
    }

    private BetaDistribution getDecisionDistribution(String bin, String psp) {
        History record = storage.getHistory(new BinPsp(bin, psp));
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

    public List<Point> getChartData(String bin, String psp, int n) {
        return null;
    }
}
