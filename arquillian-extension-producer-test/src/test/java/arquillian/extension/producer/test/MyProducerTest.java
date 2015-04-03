package arquillian.extension.producer.test;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import arquillian.extension.producer.ResourcesImpl;
import arquillian.extension.producer.enricher.ToEnrich;
import arquillian.extension.producer.provider.ToProvide;

@RunWith(Arquillian.class)
@RunAsClient
public class MyProducerTest {

    @ToEnrich
    private ResourcesImpl enrichedImplementation;

    @ToProvide
    @ArquillianResource
    private ResourcesImpl providedImplementation;


    @Test
    public void injectFieldsTest() {
        Assert.assertNotNull(enrichedImplementation);
        Assert.assertEquals("enriched in field", enrichedImplementation.getParameter());

        Assert.assertNotNull(providedImplementation);
        Assert.assertEquals("provided", providedImplementation.getParameter());
    }

    @Test
    public void injectParamsTest(@ToEnrich ResourcesImpl enrichedParam, @ToProvide @ArquillianResource ResourcesImpl providedParam) {
        Assert.assertNotNull(enrichedParam);
        Assert.assertEquals("enriched in method", enrichedParam.getParameter());

        Assert.assertNotNull(providedParam);
        Assert.assertEquals("provided", providedParam.getParameter());
    }
}
