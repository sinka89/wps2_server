package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service;

import ro.uti.ksme.wps.wps2.pojo.wps._2.DataInputType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.ProcessIdentifier;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.exceptions.ProcessInputValidationException;

import java.util.List;

/**
 * @author Bogdan-Adrian Sincu created on 10/9/2019
 */
public interface ProcessValidatorService {
    void validateInputForExecutionProcess(ProcessIdentifier processIdentifier, List<DataInputType> input) throws ProcessInputValidationException;
}
