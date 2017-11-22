package com.ai.routing.services;

import com.ai.routing.model.Point;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AIRoutingService {

    public String suggestPSP(String bin) {
        Set<String> availablePSPs = this.getPSPs(bin);
        if (availablePSPs.isEmpty()) {
            return null;
        }
        return null;
    }

    private Set<String> getPSPs(String bin) {
        return new HashSet<>();
    }

    public void postPSP(String bin, String psp, Boolean result) {
        // TODO
    }

    public List<Point> getChartData(String bin, String psp, int n) {
        return null;
    }
}
