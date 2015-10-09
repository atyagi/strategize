package org.atyagi.strategies.util;

import java.util.function.Predicate;

public class Filters {

    public static Predicate<? super Object> notNull() {
        return t -> t != null;
    }

}
