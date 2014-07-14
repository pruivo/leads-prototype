package org.jboss.pruivo.leads;

import org.infinispan.commons.equivalence.ByteArrayEquivalence;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.parsing.ConfigurationBuilderHolder;
import org.infinispan.configuration.parsing.ParserRegistry;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.server.hotrod.HotRodServer;
import org.infinispan.server.hotrod.configuration.HotRodServerConfigurationBuilder;
import org.jboss.pruivo.leads.configuration.Argument;
import org.jboss.pruivo.leads.configuration.Configuration;
import org.jboss.pruivo.leads.notification.LeadsConverterFactory;
import org.jboss.pruivo.leads.notification.LeadsKeyValueFilterFactory;

import java.io.IOException;

/**
 * The main class that starts an Infinispan server instance.
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class InfinispanServerMain {

    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration();
        configuration.parse(args);

        if (configuration.getAsBoolean(Argument.HELP)) {
            Argument.prettyPrintHelp(System.out);
            System.exit(0);
        }

        System.out.println("Using configuration " + configuration.get(Argument.CONFIGURATION));
        ParserRegistry registry = new ParserRegistry();
        ConfigurationBuilderHolder holder = registry.parseFile(configuration.get(Argument.CONFIGURATION));

        ConfigurationBuilder builder = holder.getNamedConfigurationBuilders().get(configuration.get(Argument.CACHE_NAME));
        if (builder == null) {
            throw new IllegalArgumentException("Cache " + configuration.get(Argument.CACHE_NAME) + " not defined!");
        }

        //we need to ensure we have the right equivalence for hotrod
        builder.dataContainer()
                .keyEquivalence(ByteArrayEquivalence.INSTANCE)
                .valueEquivalence(ByteArrayEquivalence.INSTANCE);
        //disabled because ISPN-4474
        builder.compatibility().disable();

        builder = holder.getNamedConfigurationBuilders().get(configuration.get(Argument.LISTENER_CACHE_NAME));
        if (builder == null) {
            throw new IllegalArgumentException("Cache " + configuration.get(Argument.CACHE_NAME) + " not defined!");
        }
        //needs to be enabled because it is accessed in embedded and remotely
        builder.compatibility().enable();

        EmbeddedCacheManager cacheManager = new DefaultCacheManager(holder, true);
        HotRodServerConfigurationBuilder serverConfigurationBuilder = new HotRodServerConfigurationBuilder();
        serverConfigurationBuilder
                .host(configuration.get(Argument.HOST))
                .port(configuration.getAsInt(Argument.PORT))
                .converterFactory("leads-converter-factory", new LeadsConverterFactory())
                .keyValueFilterFactory("leads-filter-factory", new LeadsKeyValueFilterFactory(cacheManager));
        HotRodServer server = new HotRodServer();
        server.start(serverConfigurationBuilder.build(), cacheManager);

        System.out.println("Server started! Press <enter> to exit!");
        System.console().readLine();

        server.stop();
        cacheManager.stop();

        System.exit(0);
    }


}
