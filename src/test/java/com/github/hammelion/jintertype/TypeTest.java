package com.github.hammelion.jintertype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;

import com.github.hammelion.jintertype.types.SomeDoubleTestType;
import com.github.hammelion.jintertype.types.SomeStringTestType;
import com.github.hammelion.jintertype.types.ValidatedType;

public class TypeTest {

    public static final String VALID_VAL = "val";

    public static final String INVALID_VAL = "tooLongVal";

    public static final String VAL1 = "val1";

    public static final String VAL2 = "val2";

    public static final Double DOUBLE1 = 100.100;

    public static final Double DOUBLE2 = 200.200;

    @Test
    public void testStringType() throws Exception {
        SomeStringTestType test1 = Type.of(VAL1, SomeStringTestType.class);
        SomeStringTestType test2 = Type.of(VAL1, SomeStringTestType.class);
        SomeStringTestType test3 = Type.of(VAL2, SomeStringTestType.class);

        assertTrue(test1.equals(test2));
        assertTrue(test1.hashCode() == test2.hashCode());
        assertEquals(test1.value(), test2.value());
        assertEquals(test1.toString(), test2.toString());
        assertEquals(test1, test2);
        assertNotEquals(test1, test3);
    }

    @Test
    public void testDoubleType() throws Exception {
        SomeDoubleTestType test1 = Type.of(DOUBLE1, SomeDoubleTestType.class);
        SomeDoubleTestType test2 = Type.of(DOUBLE1, SomeDoubleTestType.class);
        SomeDoubleTestType test3 = Type.of(DOUBLE2, SomeDoubleTestType.class);

        assertTrue(test1.equals(test2));
        assertTrue(test1.hashCode() == test2.hashCode());
        assertEquals(test1.value(), test2.value());
        assertEquals(test1.toString(), test2.toString());
        assertEquals(test1, test2);
        assertNotEquals(test1, test3);
    }

    @Test
    public void testValidationOverride() throws Exception {
        ValidatedType test = Type.of(VALID_VAL, ValidatedType.class);

        assertEquals(test.value(), VALID_VAL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidationOverrideInvalidValue() throws Exception {
        Type.of(INVALID_VAL, ValidatedType.class);
    }

    @Test
    public void testThreadSafety() throws Exception {
        Arrays.asList(new Integer[10000000]).parallelStream().forEach(val -> Type.of(VAL1, SomeStringTestType.class));
    }

    @Test
    public void testOptional() throws Exception {
        SomeStringTestType test = Type.of(VAL1, SomeStringTestType.class);
        Optional<SomeStringTestType> testOptional = Type.ofOptional(VAL1, SomeStringTestType.class);

        assertEquals(test, testOptional.get());
        assertTrue(testOptional.isPresent());
    }

    @Test
    public void testOptionalEmpty() throws Exception {
        Optional<SomeStringTestType> testOptional = Type.ofOptional(null, SomeStringTestType.class);

        assertFalse(testOptional.isPresent());
    }
}
