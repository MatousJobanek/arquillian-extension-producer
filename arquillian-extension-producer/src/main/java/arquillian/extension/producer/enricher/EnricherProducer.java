package arquillian.extension.producer.enricher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.test.spi.TestEnricher;

import arquillian.extension.producer.ResourcesImpl;

/**
 * Enriches test with an instance of the class {@link ResourcesImpl}.
 * Injects new instance into every field or method parameter of the type {@link Resources} annotated with {@link ToEnrich}
 * 
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class EnricherProducer implements TestEnricher {

    @Override
    public void enrich(Object testCase) {
        List<Field> fields = getFieldsWithAnnotation(testCase.getClass(), ToEnrich.class);
        for (Field field : fields) {
            try {
                setFieldValue(testCase, field, new ResourcesImpl("enriched in field"));
            } catch (Exception e) {
                throw new RuntimeException("Could not inject object on field " + field, e);
            }
        }
    }

    @Override
    public Object[] resolve(Method method) {
        List<Class<?>> parameters = getParametersWithAnnotation(method, ToEnrich.class);
        Object[] resolution = new Object[method.getParameterTypes().length];

        for (int i = 0; i < resolution.length; i++) {
            if (parameters.get(i) == null) {
                resolution[i] = null;
            } else {
                resolution[i] = new ResourcesImpl("enriched in method");
            }
        }

        return resolution;
    }

    static void setFieldValue(final Object instance, final Field field, final Object value) {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                public Void run() throws IllegalArgumentException, IllegalAccessException {
                    field.set(instance, value);
                    return null;
                }
            });
        } catch (PrivilegedActionException e) {
            final Throwable t = e.getCause();
            // Rethrow
            if (t instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) t;
            } else if (t instanceof IllegalAccessException) {
                throw new IllegalStateException("Unable to set field value of " + field.getName() + " due to: "
                    + t.getMessage(), t.getCause());
            } else {
                // No other checked Exception thrown by Class.getConstructor
                try {
                    throw (RuntimeException) t;
                }
                // Just in case we've really messed up
                catch (final ClassCastException cce) {
                    throw new RuntimeException("Obtained unchecked Exception; this code should never be reached", t);
                }
            }
        }
    }

    public static List<Field> getFieldsWithAnnotation(final Class<?> source, final Class<? extends Annotation> annotationClass) {
        List<Field> declaredAccessableFields = AccessController.doPrivileged(new PrivilegedAction<List<Field>>() {

            public List<Field> run() {
                List<Field> foundFields = new ArrayList<Field>();
                Class<?> nextSource = source;

                while (nextSource != Object.class) {
                    for (Field field : nextSource.getDeclaredFields()) {

                        if (field.isAnnotationPresent(annotationClass)) {
                            if (!field.isAccessible()) {
                                field.setAccessible(true);
                            }
                            foundFields.add(field);
                        }
                    }
                    nextSource = nextSource.getSuperclass();
                }
                return foundFields;
            }
        });
        return declaredAccessableFields;
    }

    static List<Class<?>> getParametersWithAnnotation(final Method method,
        final Class<? extends Annotation> annotationClass) {

        List<Class<?>> declaredParameters = AccessController
            .doPrivileged(new PrivilegedAction<List<Class<?>>>() {

                public List<Class<?>> run() {
                    List<Class<?>> foundParameters = new ArrayList<Class<?>>();

                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                    for (int i = 0; i < parameterTypes.length; i++) {

                        if (isAnnotationPresent(parameterAnnotations[i], annotationClass)) {
                            foundParameters.add(parameterTypes[i]);
                        } else {
                            foundParameters.add(null);
                        }
                    }
                    return foundParameters;
                }

            });
        return declaredParameters;
    }

    static boolean isAnnotationPresent(final Annotation[] annotations, final Class<? extends Annotation> needle) {
        return findAnnotation(annotations, needle) != null;
    }

    @SuppressWarnings("unchecked")
    static <T extends Annotation> T findAnnotation(final Annotation[] annotations, final Class<T> needle) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == needle) {
                return (T) annotation;
            }
        }
        return null;
    }
}