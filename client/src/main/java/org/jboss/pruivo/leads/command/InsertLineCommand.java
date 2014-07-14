package org.jboss.pruivo.leads.command;

import org.infinispan.client.hotrod.RemoteCache;
import org.jboss.pruivo.leads.RemoteClientConsole;

/**
 * Inserts a text line to the cache. The line number is specified and the content of the line is replaced if exists.
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class InsertLineCommand implements RemoteClientConsole.Command {
    @Override
    public void execute(RemoteCache<Object, Object> remoteCache) {
        String lineNumberString = System.console().readLine("Line number> ");
        int lineNumber = parseNumber(lineNumberString);
        if (lineNumber <= 0) {
            System.err.println("Invalid line number!");
            return;
        }

        String line = System.console().readLine("Text> ").trim();
        if (line.isEmpty()) {
            System.err.println("Empty line not added!");
            return;
        }

        remoteCache.put(lineNumber, line);
    }

    private int parseNumber(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public String commandInfo() {
        return "Insert line";
    }
}
