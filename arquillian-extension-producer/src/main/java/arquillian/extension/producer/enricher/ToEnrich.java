package arquillian.extension.producer.enricher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import arquillian.extension.producer.ResourcesImpl;

/**
 * Use this annotation to inject new instance of {@link ResourcesImpl}
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface ToEnrich {

}
