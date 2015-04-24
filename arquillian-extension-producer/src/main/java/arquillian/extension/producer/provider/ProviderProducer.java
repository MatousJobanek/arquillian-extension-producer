package arquillian.extension.producer.provider;

import java.lang.annotation.Annotation;

import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

import arquillian.extension.producer.Resources;
import arquillian.extension.producer.ResourcesImpl;

/**
 * Provides to test class an instance of the class {@link ResourcesImpl}.
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

        boolean isClassInjection = false;
        boolean isMethodInjection = false;
        boolean hasSpecificQualifier = false;

        // Check for the presence of possible qualifiers
        for (Annotation a : qualifiers) {
            Class<? extends Annotation> annotationType = a.annotationType();

            if (annotationType.equals(ToProvideSpecific.class)) {
                hasSpecificQualifier = true;

            } else if (annotationType.equals(ResourceProvider.ClassInjection.class)) {
                isClassInjection = true;

            } else if (annotationType.equals(ResourceProvider.MethodInjection.class)) {
                isMethodInjection = true;
            }
        }

        if (isClassInjection) {

            if (hasSpecificQualifier) {
                // returns some specific implementation into the field injection point regarding to the used qualifier
                return new ResourcesImpl("provided into field with ToProvideSpecific qualifier");
            }

            // returns the default implementation into the field injection point
            return new ResourcesImpl("provided into field without any qualifier");

        } else if (isMethodInjection) {

            if (hasSpecificQualifier) {
                // returns some specific implementation into the method param injection point regarding to the used
                // qualifier
                return new ResourcesImpl("provided into method with ToProvideSpecific qualifier");
            }

            // returns the default implementation into the method param injection point
            return new ResourcesImpl("provided into method without any qualifier");
        }

        return null;
    }

}
