package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.BoundingBoxInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.LiteralDataInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.RawDataInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.BoundingBoxOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.LiteralDataOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.RawDataOutput;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;

public abstract class ProcessExecutionHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessExecutionHelper.class);

    public static <T> T createProcessAndExecute(ProcessDescriptionType process, Class<T> clazz, Map<URI, Object> dataMap) {
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
                            } else if (data != null && data.equals("true") || data.equals("false")) {
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
                    if (method != null) {
                        if (method.getName().equals("execute")) {
                            Object result = method.invoke(t);
                            dataMap.put(URI.create(o.getIdentifier().getValue()), result);
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("ERROR >>>> Reflection... cannot inject props on class with name: " + clazz.getSimpleName() + " cause:\n" + e.getMessage(), e);
            }
        }
        return t;
    }
}
