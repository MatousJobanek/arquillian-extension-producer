package arquillian.extension.producer;

import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

import arquillian.extension.producer.enricher.EnricherProducer;
import arquillian.extension.producer.provider.ProviderProducer;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class RegisterExtension implements LoadableExtension{

    public void register(ExtensionBuilder builder) {
        builder.service(TestEnricher.class, EnricherProducer.class);
        builder.service(ResourceProvider.class, ProviderProducer.class);
    }
}
