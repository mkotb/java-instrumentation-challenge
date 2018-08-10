package com.mazenk.instrumentation.instrumentor;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.junit.Test;

import static org.junit.Assert.*;

public class InstrumentorUtilsTest {
    @Test
    public void testCodeFetchGeneral() {
        assertEquals(InstrumentorUtils.getCode("test.java"), "hello");
    }

    @Test
    public void testCodeFetchNull() {
        assertNull(InstrumentorUtils.getCode("DoesntExist.java"));
    }

    @Test
    public void testCallsThis() {
        try {
            CtClass clazz = ClassPool.getDefault().get(TestClass.class.getName());

            assertTrue(InstrumentorUtils.callsThis(clazz.getDeclaredConstructor(new CtClass[0])));
            assertFalse(InstrumentorUtils.callsThis(clazz.getDeclaredConstructor(new CtClass[] { CtClass.booleanType })));
        } catch (NotFoundException | CannotCompileException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static class TestClass {
        private boolean test;

        public TestClass(boolean s) {
            this.test = s;
        }

        public TestClass() {
            this(true);
        }
    }
}
