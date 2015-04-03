package arquillian.extension.producer.provider;

import java.lang.annotation.Annotation;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

import arquillian.extension.producer.ResourcesImpl;

/**
 * Provides to test an instance of the class {@link ResourcesImpl}
 * Injects new instance into every field or method parameter annotated with both {@link ToProvide} and {@link ArquillianResource}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ProviderProducer implements ResourceProvider{

    @Override
    public boolean canProvide(Class<?> type) {
        return type.equals(ResourcesImpl.class);
    }

    @Override
    public Object lookup(ArquillianResource resource, Annotation... qualifiers) {
        for (Annotation a : qualifiers) {
            if (a.annotationType().equals(ToProvide.class)) {
                return new ResourcesImpl("provided");
            }
        }
        return null;
    }
}
