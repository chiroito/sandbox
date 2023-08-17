package com.example.injecttest.ia;

import com.example.injecttest.ab.InteractorMock;
import com.example.injecttest.ab.Interactor;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;


@ExtendWith(org.jboss.arquillian.junit5.ArquillianExtension.class)
public class InjectTest {
    @Deployment
    public static WebArchive createDeployment() throws IOException {
        return ShrinkWrap
                .create(WebArchive.class, "inject-test.war")
                .addClass(TestApplication.class)
                .addClass(HelloApi.class)
                .addClass(Interactor.class)
                .addClass(InteractorMock.class)
                .addClass(InteractorMockProducer.class);
    }

    @Inject
    HelloApi hello;

    @Test
    public void test(){
        assertEquals("Hello API from Mock", hello.handle());
    }
}
