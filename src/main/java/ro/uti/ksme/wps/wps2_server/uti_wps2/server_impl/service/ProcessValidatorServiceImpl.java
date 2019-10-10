package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service;

import ro.uti.ksme.wps.wps2_server.pojo.wps._2.DataInputType;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.InputDescriptionType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.ProcessIdentifier;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.exceptions.ProcessInputValidationException;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bogdan-Adrian Sincu created on 10/9/2019
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ProcessValidatorServiceImpl implements ProcessValidatorService {
    @Override
    public void validateInputForExecutionProcess(ProcessIdentifier processIdentifier, List<DataInputType> input) throws ProcessInputValidationException {
        List<InputDescriptionType> processInputs = processIdentifier.getProcessDescriptionType().getInput();
        List<DataInputType> inputDataFound = new ArrayList<>();
        boolean dataFound = false;
        for (InputDescriptionType pI : processInputs) {
            for (DataInputType pD : input) {
                if (pD.getData() != null && pD.getReference() != null) {
                    throw new ProcessInputValidationException("Data & Reference simultaneous presents not supported for input: " + pD.getId());
                }
                if (pI.getIdentifier().getValue().equalsIgnoreCase(pD.getId())) {
                    inputDataFound.add(pD);
                    dataFound = true;
                }
            }
            if (pI.getMinOccurs().equals(BigInteger.ONE)) {
                if (!dataFound) {
                    throw new ProcessInputValidationException("Required Input " + pI.getIdentifier().getValue() + " is not present");
                } else {
                    for (DataInputType d : inputDataFound) {
                        nullInputChecker(d);
                    }
                }
            }
            dataFound = false;
            inputDataFound.clear();
        }
    }

    private void nullInputChecker(DataInputType inputType) {
        if (inputType.getData() != null && !inputType.getData().getContent().isEmpty()) {
            return;
        } else if (inputType.getReference() != null && inputType.getReference().isSetBody()) {
            return;
        } else if (inputType.getInput() != null && !inputType.getInput().isEmpty()) {
            for (DataInputType other : inputType.getInput()) {
                nullInputChecker(other);
            }
        } else {
            throw new ProcessInputValidationException("Input with identifier: " + inputType.getId() + " has no eligible data present");
        }
    }
}
