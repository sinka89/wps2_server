package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes;

import ro.uti.ksme.wps.wps2_server.pojo.wps._2.DataTransmissionModeType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.JobControlOps;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Bogdan-Adrian Sincu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ProcessAttr {

    String language() default "en";

    String version() default "";

    String[] properties() default {};

    /**
     * JobControlOpts specifies the control of the process (sync or async) if nothing specified the default server props will be used
     */
    JobControlOps[] jobControl() default {};

    /**
     * @return specify to manipulate the result builder (VALUE or REFERENCE value -> link atm)
     */
    DataTransmissionModeType[] dataTransmissionType() default {DataTransmissionModeType.VALUE};

}
