package org.jboss.pruivo.leads.notification;

import org.infinispan.filter.KeyValueFilter;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.server.hotrod.event.KeyValueFilterFactory;

/**
 * Constructs a {@link org.infinispan.filter.KeyValueFilter}.
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class LeadsKeyValueFilterFactory implements KeyValueFilterFactory {

    private final EmbeddedCacheManager cacheManager;

    public LeadsKeyValueFilterFactory(EmbeddedCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public KeyValueFilter<Integer, String> getKeyValueFilter(Object[] params) {
        if (params.length == 0) {
            throw new IllegalArgumentException();
        }
        String destinationCache = (String) params[0];

        return new LeadsKeyValueFilter(cacheManager.getCache(destinationCache));
    }
}
