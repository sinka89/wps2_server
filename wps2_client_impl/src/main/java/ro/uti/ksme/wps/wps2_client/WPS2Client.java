package ro.uti.ksme.wps.wps2_client;

import ro.uti.ksme.wps.wps2.pojo.wps._2.ProcessOfferings;
import ro.uti.ksme.wps.wps2.pojo.wps._2.StatusInfo;
import ro.uti.ksme.wps.wps2.pojo.wps._2.WPSCapabilitiesType;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/26/2019
 * Time: 11:30 AM
 */
public interface WPS2Client {

    Optional<WPSCapabilitiesType> getCapabilities();

    Optional<ProcessOfferings> describeProcess(String identifier, String language);

    Optional<StatusInfo> getStatusInfoForProcess(String identifier);

    Optional<StatusInfo> dismissProcess(String identifier);
}
