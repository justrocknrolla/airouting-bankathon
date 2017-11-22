package com.ai.routing.model;

import java.util.HashMap;

public class DataStorage {

    private HashMap<BinPsp, History> history = new HashMap<>();

    public History getHisotry(BinPsp binPsp) {
        return history.getOrDefault(binPsp, new History());
    }
}
