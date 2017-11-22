package com.ai.routing.model;

import java.util.Objects;

public class History {
    private final int successCount;
    private final int failCount;

    public History() {
        this(0, 0);
    }

    public History(int successCount, int failCount) {
        this.successCount = successCount;
        this.failCount = failCount;
    }

    public History addSuccess() {
        return new History(successCount + 1, failCount);
    }

    public History addFail() {
        return new History(successCount, failCount + 1);
    }

    public History addResult(boolean result) {
        return result ? addSuccess() : addFail();
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getFailCount() {
        return failCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return successCount == history.successCount &&
                failCount == history.failCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(successCount, failCount);
    }
}
