package arquillian.extension.producer.provider;

import java.lang.annotation.Annotation;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

import arquillian.extension.producer.Resources;
import arquillian.extension.producer.ResourcesImpl;

/**
 * Provides to test an instance of the class {@link ResourcesImpl}.
 * Injects new instance into every field or method parameter of the type {@link Resources} annotated with
 * {@link ArquillianResource}. It also distinguish injection points annotated with an qualifier {@link ToProvideSpecific}.
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ProviderProducer implements ResourceProvider {

    @Override
    public boolean canProvide(Class<?> type) {
        return type.equals(Resources.class);
    }

    @Override
    public Object lookup(ArquillianResource resource, Annotation... qualifiers) {

        // injection without a qualifier
        if (qualifiers.length == 0) {
            return new ResourcesImpl("provided without any qualifier");
        }

        // injection with some qualifier(s)
        for (Annotation a : qualifiers) {
            if (a.annotationType().equals(ToProvideSpecific.class)) {
                return new ResourcesImpl("provided with ToProvideSpecific qualifier");
            }
        }

        return null;
    }
}
