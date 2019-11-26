package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2.custom_pojo_types.RawData;
import ro.uti.ksme.wps.wps2.pojo.ows._2.BoundingBoxType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.*;
import ro.uti.ksme.wps.wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.BoundingBoxInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.LiteralDataInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.RawDataInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.BoundingBoxOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.LiteralDataOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.RawDataOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.exception.ProcessingException;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;

/**
 * @author Bogdan-Adrian Sincu
 * Class that is not instantiable
 * For a specific process class tries to create an instance populate declared fields and executes the method "execute()"
 * the data resulted is set inside the dataMap.
 * Only for single result (probably extend for multiple results for one process??)
 * <p>
 * the exceptions caught are rethrown to trigger process state FAILED
 */
public abstract class ProcessExecutionHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessExecutionHelper.class);

    public static <T> T createProcess(ProcessDescriptionType process, Class<T> clazz, Map<URI, Object> dataMap) {
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            LOGGER.error("ERROR >>>> Cannot instantiate class " + clazz.getSimpleName() + "for creating process execution, cause:\n" + e.getMessage(), e);
        }
        if (t != null) {
            try {
                for (InputDescriptionType i : process.getInput()) {
                    Field field = null;
                    for (Field f : clazz.getDeclaredFields()) {
                        for (Annotation a : f.getDeclaredAnnotations()) {
                            //todo: optimize atm check for each annotation type... îch
                            if (a instanceof LiteralDataInput) {
                                String id = ((LiteralDataInput) a).descriptionType().identifier();
                                String processId = process.getIdentifier().getValue();
                                String inputId = i.getIdentifier().getValue();
                                if (inputId.equals(processId + ":" + id) || inputId.equals(id) || inputId.equals(processId + ":" + f.getName())) {
                                    field = f;
                                }
                            } else if (a instanceof BoundingBoxInput) {
                                String id = ((BoundingBoxInput) a).descriptionType().identifier();
                                String processId = process.getIdentifier().getValue();
                                String inputId = i.getIdentifier().getValue();
                                if (inputId.equals(processId + ":" + id) || inputId.equals(id) || inputId.equals(processId + ":" + f.getName())) {
                                    field = f;
                                }
                            } else if (a instanceof RawDataInput) {
                                String id = ((RawDataInput) a).descriptionType().identifier();
                                String processId = process.getIdentifier().getValue();
                                String inputId = i.getIdentifier().getValue();
                                if (inputId.equals(processId + ":" + id) || inputId.equals(id) || inputId.equals(processId + ":" + f.getName())) {
                                    field = f;
                                }
                            }
                        }
                    }
                    if (field != null) {
                        field.setAccessible(true);
                        Object data = dataMap.get(URI.create(i.getIdentifier().getValue()));
                        DataDescriptionType dataDescriptionType = i.getDataDescription().getValue();
                        if (dataDescriptionType instanceof LiteralDataType) {
                            if (Number.class.isAssignableFrom(field.getType()) && data != null) {
                                try {
                                    Method valueOf = field.getType().getMethod("valueOf", String.class);
                                    if (valueOf != null) {
                                        valueOf.setAccessible(true);
                                        data = valueOf.invoke(t, data.toString());
                                    }
                                } catch (NoSuchMethodException | InvocationTargetException e) {
                                    LOGGER.error("Unable to convert the literaldata to the good obj type. cause:\n" + e.getMessage(), e);
                                }
                            } else if (data != null && (data.equals("true") || data.equals("false"))) {
                                data = data.equals("true");
                            }
                        }
                        field.set(t, data);
                    }
                }
                for (OutputDescriptionType o : process.getOutput()) {
                    Method method = null;
                    for (Method m : clazz.getDeclaredMethods()) {
                        for (Annotation a : m.getDeclaredAnnotations()) {
                            if (a instanceof LiteralDataOutput) {
                                String id = ((LiteralDataOutput) a).descriptionType().identifier();
                                String processId = process.getIdentifier().getValue();
                                String outputId = o.getIdentifier().getValue();
                                if (outputId.equals(processId + ":" + id) || outputId.equals(id) || outputId.equals(processId + ":" + m.getName())) {
                                    method = m;
                                }
                            } else if (a instanceof BoundingBoxOutput) {
                                String id = ((BoundingBoxOutput) a).descriptionType().identifier();
                                String processId = process.getIdentifier().getValue();
                                String outputId = o.getIdentifier().getValue();
                                if (outputId.equals(processId + ":" + id) || outputId.equals(id) || outputId.equals(processId + ":" + m.getName())) {
                                    method = m;
                                }
                            } else if (a instanceof RawDataOutput) {
                                String id = ((RawDataOutput) a).descriptionType().identifier();
                                String processId = process.getIdentifier().getValue();
                                String outputId = o.getIdentifier().getValue();
                                if (outputId.equals(processId + ":" + id) || outputId.equals(id) || outputId.equals(processId + ":" + m.getName())) {
                                    method = m;
                                }
                            }
                        }
                    }
                    boolean methodFound = false;
                    if (method != null) {
                        if (method.getName().equals("execute")) {
                            methodFound = true;
                        }
                    }
                    if (!methodFound) {
                        throw new ProcessingException("ERROR >>>> Reflection... cannot find execute() method on declared process class");
                    }
                }
            } catch (Exception e) {
                //catch and rethrow to show exception and mark async failed
                LOGGER.error("ERROR >>>> Reflection... cannot inject props on class with name: " + clazz.getSimpleName() + " cause:\n" + e.getMessage(), e);
                throw new ProcessingException(e);
            }
        }
        return t;
    }

    public static void executeAndGetResult(ProcessDescriptionType process, Object processObj, Map<URI, Object> dataMap) {
        try {
            Method m = processObj.getClass().getMethod("execute");
            for (OutputDescriptionType o : process.getOutput()) {
                boolean found = false;
                for (Annotation a : m.getDeclaredAnnotations()) {
                    if (a instanceof LiteralDataOutput) {
                        String id = ((LiteralDataOutput) a).descriptionType().identifier();
                        String processId = process.getIdentifier().getValue();
                        String outputId = o.getIdentifier().getValue();
                        if (outputId.equals(processId + ":" + id) || outputId.equals(id) || outputId.equals(processId + ":" + m.getName())) {
                            found = true;
                        }
                    } else if (a instanceof BoundingBoxOutput) {
                        String id = ((BoundingBoxOutput) a).descriptionType().identifier();
                        String processId = process.getIdentifier().getValue();
                        String outputId = o.getIdentifier().getValue();
                        if (outputId.equals(processId + ":" + id) || outputId.equals(id) || outputId.equals(processId + ":" + m.getName())) {
                            found = true;
                        }
                    } else if (a instanceof RawDataOutput) {
                        String id = ((RawDataOutput) a).descriptionType().identifier();
                        String processId = process.getIdentifier().getValue();
                        String outputId = o.getIdentifier().getValue();
                        if (outputId.equals(processId + ":" + id) || outputId.equals(id) || outputId.equals(processId + ":" + m.getName())) {
                            found = true;
                        }
                    }
                }
                if (found) {
                    Object result = processObj.getClass().getMethod("execute").invoke(processObj);
                    if (result instanceof BoundingBoxType) {
                        result = marshallBoundingBoxTypeResult(result);
                    }
                    dataMap.put(URI.create(o.getIdentifier().getValue()), result);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private static Object marshallBoundingBoxTypeResult(Object result) {
        BoundingBoxType bboxType = (BoundingBoxType) result;
        try (StringWriter strW = new StringWriter()) {
            BoundingBoxData bboxData = new BoundingBoxData();
            ro.uti.ksme.wps.wps2.pojo.ows._2.ObjectFactory owsFactory = new ro.uti.ksme.wps.wps2.pojo.ows._2.ObjectFactory();
            JAXBElement<BoundingBoxType> boundingBox = owsFactory.createBoundingBox(bboxType);
            bboxData.setBoundingBox(boundingBox);
            ro.uti.ksme.wps.wps2.pojo.wps._2.ObjectFactory wpsFactory = new ro.uti.ksme.wps.wps2.pojo.wps._2.ObjectFactory();
            JAXBElement<BoundingBoxData> boundingBoxData = wpsFactory.createBoundingBoxData(bboxData);
            Marshaller marshaller = JaxbContainer.INSTANCE.jaxbContext.createMarshaller();
            marshaller.marshal(boundingBoxData, strW);
            return strW.toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "Processing Result cannot be marshalled... check invocation method execute()";
    }

    public static Object marshallRawDataTypeResult(Object result) {
        RawData rawData = (RawData) result;
        try (StringWriter sw = new StringWriter()) {
            Marshaller marshaller = JaxbContainer.INSTANCE.jaxbContext.createMarshaller();
            marshaller.marshal(rawData, sw);
            return sw.toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "Processing Result cannot be marshalled... check invocation method execute()";
    }
}
