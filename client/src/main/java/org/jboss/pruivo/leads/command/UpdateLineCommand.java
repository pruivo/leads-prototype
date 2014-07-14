package org.jboss.pruivo.leads.command;

import org.infinispan.client.hotrod.RemoteCache;
import org.jboss.pruivo.leads.RemoteClientConsole;

/**
 * Updates a text line from the cache. The line must exists.
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class UpdateLineCommand implements RemoteClientConsole.Command {

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

        if (remoteCache.replace(lineNumber, line) == null) {
            System.err.println("Line number does not exists!");
        }
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
        return "Replace line";
    }
}
