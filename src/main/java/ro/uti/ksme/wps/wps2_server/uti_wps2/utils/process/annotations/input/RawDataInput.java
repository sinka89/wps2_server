package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input;

import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.DescriptionTypeAttr;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.InputAttr;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.RawDataAttr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Bogdan-Adrian Sincu created on 10/7/2019
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RawDataAttr
public @interface RawDataInput {
    InputAttr inputAttr();

    DescriptionTypeAttr descriptionType();

    RawDataAttr rawDataAttr();
}
