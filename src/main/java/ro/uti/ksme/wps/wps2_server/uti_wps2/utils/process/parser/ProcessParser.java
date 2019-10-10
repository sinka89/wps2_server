package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.parser;

import ro.uti.ksme.wps.wps2_server.pojo.wps._2.ProcessDescriptionType;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.ProcessOffering;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.exceptions.MalformedModelException;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ObjectAnnotationConverter;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ProcessParser {
    public ProcessOffering parseProcess(Class<?> cls) throws MalformedModelException {
        ProcessDescriptionType processDescriptionType = new ProcessDescriptionType();
        ObjectAnnotationConverter.annotationToObject(cls.getAnnotation(Process.class).descriptionType(), processDescriptionType, "");
        ProcessOffering processOffering = new ProcessOffering();
        processOffering.setProcess(processDescriptionType);
        ObjectAnnotationConverter.annotationToObject(cls.getAnnotation(Process.class).processAttr(), processOffering);
        return processOffering;
    }
}