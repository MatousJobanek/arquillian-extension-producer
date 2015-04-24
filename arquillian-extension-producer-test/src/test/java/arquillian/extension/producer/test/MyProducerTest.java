package arquillian.extension.producer.test;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import arquillian.extension.producer.Resources;
import arquillian.extension.producer.enricher.ToEnrich;
import arquillian.extension.producer.provider.ToProvideSpecific;

@RunWith(Arquillian.class)
@RunAsClient
public class MyProducerTest {

    @ToEnrich
    private Resources enrichedImplementation;

    @ArquillianResource
    private Resources providedImplementation;

    @ArquillianResource
    @ToProvideSpecific
    private Resources providedSpecificImplementation;

    /**
     * Checks if all new instances of {@link Resources} have been injected info the fields
     */
    @Test
    public void injectFieldsTest() {

        // should be injected an instance for field from the EnricherProducer class
        Assert.assertNotNull(enrichedImplementation);
        Assert.assertEquals("enriched into field", enrichedImplementation.getParameter());

        // should be injected an instance from the ProviderProducer class - the default one
        Assert.assertNotNull(providedImplementation);
        Assert.assertEquals("provided without any qualifier", providedImplementation.getParameter());

        // should be injected an instance from the ProviderProducer class - the specific one
        Assert.assertNotNull(providedSpecificImplementation);
        Assert.assertEquals("provided with ToProvideSpecific qualifier", providedSpecificImplementation.getParameter());
    }

    /**
     * Checks if all new instances of {@link Resources} have been injected info the method parameters
     */
    @Test
    public void injectParamsTest(
        @ToEnrich Resources enrichedParam,
        @ArquillianResource Resources providedParam,
        @ArquillianResource @ToProvideSpecific Resources providedSpecificParam) {

        // should be injected an instance for method parameter from the EnricherProducer class
        Assert.assertNotNull(enrichedParam);
        Assert.assertEquals("enriched into method", enrichedParam.getParameter());

        // should be injected an instance from the ProviderProducer class - the default one
        Assert.assertNotNull(providedParam);
        Assert.assertEquals("provided without any qualifier", providedParam.getParameter());

        // should be injected an instance from the ProviderProducer class - the specific one
        Assert.assertNotNull(providedParam);
        Assert.assertEquals("provided with ToProvideSpecific qualifier", providedSpecificParam.getParameter());
    }
}
