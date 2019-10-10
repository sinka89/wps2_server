package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Bogdan-Adrian Sincu
 * <p>
 * Annotation that is used to define the generic attributes on a BoundingBox Input / Ouput
 * <p>
 * - dimension : int -> Dimension of the BBOX
 * - defaultCRS : String of format authority:code -> Used to define the default CRS to use in the BoundingBox
 * - supportedCRSArray : String array of format authority:code -> used to define the supported CRS by the process
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface BoundingBoxAttr {
    String CRS_EPSG_4326 = "EPSG:4326";

    String defaultCRS() default CRS_EPSG_4326;

    int dimension() default 2;

    String[] supportedCRSArray() default {CRS_EPSG_4326};
}
