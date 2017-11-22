package com.ai.routing.model;

public class Stats {

    private final float average;
    private final float deviation;

    public Stats(float average, float deviation) {
        this.average = average;
        this.deviation = deviation;
    }

    public float getAverage() {
        return average;
    }

    public float getDeviation() {
        return deviation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stats stats = (Stats) o;

        if (Float.compare(stats.average, average) != 0) return false;
        return Float.compare(stats.deviation, deviation) == 0;
    }

    @Override
    public int hashCode() {
        int result = (average != +0.0f ? Float.floatToIntBits(average) : 0);
        result = 31 * result + (deviation != +0.0f ? Float.floatToIntBits(deviation) : 0);
        return result;
    }
}
