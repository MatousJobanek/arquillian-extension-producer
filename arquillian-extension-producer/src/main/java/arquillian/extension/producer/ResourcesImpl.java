package arquillian.extension.producer;

/**
 * An implementation of some resources which should be injected into test class
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ResourcesImpl {

    private String parameter;

    public ResourcesImpl() {
    }

    public ResourcesImpl(String parameter) {
        super();
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        return "ResourcesImpl [parameter=" + parameter + "]";
    }

}
