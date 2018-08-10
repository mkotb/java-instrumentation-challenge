package com.mazenk.instrumentation;

import java.util.function.Consumer;

/**
 * Holds InstrumentationData for when data is being collected.
 *
 * Each Thread holds its own data from our instrumentation and
 * accesses it from here. Therefore, this class must be thread
 * safe
 */
public class DataCollector {
    private static final DataCollector instance = new DataCollector();
    private ThreadLocal<InstrumentationData> instrumentationData = new ThreadLocal<>();
    // keeping volatile here is in the case the code accessing this
    // changes but this field is only written to at the beginning
    // of the application and should not change after that without
    // change to how this is read for thread safety
    private static volatile Consumer<InstrumentationData> dataHandler;

    private DataCollector() {
    }

    public static void beginInstrumentation(InstrumentationData data) {
        instance.instrumentationData.set(data);
    }

    public static InstrumentationData getCurrentData() {
        return instance.instrumentationData.get();
    }

    public static void endInstrumentation() {
        InstrumentationData data = getCurrentData();
        instance.instrumentationData.set(null);

        if (data == null) {
            return;
        }

        data.setEndTime(System.currentTimeMillis());

        if (dataHandler != null) {
            dataHandler.accept(data);
        }
    }

    public static void init(Consumer<InstrumentationData> dataHandler) {
        DataCollector.dataHandler = dataHandler;
    }
}
