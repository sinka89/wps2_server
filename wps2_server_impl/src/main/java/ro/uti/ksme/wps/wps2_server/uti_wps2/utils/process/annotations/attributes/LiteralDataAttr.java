package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes;

import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.FormatFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface LiteralDataAttr {
    String defaultDomain() default "";

    String[] validDomains() default {};

    String[] formats() default {FormatFactory.TEXT_EXTENSION};

    String defaultFormat() default FormatFactory.TEXT_EXTENSION;
}
