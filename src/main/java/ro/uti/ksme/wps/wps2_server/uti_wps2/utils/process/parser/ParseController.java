package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.InputDescriptionType;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.OutputDescriptionType;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.ProcessDescriptionType;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.ProcessOffering;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.exceptions.MalformedModelException;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.InputAnnotation;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.OutputAnnotation;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Bogdan-Adrian Sincu
 * Main Parser that Generates ProcessIdentifier based on the java process classes
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ParseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseController.class);

    private List<Parser> parserList;
    private ProcessParser processParser;

    public ParseController() {
        parserList = new ArrayList<>();
        parserList.add(new LiteralDataParser());
        parserList.add(new BoundingBoxParser());
        parserList.add(new RawDataParser());
    }

    @Inject
    public void setProcessParser(ProcessParser processParser) {
        this.processParser = processParser;
    }

    public Optional<ProcessOffering> parseProcess(Class<?> cls) {
        ProcessOffering processOffering;
        try {
            processOffering = processParser.parseProcess(cls);
            setProcessOffering(processOffering, cls);
            return Optional.of(processOffering);
        } catch (Exception e) {
            LOGGER.error("Unable to generate ProcessOffering... for class " + cls.getSimpleName());
        }
        return Optional.empty();
    }

    private void setProcessOffering(ProcessOffering processOffering, Class cls) throws MalformedModelException {
        ProcessDescriptionType process = processOffering.getProcess();
        List<InputDescriptionType> inputList = new ArrayList<>();
        List<OutputDescriptionType> outputList = new ArrayList<>();
        Object obj = null;
        try {
            obj = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Unable to create instance for class " + cls.getSimpleName());
        }
        boolean annotationPresent = false;
        boolean parsed = false;
        for (Field f : cls.getDeclaredFields()) {
            f.setAccessible(true);
            for (Annotation a : f.getDeclaredAnnotations()) {
                if (a instanceof InputAnnotation) {
                    Object defaultValue = null;
                    if (obj != null) {
                        try {
                            f.setAccessible(true);
                            defaultValue = f.get(obj);
                        } catch (IllegalAccessException e) {
                            LOGGER.error("Unable to retrieve the default value of the field " + f.getName(), e);
                        }
                    }
                    for (Parser p : parserList) {
                        for (Annotation anotherAnnotation : f.getDeclaredAnnotations()) {
                            annotationPresent = anotherAnnotation.annotationType().isAnnotationPresent(p.getAnnotation());
                        }
                        if (annotationPresent) {
                            InputDescriptionType input = p.parseInput(f, defaultValue, cls);
                            if (input.getInput() != null && !input.getInput().isEmpty()) {
                                inputList.addAll(input.getInput());
                            } else {
                                inputList.add(input);
                            }
                            parsed = true;
                        }
                        annotationPresent = false;
                    }
                    if (!parsed) {
                        throw new MalformedModelException(ParseController.class, a.toString(), "Unable to find the parser for the annotation " + a.toString());
                    }
                }
                parsed = false;
            }
        }
        for (Method m : cls.getDeclaredMethods()) {
            if (m.isSynthetic()) {
                continue;
            }
            m.setAccessible(true);
            if (m.getName().equals("execute")) {
                for (Annotation a : m.getDeclaredAnnotations()) {
                    if (a instanceof OutputAnnotation) {
                        if (obj != null) {
                            m.setAccessible(true);
                            for (Parser p : parserList) {
                                for (Annotation anotherAnnotation : m.getDeclaredAnnotations()) {
                                    annotationPresent = anotherAnnotation.annotationType().isAnnotationPresent(p.getAnnotation());
                                }
                                if (annotationPresent) {
                                    OutputDescriptionType output = p.parseOutput(m, null, cls);
                                    if (output.getOutput() != null && !output.getOutput().isEmpty()) {
                                        outputList.addAll(output.getOutput());
                                    } else {
                                        outputList.add(output);
                                    }
                                    parsed = true;
                                }
                                annotationPresent = false;
                            }
                            if (!parsed) {
                                throw new MalformedModelException(ParseController.class, a.toString(), "Unable to find parser for annotation " + a.toString());
                            }
                        }
                    }
                    parsed = false;
                }
            }
        }
        process.getInput().addAll(inputList);
        process.getOutput().addAll(outputList);
    }
}
