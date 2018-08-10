package com.mazenk.instrumentation;

import java.util.function.Consumer;

/**
 * Standard data handlers that can
 * be provided via the first argument
 * of this application
 */
public enum DataHandler {
    CONSOLE((data) -> {
        System.out.println("-------------------------------------------------");
        System.out.println("Completed instrumenting " + data.instrumentationId().toString());
        System.out.println("Time taken: " + (data.endTime() - data.startTime()) + " ms");
        System.out.println("String count: " + data.stringCount());
        System.out.println("Classes loaded: " + data.loadedClasses());
        System.out.println("Memory Usage: " + data.memoryUsage() + " bytes");

        if (!data.additionalMetadata().isEmpty()) {
            System.out.println("Additional data:");

            data.additionalMetadata().forEach((key, value) ->
                    System.out.println(key + "=" + value)
            );
        }

        System.out.println("-------------------------------------------------");
    });

    private Consumer<InstrumentationData> consumer;

    DataHandler(Consumer<InstrumentationData> consumer) {
        this.consumer = consumer;
    }

    public Consumer<InstrumentationData> consumer() {
        return consumer;
    }
}
