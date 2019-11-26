package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.parser;

import ro.uti.ksme.wps.wps2.pojo.wps._2.InputDescriptionType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.OutputDescriptionType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.exceptions.MalformedModelException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface Parser {

    InputDescriptionType parseInput(Field f, Object defaultValue, Class cls) throws MalformedModelException;

    OutputDescriptionType parseOutput(Method method, Object defaultValue, Class cls) throws MalformedModelException;

    Class getAnnotation();
}
