package org.jboss.pruivo.leads.configuration;

import org.infinispan.client.hotrod.impl.ConfigurationProperties;

import java.io.PrintStream;
import java.util.Map;
import java.util.Queue;

/**
 * Program command line arguments.
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public enum Argument {
    HOST("--host", true, "localhost", "HotRod server host name or ip address."),
    PORT("--port", true, Integer.toString(ConfigurationProperties.DEFAULT_HOTROD_PORT), "HotRod server port."),
    CACHE_NAME("--cachename", true, "leads-cache", "Remote cache name."),
    LISTENER_CACHE_NAME("--lcachename", true, "listener-cache", "Remote cache name where the listener will write."),
    LISTENER("--listener", false, null, "Starts in listener mode. It will register a listener and prints the events received."),
    DUMP("--dump", false, null, "Dumps the key/values stored in the remote cache."),
    CLIENT("--client", false, null, "Starts in client mode. It allows to insert/update/delete keys in cache."),
    HELP("--help", false, null, "Prints this message.");

    private final String arg;
    private final String description;
    private final boolean hasValue;
    private final String defaultValue;

    Argument(String arg, boolean hasValue, String defaultValue, String description) {
        this.arg = arg;
        this.hasValue = hasValue;
        this.defaultValue = defaultValue;
        this.description = description;
    }

    public String getArg() {
        return arg;
    }

    public static void setDefaultValues(Map<Argument, String> map) {
        for (Argument argument : values()) {
            if (argument.defaultValue != null) {
                map.put(argument, argument.defaultValue);
            } else if (!argument.hasValue) {
                map.put(argument, Boolean.toString(false));
            }
        }
    }

    public static void parse(Queue<String> args, Map<Argument, String> map) {
        while (!args.isEmpty()) {
            String arg = args.poll();
            boolean found = false;
            for (Argument argument : values()) {
                if (arg.equals(argument.arg)) {
                    if (argument.hasValue) {
                        String value = args.poll();
                        if (value == null) {
                            throw new IllegalArgumentException("Argument " + arg + " expects a value!");
                        }
                        map.put(argument, value);
                    } else {
                        map.put(argument, Boolean.toString(true));
                    }
                    found = true;
                }
            }
            if (!found) {
                throw new IllegalArgumentException("Unknown argument " + arg);
            }
        }
    }

    public static void prettyPrintHelp(PrintStream stream) {
        stream.println();
        final int argumentMaxWidth = argumentMaxWidth();
        for (Argument argument : values()) {
            StringBuilder builder = new StringBuilder(argument.arg);
            if (argument.hasValue) {
                builder.append(" <value> ");
            } else {
                builder.append(" ");
            }
            appendSpaces(argumentMaxWidth, builder);
            builder.append(argument.description);
            stream.println(builder);
            if (argument.defaultValue != null) {
                builder = new StringBuilder();
                appendSpaces(argumentMaxWidth, builder);
                builder.append("default value=").append(argument.defaultValue);
                stream.println(builder);
            }
            stream.println();
        }
    }

    private static int argumentMaxWidth() {
        int maxWidth = 0;
        for (Argument argument : values()) {
            if (argument.arg.length() > maxWidth) {
                maxWidth = argument.arg.length();
            }
        }
        return maxWidth + " <value>: ".length();
    }

    private static void appendSpaces(int width, StringBuilder builder) {
        while (builder.length() < width) {
            builder.append(' ');
        }
    }
}
