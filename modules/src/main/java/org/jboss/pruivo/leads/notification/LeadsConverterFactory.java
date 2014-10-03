package org.jboss.pruivo.leads.notification;

import org.infinispan.filter.Converter;
import org.infinispan.filter.ConverterFactory;
import org.infinispan.filter.NamedFactory;
import org.infinispan.metadata.Metadata;

/**
 * Constructs the {@link org.infinispan.filter.Converter}.
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
@NamedFactory(name = "leads-converter-factory")
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
