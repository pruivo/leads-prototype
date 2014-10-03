package org.jboss.pruivo.leads.notification;

import org.infinispan.filter.KeyValueFilter;
import org.infinispan.filter.KeyValueFilterFactory;
import org.infinispan.filter.NamedFactory;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * Constructs a {@link org.infinispan.filter.KeyValueFilter}.
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
@NamedFactory(name = "leads-filter-factory")
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
