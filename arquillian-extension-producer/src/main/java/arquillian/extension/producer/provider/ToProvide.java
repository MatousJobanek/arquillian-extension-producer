package arquillian.extension.producer.provider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.arquillian.test.api.ArquillianResource;

import arquillian.extension.producer.ResourcesImpl;

/**
 * Use this annotation in combination with annotation {@link ArquillianResource} to inject new instance of {@link ResourcesImpl}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface ToProvide {

}
