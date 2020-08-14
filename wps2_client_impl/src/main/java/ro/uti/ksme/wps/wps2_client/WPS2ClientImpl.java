package ro.uti.ksme.wps.wps2_client;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2.pojo.ows._2.CodeType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.*;
import ro.uti.ksme.wps.wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2_client.connection_util.HttpClientResponse;
import ro.uti.ksme.wps.wps2_client.request.ExecuteProcessRequest;
import ro.uti.ksme.wps.wps2_client.request.WPS2ExecuteProcessRequest;
import ro.uti.ksme.wps.wps2_client.response.WPS2DescribeProcessResponse;
import ro.uti.ksme.wps.wps2_client.response.WPS2ExecuteProcessResponse;
import ro.uti.ksme.wps.wps2_client.response.WPS2GetCapabilitiesResponse;
import ro.uti.ksme.wps.wps2_client.response.WPS2StatusInfoResponse;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.net.URL;

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
    public WPS2GetCapabilitiesResponse getCapabilities() {
        GetCapabilitiesType getCapabilitiesType = new GetCapabilitiesType();
        HttpClientResponse post = null;
        WPS2GetCapabilitiesResponse result = null;
        long timeStart = 0;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ObjectFactory wps2Factory = new ObjectFactory();
            JAXBElement<GetCapabilitiesType> getCapabilities = wps2Factory.createGetCapabilities(getCapabilitiesType);
            Marshaller marshaller = JaxbContainer.INSTANCE.getMarshallerWithPrefixMapper();
            marshaller.marshal(getCapabilities, out);
            out.flush();
            if (LOGGER.isDebugEnabled()) {
                timeStart = System.currentTimeMillis();
            }
            post = httpClient.post(serverUrl, out, "text/xml");
            if (post.getContentType().matches(".*/xml.*")) {
                result = new WPS2GetCapabilitiesResponse(post);
            }
        } catch (JAXBException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            if (post != null) {
                post.dismiss();
            }
        }
        if (LOGGER.isDebugEnabled() && timeStart != 0) {
            LOGGER.debug("The GetCapabilitiesRequest lasted: " + (System.currentTimeMillis() - timeStart) + " ms");
        }
        return result;
    }

    @Override
    public WPS2DescribeProcessResponse describeProcess(String identifier, String language) {
        if (identifier == null || identifier.trim().length() == 0) {
            throw new NullPointerException("NoIdentifierProvided");
        }
        WPS2DescribeProcessResponse result = null;
        ObjectFactory wps2Factory = new ObjectFactory();
        DescribeProcess describeProcess = wps2Factory.createDescribeProcess();
        CodeType codeType = new CodeType();
        codeType.setValue(identifier);
        describeProcess.getIdentifier().add(codeType);
        if (language != null && language.trim().length() > 0) {
            describeProcess.setLang(language);
        }
        HttpClientResponse post = null;
        long timeStart = 0;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Marshaller marshaller = JaxbContainer.INSTANCE.getMarshallerWithPrefixMapper();
            marshaller.marshal(describeProcess, out);
            out.flush();
            if (LOGGER.isDebugEnabled()) {
                timeStart = System.currentTimeMillis();
            }
            post = httpClient.post(serverUrl, out, "text/xml");
            if (post.getContentType().matches(".*/xml.*")) {
                result = new WPS2DescribeProcessResponse(post);
            }
        } catch (JAXBException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            if (post != null) {
                post.dismiss();
            }
        }
        if (LOGGER.isDebugEnabled() && timeStart != 0) {
            LOGGER.debug("The DescribeProcessRequest lasted: " + (System.currentTimeMillis() - timeStart) + " ms");
        }
        return result;
    }

    @Override
    public WPS2StatusInfoResponse getStatusInfoForProcess(String identifier) {
        if (identifier == null || identifier.trim().length() == 0) {
            throw new NullPointerException("NoIdentifierProvided");
        }
        WPS2StatusInfoResponse result = null;
        ObjectFactory wps2Factory = new ObjectFactory();
        GetStatus getStatus = wps2Factory.createGetStatus();
        getStatus.setJobID(identifier);
        HttpClientResponse post = null;
        long timeStart = 0;
        //noinspection Duplicates
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Marshaller marshaller = JaxbContainer.INSTANCE.getMarshallerWithPrefixMapper();
            marshaller.marshal(getStatus, out);
            out.flush();
            if (LOGGER.isDebugEnabled()) {
                timeStart = System.currentTimeMillis();
            }
            post = httpClient.post(serverUrl, out, "text/xml");
            if (post.getContentType().matches(".*/xml.*")) {
                result = new WPS2StatusInfoResponse(post);
            }
        } catch (JAXBException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            if (post != null) {
                post.dismiss();
            }
        }
        if (LOGGER.isDebugEnabled() && timeStart != 0) {
            LOGGER.debug("The GetStatusRequest for processId: " + identifier + " lasted: " + (System.currentTimeMillis() - timeStart) + " ms");
        }
        return result;
    }

    @Override
    public WPS2StatusInfoResponse dismissProcess(String identifier) {
        if (identifier == null || identifier.trim().length() == 0) {
            throw new NullPointerException("NoIdentifierProvided");
        }
        WPS2StatusInfoResponse result = null;
        ObjectFactory wps2Factory = new ObjectFactory();
        Dismiss dismiss = wps2Factory.createDismiss();
        dismiss.setJobID(identifier);
        HttpClientResponse post = null;
        long timeStart = 0;
        //noinspection Duplicates
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Marshaller marshaller = JaxbContainer.INSTANCE.getMarshallerWithPrefixMapper();
            marshaller.marshal(dismiss, out);
            out.flush();
            if (LOGGER.isDebugEnabled()) {
                timeStart = System.currentTimeMillis();
            }
            post = httpClient.post(serverUrl, out, "text/xml");
            if (post.getContentType().matches(".*/xml.*")) {
                result = new WPS2StatusInfoResponse(post);
            }
        } catch (JAXBException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            if (post != null) {
                post.dismiss();
            }
        }
        if (LOGGER.isDebugEnabled() && timeStart != 0) {
            LOGGER.debug("The DismissRequest for processId: " + identifier + " lasted: " + (System.currentTimeMillis() - timeStart) + " ms");
        }
        return result;
    }

    @Override
    public ExecuteProcessRequest createExecuteProcessRequest() {
        return new WPS2ExecuteProcessRequest(serverUrl);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public WPS2ExecuteProcessResponse executeProcess(ExecuteProcessRequest executeRequest) {
        ExecuteRequestType executeRequestType = executeRequest.createExecuteRequestType();
        ObjectFactory wps2Factory = new ObjectFactory();
        JAXBElement<ExecuteRequestType> execute = wps2Factory.createExecute(executeRequestType);
        HttpClientResponse post = null;
        WPS2ExecuteProcessResponse result = null;
        long timeStart = 0;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Marshaller marshaller = JaxbContainer.INSTANCE.getMarshallerWithPrefixMapper();
            marshaller.marshal(execute, out);
            out.flush();
            if (LOGGER.isDebugEnabled()) {
                timeStart = System.currentTimeMillis();
            }
            post = httpClient.post(serverUrl, out, "text/xml");
            result = new WPS2ExecuteProcessResponse(post);
        } catch (JAXBException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        if (LOGGER.isDebugEnabled() && timeStart != 0) {
            LOGGER.debug("The ExecuteProcessRequest lasted: " + (System.currentTimeMillis() - timeStart) + " ms");
        }
        return result;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public WPS2ExecuteProcessResponse getProcessResult(String identifier) {
        if (identifier == null || identifier.trim().length() == 0) {
            throw new NullPointerException("NoIdentifierProvided");
        }
        WPS2ExecuteProcessResponse result = null;
        ObjectFactory wps2Factory = new ObjectFactory();
        GetResult getResult = wps2Factory.createGetResult();
        getResult.setJobID(identifier);
        HttpClientResponse post = null;
        long timeStart = 0;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Marshaller marshaller = JaxbContainer.INSTANCE.getMarshallerWithPrefixMapper();
            marshaller.marshal(getResult, out);
            out.flush();
            if (LOGGER.isDebugEnabled()) {
                timeStart = System.currentTimeMillis();
            }
            post = httpClient.post(serverUrl, out, "text/xml");
            result = new WPS2ExecuteProcessResponse(post);
        } catch (JAXBException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        if (LOGGER.isDebugEnabled() && timeStart != 0) {
            LOGGER.debug("The GetProcessResultRequest lasted: " + (System.currentTimeMillis() - timeStart) + " ms");
        }
        return result;
    }
}
