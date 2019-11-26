package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service;

import ro.uti.ksme.wps.wps2_server.uti_wps2.dto.ExceptionDTO;

import java.util.Optional;

public interface ErrorService {
    Optional<String> generateErrorMsgXml(ExceptionDTO exDto);

    Optional<String> generateErrorMsgXml(String err);
}
