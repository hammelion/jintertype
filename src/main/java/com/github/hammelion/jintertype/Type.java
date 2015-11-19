package com.github.hammelion.jintertype;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public interface Type<V> {

    default V value() {
        return null;
    }

    default boolean isValid() {
        return true;
    }

    HashMap<Integer, Function> instantiators = new HashMap<>();

    static <V, C extends Type<V>> C of(V value, Class<C> clazz) {
        if (value == null)
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return null;
            }
        Function instantiator;
        synchronized (instantiators) {
            instantiator = instantiators.get(clazz.hashCode());
            if (instantiator == null) {
                try {
                    final CtClass origClassCt = ClassPool.getDefault().get(clazz.getCanonicalName());
                    final String extClassName = clazz.getName() + "_Type";
                    final CtClass extClassCt = ClassPool.getDefault().makeClass(extClassName, origClassCt);
                    ClassPool.getDefault().importPackage(Type.class.getPackage().getName());
                    try {
                        // Force override of methods. Can be implemented in softer way.
                        final CtMethod equals = CtNewMethod.make(
                                "public boolean equals(Object o) { if (this == o) return true; if (o == null || getClass() != o.getClass()) return false; Type type = (Type) o; return !(value() != null ? !value().equals(type.value()) : type.value() != null); }",
                                extClassCt);
                        extClassCt.addMethod(equals);
                        final CtMethod hashCode = CtNewMethod
                                .make("public int hashCode() { return value() != null ? value().hashCode() : 0; }", extClassCt);
                        extClassCt.addMethod(hashCode);
                        final CtMethod toString = CtNewMethod.make("public String toString() { return String.valueOf(value()); }",
                                extClassCt);
                        extClassCt.addMethod(toString);

                        CtField valueField = CtField.make("private final " + value.getClass().getSimpleName() + " value;",
                                extClassCt);
                        extClassCt.addField(valueField);

                        CtConstructor constructor = CtNewConstructor.make("protected " + clazz.getSimpleName() + "("
                                + value.getClass().getSimpleName() + " value) { this.value = value; }", extClassCt);
                        extClassCt.addConstructor(constructor);

                        final CtMethod getValue = CtNewMethod.make("public final Object value() { return this.value; }",
                                extClassCt);
                        extClassCt.addMethod(getValue);

                        extClassCt.toClass();

                        final CtClass instantiatorCt = ClassPool.getDefault().makeClass(extClassName + "_Instantiator");
                        instantiatorCt.addInterface(ClassPool.getDefault().get(Function.class.getCanonicalName()));
                        instantiatorCt.addMethod(CtNewMethod.make("public Object apply(Object t) { return new " + extClassName
                                + "((" + value.getClass().getName() + ")t); }", instantiatorCt));
                        instantiator = (Function) instantiatorCt.toClass().newInstance();

                        instantiators.put(clazz.hashCode(), instantiator);
                    } catch (CannotCompileException | InstantiationException | IllegalAccessException e1) {
                        extClassCt.defrost();
                        throw e1;
                    }
                } catch (NotFoundException | CannotCompileException | InstantiationException | IllegalAccessException e1) {
                    instantiator = instantiators.get(clazz.hashCode());
                }
            }
        }
        C object = (C) instantiator.apply(value);
        if (object.isValid())
            return object;
        throw new IllegalArgumentException("Invalid value " + String.valueOf(value) + " for type " + clazz.getSimpleName());
    }

    static <V, C extends Type<V>> Optional<C> ofOptional(V value, Class<C> clazz) {
        C object = of(value, clazz);
        if (object.value() == null) {
            return Optional.empty();
        }
        return Optional.of(object);
    }
}
