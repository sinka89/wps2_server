package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util;

import ro.uti.ksme.wps.wps2_server.custom_pojo_types.RawData;
import ro.uti.ksme.wps.wps2_server.pojo.ows._2.*;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.DescriptionType;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.DataType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.exceptions.MalformedModelException;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.*;

import java.math.BigInteger;
import java.util.*;

public class ObjectAnnotationConverter {

    public static LiteralDataType annotationToObject(LiteralDataAttr literalDataAttr, DataType dataType, Object defaultValueStr) throws MalformedModelException {
        LiteralDataType literalDataType = new LiteralDataType();
        List<Format> formatList = new ArrayList<>();
        formatList.add(FormatFactory.getFormatFromExtension(FormatFactory.TEXT_EXTENSION));
        formatList.get(0).setDefault(true);
        literalDataType.getFormat().addAll(formatList);

        List<LiteralDataType.LiteralDataDomain> literalDataDomains = new ArrayList<>();

        LiteralDataType.LiteralDataDomain dataDomain = createLiteralDataDomain(dataType, literalDataAttr.defaultDomain(), true);
        if (dataDomain == null) {
            throw new MalformedModelException(LiteralDataAttr.class, "validDomains", "the valid domains should be coma separated list of ranges with the pattern: min;;max / min;spacing;max or simple value");
        }
        if (defaultValueStr != null) {
            ValueType defaultValueType = new ValueType();
            defaultValueType.setValue(defaultValueStr.toString());
            dataDomain.setDefaultValue(defaultValueType);
        }
        literalDataDomains.add(dataDomain);

        if (literalDataAttr.validDomains().length != 0) {
            for (String validDomain : literalDataAttr.validDomains()) {
                dataDomain = createLiteralDataDomain(dataType, validDomain, false);
                if (dataDomain == null) {
                    throw new MalformedModelException(LiteralDataAttr.class, "validDomains", "the valid domains should be coma separated list of ranges with the pattern: min;;max / min;spacing;max or simple value");
                }
                if (defaultValueStr != null) {
                    ValueType defaultValueType = new ValueType();
                    defaultValueType.setValue(defaultValueStr.toString());
                    dataDomain.setDefaultValue(defaultValueType);
                }
                literalDataDomains.add(dataDomain);
            }
        }
        literalDataType.getLiteralDataDomain().addAll(literalDataDomains);
        return literalDataType;
    }

    public static void annotationToObject(InputAttr inputAttr, InputDescriptionType input) {
        input.setMaxOccurs("" + inputAttr.maxOccurs());
        input.setMinOccurs(BigInteger.valueOf(inputAttr.minOccurs()));
    }

    public static void annotationToObject(DescriptionTypeAttr descriptionTypeAttr, DescriptionType descriptionType, String processIdentifier) throws MalformedModelException {
        //set language
        List<LanguageStringType> titleList = new ArrayList<>();
        LanguageStringType str = new LanguageStringType();
        str.setValue(descriptionTypeAttr.title().trim());
        titleList.add(str);
        descriptionType.getTitle().addAll(titleList);

        //set description
        if (descriptionTypeAttr.description().isEmpty()) {
            List<LanguageStringType> descriptionList = new ArrayList<>();
            LanguageStringType r = new LanguageStringType();
            r.setValue(descriptionTypeAttr.description().trim());
            descriptionList.add(r);
            descriptionType.getAbstract().addAll(descriptionList);
        }

        //set identifier
        if (!descriptionTypeAttr.identifier().isEmpty()) {
            CodeType codeType = new CodeType();
            if (processIdentifier == null || processIdentifier.isEmpty()) {
                codeType.setValue(descriptionTypeAttr.identifier().trim());
            } else {
                codeType.setValue(processIdentifier.trim() + ":" + descriptionTypeAttr.identifier().trim());
            }
            descriptionType.setIdentifier(codeType);
        }

        //keywords
        String[] keywords = descriptionTypeAttr.keywords();
        LinkedList<KeywordsType> list = new LinkedList<>();
        for (String s : keywords) {
            LanguageStringType keywordStr = new LanguageStringType();
            keywordStr.setValue(s.trim());

            List<LanguageStringType> keywordList = new ArrayList<>();
            keywordList.add(keywordStr);
            KeywordsType keywordsType = new KeywordsType();
            keywordsType.getKeyword().addAll(keywordList);
            list.add(keywordsType);
        }
        descriptionType.getKeywords().addAll(list);

        //metadata
        String[] metadata = descriptionTypeAttr.metadata();
        if (metadata.length != 0) {
            if (metadata.length % 2 == 0) {
                List<MetadataType> metaList = new ArrayList<>();
                iterateMetadataArrayAndReturnList(metadata, metaList);
                descriptionType.getMetadata().addAll(metaList);
            } else {
                throw new MalformedModelException(DescriptionTypeAttr.class, "metadata", "The metadata is not composed of pair of Strings : prop -> value");
            }
        }
    }

    private static void iterateMetadataArrayAndReturnList(String[] metadata, List<MetadataType> metaList) {
        for (int i = 0; i < metadata.length; i += 2) {
            MetadataType meta = new MetadataType();
            meta.setRole(metadata[i]);
            meta.setTitle(metadata[i + 1]);
            metaList.add(meta);
        }
    }

    public static void annotationToObject(ProcessAttr processAttr, ProcessOffering processOffering) {
        processOffering.getProcess().setLang(Locale.forLanguageTag(processAttr.language()).toString());
        processOffering.setProcessVersion(processAttr.version());
        List<String> jobControlOptions = new ArrayList<>();
        for (JobControlOps jobControl : processAttr.jobControl()) {
            jobControlOptions.add(jobControl.getJobControlMode());
        }
        processOffering.getJobControlOptions().addAll(jobControlOptions);
        processOffering.getOutputTransmission().addAll(Arrays.asList(processAttr.dataTransmissionType()));
        String[] props = processAttr.properties();
        List<MetadataType> metadataList = processOffering.getProcess().getMetadata();
        iterateMetadataArrayAndReturnList(props, metadataList);
    }

    private static LiteralDataType.LiteralDataDomain createLiteralDataDomain(DataType dataType, String literalDataDomainStr, boolean isDefault) {
        LiteralDataType.LiteralDataDomain literalDataDomain = new LiteralDataType.LiteralDataDomain();
        literalDataDomain.setDefault(isDefault);
        DomainMetadataType domainMetadataType = new DomainMetadataType();
        domainMetadataType.setValue(dataType.name());
        domainMetadataType.setReference(dataType.getUri().toString());
        literalDataDomain.setDataType(domainMetadataType);
        //if no values allow any value
        if (literalDataDomainStr.isEmpty()) {
            literalDataDomain.setAnyValue(new AnyValue());
        } else {
            AllowedValues allowedValues = new AllowedValues();
            List<Object> valueOrRange = new ArrayList<>();
            String[] splitDom = literalDataDomainStr.split(",");
            for (String domain : splitDom) {
                Object allowedValue = createAllowedValue(domain);
                if (allowedValue == null) {
                    return null;
                }
                valueOrRange.add(allowedValue);
            }
            allowedValues.getValueOrRange().addAll(valueOrRange);
            literalDataDomain.setAllowedValues(allowedValues);
        }
        return literalDataDomain;
    }

    private static Object createAllowedValue(String allowedValue) {
        String allowedValuStr = allowedValue.trim();
        if (allowedValuStr.contains(";")) {
            String[] domainValues = allowedValuStr.split(";");
            //test for format (min;;max or min;spacing;max) if not properly formated throw exception
            if (domainValues[0].isEmpty() || domainValues[2].isEmpty()) {
                return null;
            }
            RangeType rangeType = new RangeType();
            ValueType minValue = new ValueType();
            minValue.setValue(domainValues[0]);
            rangeType.setMinimumValue(minValue);
            if (!domainValues[1].isEmpty()) {
                ValueType spacingValue = new ValueType();
                spacingValue.setValue(domainValues[1]);
                rangeType.setSpacing(spacingValue);
            }
            ValueType maxValue = new ValueType();
            maxValue.setValue(domainValues[2]);
            rangeType.setMaximumValue(maxValue);
            return rangeType;
        } else {
            ValueType value = new ValueType();
            value.setValue(allowedValuStr);
            return value;
        }
    }


    public static RawData annotationToObject(RawDataAttr rawDataAttr) {
        List<Format> formatList = FormatFactory.getFormatFromExtensions(rawDataAttr.fileTypes());
        RawData rawData = new RawData(formatList);
        rawData.setFile(rawDataAttr.isFile());
        rawData.setDirectory(rawDataAttr.isDirectory());
        rawData.setFileTypes(rawDataAttr.fileTypes());
        return rawData;
    }
}
