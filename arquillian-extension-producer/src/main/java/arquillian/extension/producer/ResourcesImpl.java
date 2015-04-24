package arquillian.extension.producer;

/**
 * An implementation of {@link Resources} interface which should be injected into a test class
 * 
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ResourcesImpl implements Resources {

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
