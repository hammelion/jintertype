package com.github.hammelion.jintertype.types;

import com.github.hammelion.jintertype.Type;

public class ValidatedType implements Type<String> {
    @Override
    public boolean isValid() {
        return value() == null || value().length() < 5;
    }
}
