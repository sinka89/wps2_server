package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.parser;

import ro.uti.ksme.wps.wps2.custom_pojo_types.ObjectFactoryCustom;
import ro.uti.ksme.wps.wps2.custom_pojo_types.RawData;
import ro.uti.ksme.wps.wps2.pojo.ows._2.CodeType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.InputDescriptionType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.OutputDescriptionType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.exceptions.MalformedModelException;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.RawDataAttr;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.RawDataInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.RawDataOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ObjectAnnotationConverter;

import javax.xml.bind.JAXBElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Bogdan-Adrian Sincu created on 10/7/2019
 */
public class RawDataParser implements Parser {
    @Override
    public InputDescriptionType parseInput(Field f, Object defaultValue, Class cls) throws MalformedModelException {
        Process processAnnotation = (Process) cls.getAnnotation(Process.class);
        RawData rawData = ObjectAnnotationConverter.annotationToObject(f.getAnnotation(RawDataInput.class).rawDataAttr());
        InputDescriptionType input = new InputDescriptionType();
        JAXBElement<RawData> jaxbElement = new ObjectFactoryCustom().createRawData(rawData);
        input.setDataDescription(jaxbElement);
        ObjectAnnotationConverter.annotationToObject(f.getAnnotation(RawDataInput.class).inputAttr(), input);
        ObjectAnnotationConverter.annotationToObject(f.getAnnotation(RawDataInput.class).descriptionType(), input, processAnnotation.descriptionType().identifier());
        if (input.getIdentifier() == null) {
            CodeType codeType = new CodeType();
            codeType.setValue(processAnnotation.descriptionType().identifier() + "_" + f.getName());
            input.setIdentifier(codeType);
        }
        return input;
    }

    @Override
    public OutputDescriptionType parseOutput(Method method, Object defaultValue, Class cls) throws MalformedModelException {
        Process processAnnotation = (Process) cls.getAnnotation(Process.class);
        RawData rawData = ObjectAnnotationConverter.annotationToObject(method.getAnnotation(RawDataOutput.class).rawDataAttr());

        OutputDescriptionType output = new OutputDescriptionType();
        JAXBElement<RawData> jaxbElement = new ObjectFactoryCustom().createRawData(rawData);
        output.setDataDescription(jaxbElement);

        ObjectAnnotationConverter.annotationToObject(method.getAnnotation(RawDataOutput.class).descriptionType(), output, processAnnotation.descriptionType().identifier());
        if (output.getIdentifier() == null) {
            CodeType codeType = new CodeType();
            codeType.setValue(processAnnotation.descriptionType().identifier() + "_" + method.getName());
            output.setIdentifier(codeType);
        }
        return output;
    }

    @Override
    public Class getAnnotation() {
        return RawDataAttr.class;
    }
}
