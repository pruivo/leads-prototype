package org.jboss.pruivo.leads.command;

import org.infinispan.client.hotrod.RemoteCache;
import org.jboss.pruivo.leads.RemoteClientConsole;

/**
 * Appends a new text line to the cache.
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class AppendLineCommand implements RemoteClientConsole.Command {

    @Override
    public void execute(RemoteCache<Object, Object> remoteCache) {
        String line = System.console().readLine("Text> ").trim();
        if (line.isEmpty()) {
            System.err.println("Empty line not added!");
            return;
        }
        boolean added = false;
        int lineNumber = 0;
        while (!added) {
            lineNumber = getNextLine(remoteCache);
            added = remoteCache.putIfAbsent(lineNumber, line) == null;
        }
        System.out.println("Line added with number " + lineNumber);
    }

    private int getNextLine(RemoteCache<Object, ?> remoteCache) {
        int nextLine = 0;
        for (Object key : remoteCache.keySet()) {
            int value = (Integer) key;
            if (value > nextLine) {
                nextLine = value;
            }
        }
        return nextLine + 1;
    }

    @Override
    public String commandInfo() {
        return "Append line";
    }
}
