package com.mazenk.instrumentation;

import com.mazenk.instrumentation.instrumentor.*;
import javassist.ClassPool;
import javassist.CtClass;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.util.Arrays;
import java.util.List;

public class MainInstrumentation {
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation instrumentation) throws Exception {
        List<FrameworkInstrumentor> instrumentors = Arrays.asList(
                new SpringInstrumentor(),
                new StringInstrumentor(),
                new GenericInstrumentor()
        );

        DataHandler handler = null;

        try {
            if (args != null) {
                handler = DataHandler.valueOf(args.split(" ")[0].toUpperCase());
            }
        } catch (IllegalArgumentException ignored) {
        }

        if (handler == null) {
            handler = DataHandler.CONSOLE;
        }

        DataCollector.init(handler.consumer());

        MainInstrumentation.instrumentation = instrumentation;

        instrumentation.addTransformer(new InternalClassTransformer(
                instrumentors
        ), true);

        instrumentation.retransformClasses(String.class);
    }

    public static Instrumentation instrumentation() {
        return instrumentation;
    }
}
