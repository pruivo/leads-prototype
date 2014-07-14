package org.jboss.pruivo.leads;

import org.infinispan.client.hotrod.RemoteCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Command line interaction.
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class RemoteClientConsole {

    private final RemoteCache<Object, Object> remoteCache;
    private final List<Command> commandList;
    private boolean exit = false;

    public RemoteClientConsole(RemoteCache<Object, Object> remoteCache) {
        this.remoteCache = remoteCache;
        commandList = new ArrayList<Command>();
        commandList.add(new ExitCommand());
    }

    public final void register(Command command) {
        commandList.add(commandList.size() - 1, command);
    }

    public final void run() {
        while (!exit) {
            printMenu();
            Command command = selectCommand();
            if (command != null) {
                command.execute(remoteCache);
            }
        }

    }

    private void printMenu() {
        System.out.println("Commands:");
        int commandId = 0;
        for (Command command : commandList) {
            System.out.println(commandId++ + ": " + command.commandInfo());
        }
    }

    private Command selectCommand() {
        String line = System.console().readLine("> ");
        try {
            int commandId = Integer.parseInt(line);
            if (commandId >= 0 && commandId < commandList.size()) {
                return commandList.get(commandId);
            }
            System.err.println("Invalid command: " + line);
        } catch (NumberFormatException e) {
            System.err.println("Unknown command: " + line);
        }
        return null;
    }

    public static interface Command {
        void execute(RemoteCache<Object, Object> remoteCache);

        String commandInfo();
    }

    private class ExitCommand implements Command {

        @Override
        public void execute(RemoteCache<Object, Object> remoteCache) {
            exit = true;
        }

        @Override
        public String commandInfo() {
            return "Exit";
        }
    }

}
