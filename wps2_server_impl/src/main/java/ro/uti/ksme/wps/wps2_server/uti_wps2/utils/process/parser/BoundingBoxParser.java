package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.parser;

import ro.uti.ksme.wps.wps2.pojo.ows._2.BoundingBoxType;
import ro.uti.ksme.wps.wps2.pojo.ows._2.CodeType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.BoundingBoxData;
import ro.uti.ksme.wps.wps2.pojo.wps._2.InputDescriptionType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.OutputDescriptionType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.SupportedCRS;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.exceptions.MalformedModelException;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.BoundingBoxAttr;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.BoundingBoxInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.BoundingBoxOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.FormatFactory;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ObjectAnnotationConverter;

import javax.xml.bind.JAXBElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bogdan-Adrian Sincu created on 10/7/2019
 */
public class BoundingBoxParser implements Parser {
    @Override
    public InputDescriptionType parseInput(Field f, Object defaultValue, Class cls) throws MalformedModelException {
        Process processAnnotation = (Process) cls.getAnnotation(Process.class);
        BoundingBoxType bboxType = new BoundingBoxType();
        bboxType.setCrs(f.getAnnotation(BoundingBoxInput.class).boundingBoxAttr().defaultCRS());
        bboxType.setDimensions(BigInteger.valueOf(f.getAnnotation(BoundingBoxInput.class).boundingBoxAttr().dimension()));
        ro.uti.ksme.wps.wps2.pojo.ows._2.ObjectFactory owsFactory = new ro.uti.ksme.wps.wps2.pojo.ows._2.ObjectFactory();
        JAXBElement<BoundingBoxType> boundingBox = owsFactory.createBoundingBox(bboxType);

        BoundingBoxData bboxData = new BoundingBoxData();
        bboxData.setBoundingBox(boundingBox);
        String[] supportedCRss = f.getAnnotation(BoundingBoxInput.class).boundingBoxAttr().supportedCRSArray();
        List<SupportedCRS> supCRS = new ArrayList<>();
        for (String s : supportedCRss) {
            SupportedCRS crs = new SupportedCRS();
            crs.setValue(s);
            if (s.equals(f.getAnnotation(BoundingBoxInput.class).boundingBoxAttr().defaultCRS())) {
                crs.setDefault(true);
            }
            supCRS.add(crs);
        }
        bboxData.getSupportedCRS().addAll(supCRS);
        bboxData.getFormat().addAll(FormatFactory.getFormatFromExtensions(FormatFactory.XML_EXTENSION));
        ro.uti.ksme.wps.wps2.pojo.wps._2.ObjectFactory wpsFactory = new ro.uti.ksme.wps.wps2.pojo.wps._2.ObjectFactory();
        JAXBElement<BoundingBoxData> boundingBoxData = wpsFactory.createBoundingBoxData(bboxData);
        InputDescriptionType input = new InputDescriptionType();
        input.setDataDescription(boundingBoxData);
        ObjectAnnotationConverter.annotationToObject(f.getAnnotation(BoundingBoxInput.class).inputAttr(), input);
        ObjectAnnotationConverter.annotationToObject(f.getAnnotation(BoundingBoxInput.class).descriptionType(), input, processAnnotation.descriptionType().identifier());
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
        BoundingBoxType bboxType = new BoundingBoxType();
        bboxType.setDimensions(BigInteger.valueOf(method.getAnnotation(BoundingBoxOutput.class).boundingBoxAttr().dimension()));
        ro.uti.ksme.wps.wps2.pojo.ows._2.ObjectFactory owsFactory = new ro.uti.ksme.wps.wps2.pojo.ows._2.ObjectFactory();
        JAXBElement<BoundingBoxType> boundingBox = owsFactory.createBoundingBox(bboxType);
        BoundingBoxData bboxData = new BoundingBoxData();
        String[] supportedCRss = method.getAnnotation(BoundingBoxOutput.class).boundingBoxAttr().supportedCRSArray();
        List<SupportedCRS> supCRS = new ArrayList<>();
        for (String s : supportedCRss) {
            SupportedCRS crs = new SupportedCRS();
            crs.setValue(s);
            if (s.equals(method.getAnnotation(BoundingBoxOutput.class).boundingBoxAttr().defaultCRS())) {
                crs.setDefault(true);
            }
            supCRS.add(crs);
        }
        bboxData.getSupportedCRS().addAll(supCRS);
        bboxData.setBoundingBox(boundingBox);
        bboxData.getFormat().addAll(FormatFactory.getFormatFromExtensions(FormatFactory.XML_EXTENSION));
        ro.uti.ksme.wps.wps2.pojo.wps._2.ObjectFactory wpsFactory = new ro.uti.ksme.wps.wps2.pojo.wps._2.ObjectFactory();
        JAXBElement<BoundingBoxData> boundingBoxData = wpsFactory.createBoundingBoxData(bboxData);
        OutputDescriptionType output = new OutputDescriptionType();
        output.setDataDescription(boundingBoxData);
        ObjectAnnotationConverter.annotationToObject(method.getAnnotation(BoundingBoxOutput.class).descriptionType(), output, processAnnotation.descriptionType().identifier());
        if (output.getIdentifier() == null) {
            CodeType codeType = new CodeType();
            codeType.setValue(processAnnotation.descriptionType().identifier() + "_" + method.getName());
            output.setIdentifier(codeType);
        }
        return output;
    }

    @Override
    public Class getAnnotation() {
        return BoundingBoxAttr.class;
    }
}
