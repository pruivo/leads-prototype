package org.jboss.pruivo.leads;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.jboss.pruivo.leads.command.AppendLineCommand;
import org.jboss.pruivo.leads.command.DeleteLineCommand;
import org.jboss.pruivo.leads.command.InsertLineCommand;
import org.jboss.pruivo.leads.command.UpdateLineCommand;
import org.jboss.pruivo.leads.configuration.Argument;
import org.jboss.pruivo.leads.configuration.Configuration;
import org.jboss.pruivo.leads.notification.LeadsListener;

/**
 * Main class for the client to interact with the Infinispan cache.
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class RemoteClientMain {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.parse(args);

        if (configuration.getAsBoolean(Argument.HELP)) {
            Argument.prettyPrintHelp(System.out);
            System.exit(0);
        }


        RemoteCacheManager remoteCacheManager = createRemoteCacheManager(configuration);
        RemoteCache<Object, Object> remoteCache = remoteCacheManager.getCache(configuration.get(Argument.CACHE_NAME));

        System.out.println("Using cache " + configuration.get(Argument.CACHE_NAME));

        if (remoteCache == null) {
            System.err.println("Cache " + configuration.get(Argument.CACHE_NAME) + " not found!");
            System.exit(1);
        }

        try {
            if (configuration.getAsBoolean(Argument.DUMP)) {
                for (Object key : remoteCache.keySet()) {
                    System.out.println(key + " = " + remoteCache.get(key));
                }
            } else if (configuration.getAsBoolean(Argument.LISTENER)) {
                startListener(remoteCache, configuration.get(Argument.LISTENER_CACHE_NAME));
            } else if (configuration.getAsBoolean(Argument.CLIENT)) {
                startClient(remoteCache);
            } else {
                System.err.println("No option select!");
            }
        } finally {
            remoteCache.stop();
            remoteCacheManager.stop();
        }
        System.exit(0);
    }

    private static void startListener(RemoteCache<Object, Object> remoteCache, String listenerCacheName) {
        System.out.println("Registering listener...");
        LeadsListener leadsListener = new LeadsListener();
        remoteCache.addClientListener(leadsListener, new Object[]{listenerCacheName}, new Object[0]);

        System.out.println("Listener registered. Press <enter> to exit.");
        System.console().readLine();
        System.out.println("Exiting");

        remoteCache.removeClientListener(leadsListener);
    }

    private static void startClient(RemoteCache<Object, Object> remoteCache) {
        RemoteClientConsole console = new RemoteClientConsole(remoteCache);
        console.register(new InsertLineCommand());
        console.register(new AppendLineCommand());
        console.register(new UpdateLineCommand());
        console.register(new DeleteLineCommand());
        console.run();
    }

    private static RemoteCacheManager createRemoteCacheManager(Configuration configuration) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer().host(configuration.get(Argument.HOST)).port(configuration.getAsInt(Argument.PORT));
        return new RemoteCacheManager(builder.build());
    }


}
