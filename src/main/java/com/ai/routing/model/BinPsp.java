package com.ai.routing.model;

import java.util.Objects;

public class BinPsp {

    private final String bin;
    private final String psp;

    public BinPsp(String bin, String psp) {
        this.bin = bin;
        this.psp = psp;
    }

    public String getBin() {
        return bin;
    }

    public String getPsp() {
        return psp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinPsp binPsp = (BinPsp) o;
        return Objects.equals(bin, binPsp.bin) &&
                Objects.equals(psp, binPsp.psp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bin, psp);
    }

    @Override
    public String toString() {
        return "BinPsp{" +
                "bin='" + bin + '\'' +
                ", psp='" + psp + '\'' +
                '}';
    }
}
