package com.mazenk.instrumentation;

import com.mazenk.instrumentation.instrumentor.InstrumentorUtils;
import javassist.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.List;

/**
 * Transforms any classfile by looking
 * testing against the FrameworkInstrumentors
 * provided
 *
 * @see InstrumentorUtils#applyInstrumentors(List, CtClass)
 */
public class InternalClassTransformer implements ClassFileTransformer {
    private final List<FrameworkInstrumentor> instrumentors;

    InternalClassTransformer(List<FrameworkInstrumentor> instrumentors) {
        this.instrumentors = instrumentors;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] byteCode) {
        InstrumentationData data = DataCollector.getCurrentData();

        if (data != null) {
            data.incrementClassCount();
        }

        List<FrameworkInstrumentor> applicableInstrumentors = InstrumentorUtils.getApplicableInstrumentors(
                instrumentors,
                className
        );

        if (!applicableInstrumentors.isEmpty()) {
            try {
                ClassPool pool = ClassPool.getDefault();
                CtClass ctClass = pool.makeClass(new ByteArrayInputStream(byteCode));

                pool.insertClassPath(new LoaderClassPath(loader));
                InstrumentorUtils.applyInstrumentors(applicableInstrumentors, ctClass);

                byteCode = ctClass.toBytecode();
                ctClass.detach();
            } catch (IOException | CannotCompileException ex) {
                System.out.printf("Could not instrument %s due to an exception! Printing...\n", className);
                ex.printStackTrace();
            }
        }

        return byteCode;
    }
}
