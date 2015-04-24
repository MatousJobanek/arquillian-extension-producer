# arquillian-extension-producer
How to create something like CDI producer in arquillian extension

This arquillian-1.1.7.Final branch is an implementatin for arquillian 1.1.7.Final. 

In the next release of Arquillian (1.1.8.Final) there have been introduced two new annotations for the injection via ResourceProvider (for more informatin see https://issues.jboss.org/browse/ARQ-1921):
- @ResourceProvider.ClassInjection
- @ResourceProvider.MethodInjection

If you need an implementation for the 1.1.8.Final release, please see the [master branch](https://github.com/MatousJobanek/arquillian-extension-producer/)
