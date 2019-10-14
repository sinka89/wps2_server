package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 10/14/2019
 * Time: 11:40 AM
 */
public enum ProcessCloserMap {
    INSTANCE;

    public Map<UUID, Object> closureMap;

    ProcessCloserMap() {
        closureMap = new ConcurrentHashMap<>();
    }
}
