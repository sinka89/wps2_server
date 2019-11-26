package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process;

import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.DescriptionTypeAttr;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.ProcessAttr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Bogdan-Adrian Sincu
 * Main process annotation that defines a wps2 process implementation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Process {

    DescriptionTypeAttr descriptionType();

    ProcessAttr processAttr();
}
