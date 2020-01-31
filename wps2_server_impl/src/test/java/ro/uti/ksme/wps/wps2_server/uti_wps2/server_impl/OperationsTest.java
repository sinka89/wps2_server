package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ro.uti.ksme.wps.WpsServer;
import ro.uti.ksme.wps.wps2.pojo.wps._2.GetCapabilitiesType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.WPSCapabilitiesType;
import ro.uti.ksme.wps.wps2.utils.JaxbContainer;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

public class OperationsTest {

    private WpsServer wpsServer;

    @Before
    public void init() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring_app_context.xml");
        wpsServer = applicationContext.getBean("wps2ServerImpl", WpsServer.class);
//        Weld weld = new Weld();
//        WeldContainer container = weld.initialize();
//        wpsServer = container.select(WpsServer.class).get();
//        container.shutdown();
    }

    @Test
    public void getCapabilities() throws JAXBException, URISyntaxException {
        Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
        File getCapFile = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("./xml-examples/wpsGetCapabilitiesRequestExample.xml")).toURI()).toFile();
        Object obj = unmarshaller.unmarshal(getCapFile);
        Assert.assertTrue(obj instanceof JAXBElement);
        Assert.assertTrue(((JAXBElement) obj).getValue() instanceof GetCapabilitiesType);
        Marshaller marshaller = JaxbContainer.INSTANCE.getMarshallerWithPrefixMapper();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        marshaller.marshal(obj, out);
        InputStream in = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        Object result = wpsServer.callOperation(in, "/wps", "POST");
        if (result instanceof StringWriter) {
            InputStream resultXml = new ByteArrayInputStream(result.toString().getBytes());
            Object resultObj = unmarshaller.unmarshal(resultXml);
            Assert.assertNotNull("Result is null from marshall", resultObj);
            Assert.assertTrue(resultObj instanceof JAXBElement);
            Assert.assertTrue(((JAXBElement) resultObj).getValue() instanceof WPSCapabilitiesType);
        } else {
            Assert.fail();
        }
    }
}
