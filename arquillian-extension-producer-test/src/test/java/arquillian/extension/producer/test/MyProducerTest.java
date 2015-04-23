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

    @Test
    public void injectFieldsTest() {
        
        Assert.assertNotNull(enrichedImplementation);
        Assert.assertEquals("enriched into field", enrichedImplementation.getParameter());

        Assert.assertNotNull(providedImplementation);
        Assert.assertEquals("provided into field without any qualifier", providedImplementation.getParameter());

        Assert.assertNotNull(providedSpecificImplementation);
        Assert.assertEquals("provided into field with ToProvideSpecific qualifier",
            providedSpecificImplementation.getParameter());
    }

    @Test
    public void injectParamsTest(
        @ToEnrich Resources enrichedParam,
        @ArquillianResource Resources providedParam,
        @ArquillianResource @ToProvideSpecific Resources providedSpecificParam) {

        Assert.assertNotNull(enrichedParam);
        Assert.assertEquals("enriched into method", enrichedParam.getParameter());

        Assert.assertNotNull(providedParam);
        Assert.assertEquals("provided into method without any qualifier", providedParam.getParameter());

        Assert.assertNotNull(providedParam);
        Assert.assertEquals("provided into method with ToProvideSpecific qualifier", providedSpecificParam.getParameter());
    }
}
