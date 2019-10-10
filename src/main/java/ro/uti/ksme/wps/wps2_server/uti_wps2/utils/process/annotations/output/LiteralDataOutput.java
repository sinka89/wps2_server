package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output;

import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.DescriptionTypeAttr;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.LiteralDataAttr;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.OutputAttr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LiteralDataAttr
public @interface LiteralDataOutput {
    OutputAttr outputAttr();

    DescriptionTypeAttr descriptionType();

    LiteralDataAttr literalAttr();
}
