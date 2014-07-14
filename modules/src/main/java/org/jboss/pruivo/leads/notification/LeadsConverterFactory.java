package org.jboss.pruivo.leads.notification;

import org.infinispan.filter.Converter;
import org.infinispan.metadata.Metadata;
import org.infinispan.server.hotrod.event.ConverterFactory;

/**
 * Constructs the {@link org.infinispan.filter.Converter}.
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class LeadsConverterFactory implements ConverterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public Converter<Integer, String, LeadsEvent> getConverter(Object[] params) {
        return new Converter<Integer, String, LeadsEvent>() {
            @Override
            public LeadsEvent convert(Integer key, String value, Metadata metadata) {
                return new LeadsEvent(key);
            }
        };
    }
}
