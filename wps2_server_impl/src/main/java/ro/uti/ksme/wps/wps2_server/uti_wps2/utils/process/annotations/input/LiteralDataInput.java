package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input;

import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.DescriptionTypeAttr;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.InputAttr;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.LiteralDataAttr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@LiteralDataAttr
public @interface LiteralDataInput {
    InputAttr inputAttr();

    DescriptionTypeAttr descriptionType();

    LiteralDataAttr literalAttr();
}
