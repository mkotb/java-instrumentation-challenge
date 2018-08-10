package com.mazenk.instrumentation.instrumentor;

import com.mazenk.instrumentation.DataCollector;
import com.mazenk.instrumentation.InstrumentationData;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class GenericInstrumentorIT {
    @Test
    public void testObjectCreationNoInstrumentation() {
        // shouldn't throw an exception
        GenericInstrumentor.onObjectCreation(false);
    }

    @Test
    public void testObjectCreation() {
        DataCollector.beginInstrumentation(new InstrumentationData());

        GenericInstrumentor.onObjectCreation(new Object());
        InstrumentationData data = DataCollector.getCurrentData();
        DataCollector.endInstrumentation();

        assertTrue(data.memoryUsage() > 0);
    }

    @Test
    public void testArrayCreationWithNonArray() {
        DataCollector.beginInstrumentation(new InstrumentationData());
        GenericInstrumentor.onArrayCreation(1);
        DataCollector.endInstrumentation();
    }

    @Test
    public void testArrayCreationNoInstrumentation() {
        GenericInstrumentor.onArrayCreation(new int[1]);
    }

    @Test
    public void testArrayCreation() {
        DataCollector.beginInstrumentation(new InstrumentationData());

        GenericInstrumentor.onArrayCreation(new Object[5]);
        InstrumentationData data = DataCollector.getCurrentData();
        DataCollector.endInstrumentation();

        assertTrue(data.memoryUsage() > 0);
    }
}
