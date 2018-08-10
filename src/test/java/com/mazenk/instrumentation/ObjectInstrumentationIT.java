package com.mazenk.instrumentation;

import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectInstrumentationIT {
    private void beginInstrumentation() {
        DataCollector.beginInstrumentation(new InstrumentationData());
    }

    private void endInstrumentation() {
        DataCollector.endInstrumentation();
    }

    /**
     * This range allows for a reasonable amount
     * of room for any JVM overhead that may be
     * on an object.
     *
     * @param expected The amount of memory expected to be used
     * @param objects The amount of objects that were made. An
     *                array counts as two as they need four additional
     *                bytes for the array length, and in JVMs like
     *                HotSpot 4 bytes are typically added as padding
     */
    private void assertWithinRange(long expected, double objects) {
        long usage = DataCollector.getCurrentData().memoryUsage();
        long diff = usage - expected;

        assertTrue(diff >= 0 && (double) diff <= (8 * objects));
    }

    @Test
    public void byteArrayTest() {
        beginInstrumentation();

        byte[] test = new byte[24];

        assertWithinRange(24, 2);
        endInstrumentation();
    }

    @Test
    public void objectArrayTest() {
        beginInstrumentation();

        Object[] objects = new Object[5];

        assertWithinRange(8 * 5, 7);
    }
}
