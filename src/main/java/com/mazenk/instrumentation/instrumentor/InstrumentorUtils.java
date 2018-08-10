package com.mazenk.instrumentation.instrumentor;

import com.mazenk.instrumentation.FrameworkInstrumentor;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class InstrumentorUtils {
    /**
     * Returns code from the resource file of
     * the jar without comments
     */
    public static String getCode(String resource) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream is = classLoader.getResourceAsStream(resource);

        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return reader.lines()
                    .filter((line) -> !line.startsWith("//"))
                    .collect(Collectors.joining(System.lineSeparator()));
        }

        return null;
    }

    /**
     * Utility method to test the instrumentor against the class
     * name and return all the ones which pass testClass(String)
     *
     * @see FrameworkInstrumentor#testClass(String)
     */
    public static List<FrameworkInstrumentor> getApplicableInstrumentors(List<FrameworkInstrumentor> instrumentors, String className) {
        return instrumentors.stream()
                .filter((instrumentor) -> instrumentor.testClass(className))
                .collect(Collectors.toList());
    }

    /**
     * Loop through all the instrumentors and call their instrumentClass(CtClass)
     * method and find any applicable methods in CtClass to instrument
     *
     * @see FrameworkInstrumentor#instrumentClass(CtClass)
     * @see FrameworkInstrumentor#testMethod(CtMethod)
     * @see FrameworkInstrumentor#testClass(String)
     */
    public static void applyInstrumentors(List<FrameworkInstrumentor> instrumentors, CtClass ctClass) {
        CtMethod[] methods = ctClass.getDeclaredMethods();

        instrumentors.forEach((instrumentor) -> {
            try {
                instrumentor.instrumentClass(ctClass);

                for (CtMethod method : methods) {
                    if (instrumentor.testMethod(method)) {
                        instrumentor.instrumentMethod(method);
                    }
                }
            } catch (CannotCompileException ex) {
                System.out.printf(
                        "%s could not compile instrumentation for %s, printing exception...",
                        instrumentor.getClass().getSimpleName(),
                        ctClass.getName()
                );
                ex.printStackTrace();
            }
        });
    }

    /**
     * Tests if a constructor makes a this(...) call
     * to another one as their first line
     */
    public static boolean callsThis(CtConstructor constructor) throws CannotCompileException {
        CodeAttribute codeAttr = constructor.getMethodInfo().getCodeAttribute();

        if (codeAttr != null) {
            CodeIterator it = codeAttr.iterator();

            try {
                int index = it.skipThisConstructor();
                return index >= 0;
            } catch (BadBytecode ex) {
                throw new CannotCompileException(ex);
            }
        } else {
            return false;
        }
    }
}
