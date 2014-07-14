package org.jboss.pruivo.leads.command;

import org.infinispan.client.hotrod.RemoteCache;
import org.jboss.pruivo.leads.RemoteClientConsole;

/**
 * Deletes a text line from the cache.
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class DeleteLineCommand implements RemoteClientConsole.Command {
    @Override
    public void execute(RemoteCache<Object, Object> remoteCache) {
        String lineNumberString = System.console().readLine("Line number> ");
        int lineNumber = parseNumber(lineNumberString);
        if (lineNumber <= 0) {
            System.err.println("Invalid line number!");
            return;
        }

        if (remoteCache.remove(lineNumber) == null) {
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
        return "Delete line";
    }
}
