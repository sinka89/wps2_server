package ro.uti.ksme.wps.wps2_client;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2.pojo.ows._2.CodeType;
import ro.uti.ksme.wps.wps2.pojo.ows._2.ExceptionReport;
import ro.uti.ksme.wps.wps2.pojo.wps._2.*;
import ro.uti.ksme.wps.wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2_client.connection_util.HttpClientResponse;
import ro.uti.ksme.wps.wps2_client.exception.Wps2ServerException;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/25/2019
 * Time: 3:07 PM
 */
public class WPS2ClientImpl extends AbstractWps2Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(WPS2ClientImpl.class);

    public WPS2ClientImpl(URL serverUrl) {
        super(serverUrl);
    }

    public WPS2ClientImpl(URL serverUrl, int requestTimeout) {
        super(serverUrl, requestTimeout);
    }

    @Override
    public Optional<WPSCapabilitiesType> getCapabilities() {
        Optional<WPSCapabilitiesType> result = Optional.empty();
        GetCapabilitiesType getCapabilitiesType = new GetCapabilitiesType();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ObjectFactory wps2Factory = new ObjectFactory();
            JAXBElement<GetCapabilitiesType> getCapabilities = wps2Factory.createGetCapabilities(getCapabilitiesType);
            Marshaller marshaller = JaxbContainer.INSTANCE.jaxbContext.createMarshaller();
            marshaller.marshal(getCapabilities, out);
            out.flush();
            HttpClientResponse post = httpClient.post(serverUrl, out.toInputStream(), "text/xml");
            if (post.getContentType().equals("application/xml")) {
                try {
                    Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
                    Object obj = unmarshaller.unmarshal(post.getResponseInputStream());
                    if (obj instanceof JAXBElement) {
                        JAXBElement jaxbElement = (JAXBElement) obj;
                        if (jaxbElement.getValue() != null && jaxbElement.getValue() instanceof WPSCapabilitiesType) {
                            result = Optional.of((WPSCapabilitiesType) jaxbElement.getValue());
                        }
                    } else {
                        if (obj instanceof ExceptionReport) {
                            post.dismiss();
                            throw new Wps2ServerException("WpsServerGetCapabilitiesError", (ExceptionReport) obj);
                        }
                    }
                } catch (JAXBException ex) {
                    LOGGER.error(">>>> ERROR unmarshalling response from wps. Cause:\n" + ex.getMessage(), ex);
                }
            }
            post.dismiss();
        } catch (JAXBException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Optional<ProcessOfferings> describeProcess(String identifier, String language) {
        if (identifier == null || identifier.trim().length() == 0) {
            throw new NullPointerException("NoIdentifierProvided");
        }
        Optional<ProcessOfferings> result = Optional.empty();
        ObjectFactory wps2Factory = new ObjectFactory();
        DescribeProcess describeProcess = wps2Factory.createDescribeProcess();
        CodeType codeType = new CodeType();
        codeType.setValue(identifier);
        describeProcess.getIdentifier().add(codeType);
        if (language != null && language.trim().length() > 0) {
            describeProcess.setLang(language);
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Marshaller marshaller = JaxbContainer.INSTANCE.jaxbContext.createMarshaller();
            marshaller.marshal(describeProcess, out);
            HttpClientResponse post = httpClient.post(serverUrl, out.toInputStream(), "text/xml");
            if (post.getContentType().equals("application/xml")) {
                try {
                    Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
                    Object obj = unmarshaller.unmarshal(post.getResponseInputStream());
                    if (obj instanceof ProcessOfferings) {
                        result = Optional.of((ProcessOfferings) obj);
                    } else if (obj instanceof ExceptionReport) {
                        post.dismiss();
                        throw new Wps2ServerException("WpsServerDescribeProcessError", (ExceptionReport) obj);
                    }
                } catch (JAXBException ex) {
                    LOGGER.error(">>>> ERROR unmarshalling response from wps. Cause:\n" + ex.getMessage(), ex);
                }
                post.dismiss();
            }
        } catch (JAXBException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Optional<StatusInfo> getStatusInfoForProcess(String identifier) {
        if (identifier == null || identifier.trim().length() == 0) {
            throw new NullPointerException("NoIdentifierProvided");
        }
        Optional<StatusInfo> result = Optional.empty();
        ObjectFactory wps2Factory = new ObjectFactory();
        GetStatus getStatus = wps2Factory.createGetStatus();
        getStatus.setJobID(identifier);
        //noinspection Duplicates
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Marshaller marshaller = JaxbContainer.INSTANCE.jaxbContext.createMarshaller();
            marshaller.marshal(getStatus, out);
            HttpClientResponse post = httpClient.post(serverUrl, out.toInputStream(), "text/xml");
            if (post.getContentType().equals("application/xml")) {
                try {
                    Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
                    Object obj = unmarshaller.unmarshal(post.getResponseInputStream());
                    if (obj instanceof StatusInfo) {
                        result = Optional.of((StatusInfo) obj);
                    } else if (obj instanceof ExceptionReport) {
                        post.dismiss();
                        throw new Wps2ServerException("WpsServerGetStatusError", (ExceptionReport) obj);
                    }
                } catch (JAXBException ex) {
                    LOGGER.error(">>>> ERROR unmarshalling response from wps. Cause:\n" + ex.getMessage(), ex);
                }
            }
            post.dismiss();
        } catch (JAXBException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Optional<StatusInfo> dismissProcess(String identifier) {
        if (identifier == null || identifier.trim().length() == 0) {
            throw new NullPointerException("NoIdentifierProvided");
        }
        Optional<StatusInfo> result = Optional.empty();
        ObjectFactory wps2Factory = new ObjectFactory();
        Dismiss dismiss = wps2Factory.createDismiss();
        dismiss.setJobID(identifier);
        //noinspection Duplicates
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Marshaller marshaller = JaxbContainer.INSTANCE.jaxbContext.createMarshaller();
            marshaller.marshal(dismiss, out);
            HttpClientResponse post = httpClient.post(serverUrl, out.toInputStream(), "text/xml");
            if (post.getContentType().equals("application/xml")) {
                try {
                    Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
                    Object obj = unmarshaller.unmarshal(post.getResponseInputStream());
                    if (obj instanceof StatusInfo) {
                        result = Optional.of((StatusInfo) obj);
                    } else if (obj instanceof ExceptionReport) {
                        post.dismiss();
                        throw new Wps2ServerException("WpsServerGetStatusError", (ExceptionReport) obj);
                    }
                } catch (JAXBException ex) {
                    LOGGER.error(">>>> ERROR unmarshalling response from wps. Cause:\n" + ex.getMessage(), ex);
                }

            }
            post.dismiss();
        } catch (JAXBException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }
}
