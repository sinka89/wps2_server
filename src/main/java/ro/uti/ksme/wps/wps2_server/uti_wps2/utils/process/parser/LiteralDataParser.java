package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.parser;

import ro.uti.ksme.wps.wps2_server.pojo.ows._2.CodeType;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.InputDescriptionType;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.LiteralDataType;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.ObjectFactory;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.OutputDescriptionType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.DataType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.exceptions.MalformedModelException;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.LiteralDataAttr;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.LiteralDataInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.LiteralDataOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ObjectAnnotationConverter;

import javax.xml.bind.JAXBElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LiteralDataParser implements Parser {
    @Override
    public InputDescriptionType parseInput(Field f, Object defaultValue, Class cls) throws MalformedModelException {
        Process processAnnotation = (Process) cls.getAnnotation(Process.class);

        InputDescriptionType input = new InputDescriptionType();
        DataType dataType = getFieldDataType(f);
        if (dataType == null) {
            throw new MalformedModelException(LiteralDataInput.class, f.getName(), "Filed type not recognized");
        }
        LiteralDataType data = ObjectAnnotationConverter.annotationToObject(f.getAnnotation(LiteralDataInput.class).literalAttr(), dataType, null);
        JAXBElement<LiteralDataType> jaxbElement = new ObjectFactory().createLiteralData(data);
        input.setDataDescription(jaxbElement);

        ObjectAnnotationConverter.annotationToObject(f.getAnnotation(LiteralDataInput.class).inputAttr(), input);
        ObjectAnnotationConverter.annotationToObject(f.getAnnotation(LiteralDataInput.class).descriptionType(), input, processAnnotation.descriptionType().identifier());

        if (input.getIdentifier() == null) {
            CodeType codeType = new CodeType();
            codeType.setValue(processAnnotation.descriptionType().identifier() + ":" + f.getName());
            input.setIdentifier(codeType);
        }
        return input;
    }

    @Override
    public OutputDescriptionType parseOutput(Method method, Object defaultValue, Class cls) throws MalformedModelException {
        Process processAnnotation = (Process) cls.getAnnotation(Process.class);

        OutputDescriptionType output = new OutputDescriptionType();
        DataType dataType = getMethodDataType(method);
        if (dataType == null) {
            throw new MalformedModelException(LiteralDataOutput.class, method.getName(), "The method return type is not recognized.");
        }
        LiteralDataType data = ObjectAnnotationConverter.annotationToObject(method.getAnnotation(LiteralDataOutput.class).literalAttr(), dataType, null);
        JAXBElement<LiteralDataType> jaxbElement = new ObjectFactory().createLiteralData(data);
        output.setDataDescription(jaxbElement);
        //instantiate the return type
        ObjectAnnotationConverter.annotationToObject(method.getAnnotation(LiteralDataOutput.class).descriptionType(), output, processAnnotation.descriptionType().identifier());
        if (output.getIdentifier() == null) {
            CodeType codeType = new CodeType();
            codeType.setValue(processAnnotation.descriptionType().identifier() + ":" + method.getName());
            output.setIdentifier(codeType);
        }
        return output;
    }

    private DataType getFieldDataType(Field f) {
        DataType dataType = null;
        Class<?> type = f.getType();
        return getDataType(dataType, type);
    }

    private DataType getMethodDataType(Method m) {
        DataType dataType = null;
        Class<?> type = m.getReturnType();
        return getDataType(dataType, type);
    }

    private DataType getDataType(DataType dataType, Class type) {
        if (type.equals(int.class) || type.equals(Integer.class)) {
            dataType = DataType.INTEGER;
        } else if (type.equals(float.class) || type.equals(Float.class)) {
            dataType = DataType.FLOAT;
        } else if (type.equals(long.class) || type.equals(Long.class)) {
            dataType = DataType.LONG;
        } else if (type.equals(double.class) || type.equals(Double.class)) {
            dataType = DataType.DOUBLE;
        } else if (type.equals(char.class) || type.equals(Character.class)) {
            dataType = DataType.UNSIGNED_BYTE;
        } else if (type.equals(short.class) || type.equals(Short.class)) {
            dataType = DataType.SHORT;
        } else if (type.equals(byte.class) || type.equals(Byte.class)) {
            dataType = DataType.BYTE;
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            dataType = DataType.BOOLEAN;
        } else if (type.equals(String.class)) {
            dataType = DataType.STRING;
        }
        return dataType;
    }


    @Override
    public Class getAnnotation() {
        return LiteralDataAttr.class;
    }
}
