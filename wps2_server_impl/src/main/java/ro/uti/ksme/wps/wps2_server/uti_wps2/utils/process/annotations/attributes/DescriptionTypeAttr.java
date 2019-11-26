package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Bogdan-Adrian Sincu
 * <p>
 * Annotation that is used to describe generic attributes for the Process or Input / Output Data
 * The internationalization of the title / description is not implemented
 * <p>
 * title: String (mandatory) -> used to set the title of the process, input, output. From xsd Title of this resource, normally used for display to humans.
 * not implemented yet the internationalization of the process title
 * <p>
 * identifier: String (mandatory) -> The unique identifier for the Process / Input / Ouput field
 * <<will cause exception if there are classes with non unique process identifier and server will shutdown with code -1>>
 * <p>
 * description: String (optional) -> used to set the brief description of the process, input, output.
 * <p>
 * keywords: String array (optional) -> used to define a list of human redable keywords with language base. ex: ["keyword1, keyword2", "en", "something1, something2", "ro"] : Implementation not finished
 * <p>
 * metadata: String array (optional) -> used to define additional metadata about the item. Composed of a succession of three Strings: the metadata role, the metadata title and the href comma separated
 * Implementation not finished
 * ex: ["rolex,title,hrefx", "roleT,title,hrefT"]
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface DescriptionTypeAttr {

    String title();

    String description() default "";

    String[] keywords() default {};

    String identifier();

    String[] metadata() default {};
}
