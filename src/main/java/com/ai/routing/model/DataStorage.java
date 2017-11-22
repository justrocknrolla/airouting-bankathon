package com.ai.routing.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataStorage {

    private static final Logger log = LoggerFactory.getLogger(DataStorage.class);

    private Map<BinPsp, History> history = new HashMap<>();
    private Map<String, Set<String>> pspBins = new HashMap<>();
    private Map<String, Set<String>> binPSPs = new HashMap<>();

    public History getHistory(BinPsp binPsp) {
        return history.getOrDefault(binPsp, new History());
    }

    public Set<String> getAllBins(String psp) {
        return pspBins.getOrDefault(psp, new HashSet<>());
    }

    public Set<String> getAllPsps(String bin) {
        return binPSPs.getOrDefault(bin, new HashSet<>());
    }

    public void postResult(BinPsp binPsp, boolean result) {
        log.info("Storing history: bin, psp={}, res={}", binPsp, result);
        updateHistory(binPsp, result);
        updatePspBins(binPsp);
        updateBinPSPs(binPsp);
    }

    private void updateHistory(BinPsp binPsp, boolean result) {
        history.put(binPsp, getHistory(binPsp).addResult(result));
    }

    private void updatePspBins(BinPsp binPsp) {
        String bin = binPsp.getBin();
        String psp = binPsp.getPsp();
        if (pspBins.containsKey(psp)) {
            pspBins.get(psp).add(bin);
        } else {
            pspBins.put(psp, new HashSet<>());
            pspBins.get(psp).add(bin);
        }
    }

    private void updateBinPSPs(BinPsp binPsp) {
        String bin = binPsp.getBin();
        String psp = binPsp.getPsp();
        if (binPSPs.containsKey(bin)) {
            binPSPs.get(bin).add(psp);
        } else {
            binPSPs.put(bin, new HashSet<>());
            binPSPs.get(bin).add(psp);
        }
    }
}
