package org.jboss.pruivo.leads.notification;

import org.infinispan.Cache;
import org.infinispan.filter.KeyValueFilter;
import org.infinispan.metadata.Metadata;

import java.util.UUID;

/**
 * Filters the keys.
 * <p/>
 * It checks if the line inserted/updated has the word "beer" or "free". If it has the word "beer", a counter is
 * incremented and stored in the defined cache. And, if the word "free" is present too, the event is sent to the
 * coordinator.
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class LeadsKeyValueFilter implements KeyValueFilter<Integer, String> {

    private final Cache<Object, Object> cache;
    private final String uuid;
    private int counter;

    public LeadsKeyValueFilter(Cache<Object, Object> cache) {
        this.cache = cache;
        this.uuid = UUID.randomUUID().toString();
        this.counter = 0;
    }

    @Override
    public boolean accept(Integer key, String value, Metadata metadata) {
        if (value.contains("beer")) {
            synchronized (this) {
                counter++;
                cache.put(uuid, counter);
            }
            return value.contains("free");
        }
        return false;
    }
}
