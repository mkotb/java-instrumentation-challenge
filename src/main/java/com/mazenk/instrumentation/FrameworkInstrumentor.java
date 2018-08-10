package com.mazenk.instrumentation;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * Representing instrumentation with a specific purpose
 * and set range of classes to filter
 */
public interface FrameworkInstrumentor {
    /**
     * Should this class be loaded to then by passed down
     * to instrumentClass(CtClass) for instrumentation
     *
     * @see FrameworkInstrumentor#instrumentClass(CtClass)
     */
    boolean testClass(String className);

    /**
     * Perform any instrumentation necessary to the class
     * which was permitted via testClass(String)
     *
     * @see FrameworkInstrumentor#testClass(String)
     */
    void instrumentClass(CtClass clazz) throws CannotCompileException;

    /**
     * Should this (declared) method be instrumented?
     */
    default boolean testMethod(CtMethod method) {
        return false;
    }

    /**
     * By default, this method will never get called as
     * the implementation of testMethod(CtMethod) returns
     * false. But if that is overridden and it passes, it
     * calls this method for instrumentation
     *
     * @see FrameworkInstrumentor#testMethod(CtMethod)
     */
    void instrumentMethod(CtMethod method) throws CannotCompileException;
}
