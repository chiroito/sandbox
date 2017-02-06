package chiroito.rest;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class RestSampleTest {

    @Inject
    private RestSample target;

    @org.junit.Test
    public void getId() throws Exception {
        assertThat(this.target.getId(), is("abcdefg"));
    }

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(RestSample.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

}
