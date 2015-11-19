package com.github.hammelion.jintertype.structures;

import java.util.Optional;

import com.github.hammelion.jintertype.Structure;
import com.github.hammelion.jintertype.types.SomeDoubleTestType;
import com.github.hammelion.jintertype.types.SomeStringTestType;
import com.github.hammelion.jintertype.types.ValidatedType;

public class SomeStructure extends Structure {
    private final ValidatedType name;

    private final SomeDoubleTestType salary;

    private final Optional<ValidatedType> email;

    public SomeStructure(String name, Double salary, String email) {
        this.name = of(name, ValidatedType.class);
        this.salary = of(salary, SomeDoubleTestType.class);
        this.email = ofOptional(email, ValidatedType.class);
    }

    public SomeStructure(String name, Double salary) {
        this(name, salary, null);
    }

    public ValidatedType getName() {
        return name;
    }

    public SomeDoubleTestType getSalary() {
        return salary;
    }

    public Optional<ValidatedType> getEmail() {
        return email;
    }
}
