package org.jboss.pruivo.leads.configuration;

import java.util.*;

/**
 * Keeps the program command line argument's values.
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class Configuration {

    private final Map<Argument, String> argumentValues;

    public Configuration() {
        argumentValues = new EnumMap<Argument, String>(Argument.class);
    }

    public final String get(Argument argument) {
        return getOrThrowException(argument);
    }

    public final int getAsInt(Argument argument) {
        return Integer.parseInt(getOrThrowException(argument));
    }

    public final boolean getAsBoolean(Argument argument) {
        return Boolean.parseBoolean(getOrThrowException(argument));
    }

    public final void parse(String[] args) {
        argumentValues.clear();
        Argument.setDefaultValues(argumentValues);
        Argument.parse(new LinkedList<String>(Arrays.asList(args)), argumentValues);
    }

    private String getOrThrowException(Argument argument) {
        String value = argumentValues.get(argument);
        if (value == null) {
            throw new NoSuchElementException("Missing argument " + argument.getArg());
        }
        return value;
    }
}
