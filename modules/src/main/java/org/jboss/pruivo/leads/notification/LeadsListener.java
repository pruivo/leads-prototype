package org.jboss.pruivo.leads.notification;

import org.infinispan.client.hotrod.annotation.ClientCacheEntryCreated;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryModified;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryRemoved;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.event.ClientCacheEntryCustomEvent;

/**
 * A listener that prints the line number when a text line with "free" "beer" is inserted or updated in the cache.
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
@ClientListener(
        converterFactoryName = "leads-converter-factory",
        filterFactoryName = "leads-filter-factory"
)
public class LeadsListener {

    @ClientCacheEntryCreated
    @ClientCacheEntryModified
    @ClientCacheEntryRemoved
    public void handleCustomEvent(ClientCacheEntryCustomEvent<LeadsEvent> event) {
        System.out.println("Free beer found in line " + event.getEventData().getLineNumber());
    }

}
