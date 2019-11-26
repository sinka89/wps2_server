package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Bogdan-Adrian Sincu
 * <p>
 * Annotation used to describe the min / max occurrences of an item value
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface InputAttr {

    //Minimum nr of times the value for this param is required. 0 means the input is optional
    int minOccurs() default 1;

    //Maximum nr of times that this param may be present
    int maxOccurs() default 1;
}
