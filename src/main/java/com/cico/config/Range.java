package com.cico.config;

public class Range {
    private final long start;
    private final long end;

    public Range(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public boolean isValid() {
        return start >= 0 && (end == -1 || end >= start);
    }

    public String getContentRangeHeaderValue(long totalSize) {
        // Example: "bytes 1000-1999/5000"
        return "bytes " + start + "-" + (end == -1 ? "" : end) + "/" + totalSize;
    }
}
