package com.mazenk.instrumentation.instrumentor;

import com.mazenk.instrumentation.FrameworkInstrumentor;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * Hooks into Spring's FrameworkServlet so Spring requests
 * can be instrumented
 */
public class SpringInstrumentor implements FrameworkInstrumentor {
    @Override
    public boolean testClass(String className) {
        return "org/springframework/web/servlet/FrameworkServlet".equals(className);
    }

    @Override
    public void instrumentClass(CtClass clazz) throws CannotCompileException {
    }

    @Override
    public void instrumentMethod(CtMethod method) throws CannotCompileException {
        method.insertBefore(InstrumentorUtils.getCode("spring/FrameworkServlet#processRequest.before.java"));
        method.insertAfter(InstrumentorUtils.getCode("spring/FrameworkServlet#processRequest.after.java"));
    }

    @Override
    public boolean testMethod(CtMethod method) {
        return method.getName().equals("processRequest");
    }
}
