package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service;

import org.springframework.stereotype.Service;
import ro.uti.ksme.wps.wps2.pojo.wps._2.DataInputType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.InputDescriptionType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.OutputDefinitionType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.OutputDescriptionType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.ProcessIdentifier;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.exceptions.ProcessInputValidationException;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.exceptions.ProcessOutputValidationException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bogdan-Adrian Sincu created on 10/9/2019
 */
@Service
public class ProcessValidatorServiceImpl implements ProcessValidatorService {
    @Override
    public void validateInputForExecutionProcess(ProcessIdentifier processIdentifier, List<DataInputType> input) throws ProcessInputValidationException {
        List<InputDescriptionType> processInputs = processIdentifier.getProcessDescriptionType().getInput();
        List<DataInputType> inputDataFound = new ArrayList<>();
        List<Integer> allreadyChecked = new ArrayList<>();
        boolean dataFound = false;
        for (InputDescriptionType pI : processInputs) {
            for (int i = 0; i < input.size(); ++i) {
                if (!allreadyChecked.isEmpty() && allreadyChecked.contains(i)) {
                    continue;
                }
                if (input.get(i).getData() != null && input.get(i).getReference() != null) {
                    throw new ProcessInputValidationException("Data & Reference simultaneous presents not supported for input: " + input.get(i).getId());
                }
                if (pI.getIdentifier().getValue().equalsIgnoreCase(input.get(i).getId())) {
                    inputDataFound.add(input.get(i));
                    dataFound = true;
                    allreadyChecked.add(i);
                }
            }
            if (inputDataFound.size() > Integer.valueOf(pI.getMaxOccurs())) {
                throw new ProcessInputValidationException("The required Input " + pI.getIdentifier().getValue() + " has too many occurs. Maximum allowed value is " + pI.getMaxOccurs());
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

    /**
     * @param processIdentifier the process from memory
     * @param outputs           the outputs provided by the http request body
     * @throws ProcessInputValidationException if the matching identifier from the request is not defined in the process
     */
    @Override
    public void validateOutputForExecutionProcess(ProcessIdentifier processIdentifier, List<OutputDefinitionType> outputs) throws ProcessInputValidationException {
        List<OutputDescriptionType> processOutputs = processIdentifier.getProcessDescriptionType().getOutput();
        boolean dataFound = false;
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < outputs.size(); ++i) {
            for (OutputDescriptionType pO : processOutputs) {
                if (pO.isSetIdentifier() && outputs.get(i).isSetId() && pO.getIdentifier().isSetValue() && pO.getIdentifier().getValue().equals(outputs.get(i).getId())) {
                    dataFound = true;
                }
            }
            if (!dataFound) {
                throw new ProcessOutputValidationException("The output identifier provided: " + outputs.get(i).getId() + " is not defined in the process");
            }
            dataFound = false;
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
