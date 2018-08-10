package com.mazenk.instrumentation.instrumentor;

import com.mazenk.instrumentation.DataCollector;
import com.mazenk.instrumentation.FrameworkInstrumentor;
import com.mazenk.instrumentation.InstrumentationData;
import com.mazenk.instrumentation.MainInstrumentation;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.NewArray;

/**
 * Instruments all classes to monitor memory usage
 *
 * It hooks into their constructors to execute a callback
 * to get their object size and register it in InstrumentationData
 *
 * It also hooks into any new array creation. This is critical
 * as it cannot be accessed by hooking into an "Array class"
 * (like we did with String) as that doesn't exist.
 *
 * @see GenericInstrumentor#onObjectCreation(Object)
 * @see GenericInstrumentor#onArrayCreation(Object)
 */
public class GenericInstrumentor implements FrameworkInstrumentor {
    public static void onObjectCreation(Object object) {
        InstrumentationData data = DataCollector.getCurrentData();

        if (data != null) {
            data.increaseMemoryUsage(MainInstrumentation.instrumentation().getObjectSize(object));
        }
    }

    public static void onArrayCreation(Object array) {
        InstrumentationData data = DataCollector.getCurrentData();

        if (data != null) {
            data.registerArray(array);
        }
    }

    @Override
    public boolean testClass(String className) {
        return true;
    }

    @Override
    public void instrumentClass(CtClass clazz) throws CannotCompileException {
        String code = InstrumentorUtils.getCode("Generic.constructor.after.java");

        for (CtConstructor constructor : clazz.getDeclaredConstructors()) {
            if (!InstrumentorUtils.callsThis(constructor)) {
                constructor.insertAfter(
                        code
                );
            }
        }

        String arrayCode = InstrumentorUtils.getCode("ArrayCreation.java");

        clazz.instrument(new ExprEditor() {
            @Override
            public void edit(NewArray a) throws CannotCompileException {
                a.replace(arrayCode);
            }
        });
    }

    @Override
    public void instrumentMethod(CtMethod method) throws CannotCompileException {

    }
}
