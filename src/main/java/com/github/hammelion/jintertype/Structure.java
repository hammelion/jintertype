package com.github.hammelion.jintertype;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class Structure {
    private final Set<String> _errors = new HashSet<>();

    protected final <V, C extends Type<V>> C of(V value, Class<C> clazz) {
        try {
            return Type.of(value, clazz);
        } catch (IllegalArgumentException e) {
            _errors.add(e.getMessage());
        }
        return null;
    }

    protected final <V, C extends Type<V>> Optional<C> ofOptional(V value, Class<C> clazz) {
        try {
            return Type.ofOptional(value, clazz);
        } catch (IllegalArgumentException e) {
            _errors.add(e.getMessage());
        }
        return Optional.empty();
    }

    public final boolean hasErrors() {
        return _errors.size() > 0;
    }

    public final boolean hasNoErrors() {
        return !hasErrors();
    }

    public final Set<String> _getErrors() {
        return new HashSet<>(_errors);
    }
}
