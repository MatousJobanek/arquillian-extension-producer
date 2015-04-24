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

import arquillian.extension.producer.Resources;
import arquillian.extension.producer.ResourcesImpl;

/**
 * Enriches a test with an instance of the {@link ResourcesImpl} - an implementation of {@link Resources}.
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
                setFieldValue(testCase, field, new ResourcesImpl("enriched into field"));
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
                resolution[i] = new ResourcesImpl("enriched into method");
            }
        }

        return resolution;
    }

    /**
     * Sets the field represented by Field object on the specified object argument to the specified new value. The new value is
     * automatically unwrapped if the underlying field has a primitive type.
     *
     * @param instance - the object whose field should be modified
     * @param field - the field that should be modified
     * @param value - the new value for the field of obj being modified
     */
    private void setFieldValue(final Object instance, final Field field, final Object value) {
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

    /**
     * Returns all the fields annotated with the given annotation
     *
     * @param source - the class where the fields should be examined in
     * @param annotationClass - the annotation the fields should be annotated with
     * @return list of found fields annotated with the given annotation
     */
    private List<Field> getFieldsWithAnnotation(final Class<?> source, final Class<? extends Annotation> annotationClass) {
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

    /**
     * Returns all the parameters annotated with the given annotation for the specified method
     *
     * @param method - the method where the parameters should be examined
     * @param annotationClass - the annotation the parameters should be annotated with
     * @return list of found parameters annotated with the given annotation
     */
    private List<Class<?>> getParametersWithAnnotation(final Method method,
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

    /**
     * Checks if some annotation is present an the given array of annotations
     *
     * @param annotations - array of annotations
     * @param needle - annotation we are looking for
     * @return if the annotation is present an the given array of annotations
     */
    private boolean isAnnotationPresent(final Annotation[] annotations, final Class<? extends Annotation> needle) {
        return findAnnotation(annotations, needle) != null;
    }

    /**
     * Finds some annotation in the given array of annotations
     *
     * @param annotations - array of annotations
     * @param needle - annotation we are looking for
     * @return the found annotation
     */
    @SuppressWarnings("unchecked")
    private <T extends Annotation> T findAnnotation(final Annotation[] annotations, final Class<T> needle) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == needle) {
                return (T) annotation;
            }
        }
        return null;
    }
}