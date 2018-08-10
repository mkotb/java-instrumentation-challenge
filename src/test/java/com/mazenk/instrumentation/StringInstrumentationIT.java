package com.mazenk.instrumentation;

import org.junit.Test;

import java.nio.charset.Charset;
import java.util.function.LongSupplier;

import static org.junit.Assert.*;

public class StringInstrumentationIT {
    private void beginInstrumentation() {
        DataCollector.beginInstrumentation(new InstrumentationData());
    }

    private void endInstrumentation() {
        DataCollector.endInstrumentation();
    }

    private void basicTest(LongSupplier supplier) {
        beginInstrumentation();
        assertEquals(supplier.getAsLong(), DataCollector.getCurrentData().stringCount());
        endInstrumentation();
    }

    @Test
    public void literalTest() {
        basicTest(() -> {
            // this string is not created, it is stored as a constant and
            // thus is not counted
            String test = "sadsaa";
            return 0;
        });
    }

    @Test
    public void stringConstructor() {
        basicTest(() -> {
            String test = new String("sdasa");
            return 1;
        });
    }

    @Test
    public void emptyConstructor() {
        basicTest(() -> {
            String test = new String();
            return 1;
        });
    }

    @Test
    public void byteArrayConstructor() {
        basicTest(() -> {
            String test = new String("sadsad".getBytes());
            return 1;
        });
    }

    @Test
    public void byteArrayByteConstructor() {
        basicTest(() -> {
            String test = new String("sadsad".getBytes(), (byte) 0);
            return 1;
        });
    }

    @Test
    public void byteArrayCharsetConstructor() {
        basicTest(() -> {
            String test = new String("sadsad".getBytes(), Charset.defaultCharset());
            return 1;
        });
    }

    @Test
    public void byteArrayIntConstructor() {
        basicTest(() -> {
            String test = new String("sadasd".getBytes(), 0);
            return 1;
        });
    }

    @Test
    public void byteArrayIntIntConstructor() {
        basicTest(() -> {
            String test = new String("sadsad".getBytes(), 0, 0);
            return 1;
        });
    }

    @Test
    public void byteArrayIntIntCharsetConstructor() {
        basicTest(() -> {
            String test = new String("sdaasd".getBytes(), 0, 0, Charset.defaultCharset());
            return 1;
        });
    }

    @Test
    public void byteArrayIntIntIntConstructor() {
        basicTest(() -> {
            String test = new String("sadsad".getBytes(), 0, 0, 0);
            return 1;
        });
    }

    @Test
    public void byteArrayIntIntStringConstructor() {
        basicTest(() -> {
            try {
                String test = new String("sadsad".getBytes(), 0, 0, "UTF-8");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return 1;
        });
    }

    @Test
    public void byteArrayStringConstructor() {
        basicTest(() -> {
            try {
                String test = new String("sadsad".getBytes(), "UTF-8");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return 1;
        });
    }

    @Test
    public void charArrayConstructor() {
        basicTest(() -> {
            String test = new String("sadsad".toCharArray());
            return 1;
        });
    }

    @Test
    public void charArrayIntIntConstructor() {
        basicTest(() -> {
            String test = new String("sadsad".toCharArray(), 0, 0);
            return 1;
        });
    }

    @Test
    public void intArrayIntIntConstructor() {
        basicTest(() -> {
            String test = new String(new int[0], 0, 0);
            return 1;
        });
    }

    @Test
    public void stringBufferConstructor() {
        basicTest(() -> {
            String test = new String(new StringBuffer());
            return 1;
        });
    }

    @Test
    public void stringBuilderConstructor() {
        basicTest(() -> {
            String test = new String(new StringBuilder());
            return 1;
        });
    }
}
