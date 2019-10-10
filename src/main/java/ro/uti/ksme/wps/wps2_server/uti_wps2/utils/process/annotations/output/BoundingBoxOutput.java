package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output;

import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.BoundingBoxAttr;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.DescriptionTypeAttr;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.OutputAttr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@BoundingBoxAttr
public @interface BoundingBoxOutput {
    OutputAttr outputAttr();

    DescriptionTypeAttr descriptionType();

    BoundingBoxAttr boundingBoxAttr();
}
