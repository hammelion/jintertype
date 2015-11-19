package com.github.hammelion.jintertype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

import com.github.hammelion.jintertype.structures.SomeStructure;

public class StructureTest {

    public static final String NAME = "Alice";
    public static final String BAD_NAME = "Brunhilda";

    public static final Double SALARY = 3000.14;

    public static final String EMAIL = "a@b.c";
    public static final String BAD_EMAIL = "brunhilda@b.c";

    @Test
    public void testErrors() throws Exception {
        SomeStructure alice = new SomeStructure(NAME, SALARY, EMAIL);

        assertTrue(alice.hasNoErrors());
        assertFalse(alice.hasErrors());
        assertTrue(alice._getErrors().isEmpty());
    }

    @Test
    public void testErrorsBadName() throws Exception {
        SomeStructure alice = new SomeStructure(BAD_NAME, SALARY, EMAIL);

        assertTrue(alice.hasErrors());
        assertTrue(alice._getErrors().size() == 1);
        assertTrue(alice._getErrors().stream().allMatch(error -> error.contains(BAD_NAME)));
    }

    @Test
    public void testErrorsBadEmail() throws Exception {
        SomeStructure alice = new SomeStructure(NAME, SALARY, BAD_EMAIL);

        assertTrue(alice.hasErrors());
        assertTrue(alice._getErrors().size() == 1);
        assertTrue(alice._getErrors().stream().allMatch(error -> error.contains(BAD_EMAIL)));
    }

    @Test
    public void testErrorsBadNameAndEmail() throws Exception {
        SomeStructure alice = new SomeStructure(BAD_NAME, SALARY, BAD_EMAIL);

        assertTrue(alice.hasErrors());
        assertTrue(alice._getErrors().size() == 2);
    }

    @Test
    public void testValues() throws Exception {
        SomeStructure alice = new SomeStructure(NAME, SALARY, EMAIL);

        assertEquals(NAME, alice.getName().value());
        assertEquals(SALARY, alice.getSalary().value());
        assertEquals(EMAIL, alice.getEmail().get().value());
    }

    @Test
    public void testOptional() throws Exception {
        SomeStructure alice = new SomeStructure(NAME, SALARY);

        assertNotNull(alice.getEmail());
        assertEquals(Optional.empty(), alice.getEmail());
    }
}
