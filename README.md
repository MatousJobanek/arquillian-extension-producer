# arquillian-extension-producer
How to create something like CDI producer in an arquillian extension.
There are two posibilities:
- implementing the [ResourceProvider](https://github.com/arquillian/arquillian-core/blob/master/test/spi/src/main/java/org/jboss/arquillian/test/spi/enricher/resource/ResourceProvider.java) interface and using the [@ArquillianResource](https://github.com/arquillian/arquillian-core/blob/master/test/api/src/main/java/org/jboss/arquillian/test/api/ArquillianResource.java) annotation in the test class - it can be combinated with some qualifier (probably what are you looking for)
  - see the example [ProviderProducer](https://github.com/MatousJobanek/arquillian-extension-producer/blob/master/arquillian-extension-producer/src/main/java/arquillian/extension/producer/provider/ProviderProducer.java)

- implementing the [TestEnricher](https://github.com/arquillian/arquillian-core/blob/master/test/spi/src/main/java/org/jboss/arquillian/test/spi/TestEnricher.java) interface and using your own annotation (this is more flexible but also more complicated solution)
  - see the example [EnricherProducer](https://github.com/MatousJobanek/arquillian-extension-producer/blob/master/arquillian-extension-producer/src/main/java/arquillian/extension/producer/enricher/EnricherProducer.java)
  
<b>NOTE:</b>
This arquillian-1.1.7.Final branch is an implementatin for arquillian 1.1.7.Final.</br> 
In the next release of Arquillian (1.1.8.Final) there have been introduced two new annotations for the injection via ResourceProvider (for more informatin see https://issues.jboss.org/browse/ARQ-1921):
- @ResourceProvider.ClassInjection
- @ResourceProvider.MethodInjection

If you need an implementation for the 1.1.8.Final release, please see the [master branch](https://github.com/MatousJobanek/arquillian-extension-producer/)
