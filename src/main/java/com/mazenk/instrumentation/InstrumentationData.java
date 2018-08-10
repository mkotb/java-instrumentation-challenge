package com.mazenk.instrumentation;

import java.util.*;

/**
 * Represents data to measure during execution
 *
 * This class does not need to be thread safe,
 * as it is locked via ThreadLocal in the
 * DataCollector. Thus it goes without saying,
 * don't keep a copy of an instance of this and
 * access it from other threads.
 */
public class InstrumentationData {
    private UUID instrumentationId = UUID.randomUUID();
    private long stringCount;
    private long memoryUsage;
    private long loadedClasses;
    private final long startTime;
    private long endTime;
    // typically used for fields that
    // are specific to the framework
    // being used. e.g. path for a REST server
    private Map<String, String> additionalMetadata = new HashMap<>();
    private List<Object> arrays = new ArrayList<>();

    public InstrumentationData() {
        startTime = System.currentTimeMillis();
    }

    public UUID instrumentationId() {
        return instrumentationId;
    }

    public long stringCount() {
        return stringCount;
    }

    public long startTime() {
        return startTime;
    }

    public long endTime() {
        return endTime;
    }

    public long memoryUsage() {
        return memoryUsage + arrays.stream()
                .mapToLong((array) -> MainInstrumentation.instrumentation().getObjectSize(array))
                .sum();
    }

    public Map<String, String> additionalMetadata() {
        return additionalMetadata;
    }

    void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void incrementStringCount() {
        stringCount++;
    }

    public void incrementClassCount() {
        loadedClasses++;
    }

    public long loadedClasses() {
        return loadedClasses;
    }

    public void increaseMemoryUsage(long amount) {
        memoryUsage += amount;
    }

    public void registerArray(Object array) {
        arrays.add(array);
    }
}
