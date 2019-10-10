package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Bogdan-Adrian Sincu created on 10/7/2019
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface RawDataAttr {

    boolean isDirectory() default false;

    boolean isFile() default true;

    String[] fileTypes() default {};
}
