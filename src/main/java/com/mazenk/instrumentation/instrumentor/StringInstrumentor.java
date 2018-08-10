package com.mazenk.instrumentation.instrumentor;

import com.mazenk.instrumentation.FrameworkInstrumentor;
import javassist.*;

/**
 * Hooks into String's constructors to maintain a count
 * of Strings created during a period of time.
 *
 * We must not insert a hook into any constructor that calls
 * another one. This is for two reasons:
 *
 * - We would end up counting that string twice
 * - Even if we wanted to, it would end up crashing some JVMs for... some reason
 *
 * @see InstrumentorUtils#callsThis(CtConstructor)
 */
public class StringInstrumentor implements FrameworkInstrumentor {
    @Override
    public boolean testClass(String className) {
        return "java/lang/String".equals(className);
    }

    @Override
    public void instrumentClass(CtClass clazz) throws CannotCompileException {
        String code = InstrumentorUtils.getCode("String.constructor.after.java");

        for (CtConstructor constructor : clazz.getDeclaredConstructors()) {
            if (!InstrumentorUtils.callsThis(constructor)) {
                constructor.insertBeforeBody(code);
            }
        }
    }

    @Override
    public void instrumentMethod(CtMethod method) throws CannotCompileException {

    }

}
