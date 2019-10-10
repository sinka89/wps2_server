package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input;

import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.BoundingBoxAttr;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.DescriptionTypeAttr;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.InputAttr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@BoundingBoxAttr
public @interface BoundingBoxInput {
    InputAttr inputAttr();

    DescriptionTypeAttr descriptionType();

    BoundingBoxAttr boundingBoxAttr();
}
