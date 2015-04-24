package arquillian.extension.producer;

/**
 * An interfaces representing some kind of resources
 * 
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface Resources {

    public String getParameter();
    
    public void setParameter(String parameter);
}
