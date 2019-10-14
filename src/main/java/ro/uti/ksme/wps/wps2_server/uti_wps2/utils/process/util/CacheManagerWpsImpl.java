package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.net.URL;

/**
 * @author Bogdan-Adrian Sincu created on 10/9/2019
 * Class that creates an instance of EhCache manager and Cache for ProcessJobs that contain the result of the process itself
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class CacheManagerWpsImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheManagerWpsImpl.class);

    private final Cache jobProcessCache;

    private final CacheManager manager;

    public CacheManagerWpsImpl() {
        LOGGER.info("Trying to start Cache Manager");
        final URL url = this.getClass().getClassLoader().getResource("ehcache.xml");
        if (url == null) {
            throw new NullPointerException("ERROR >>>> ehcache.xml file is missing... cannot start server.");
        }
        this.manager = CacheManager.newInstance(url);
        LOGGER.debug("Cache Manager started...");
        this.jobProcessCache = this.manager.getCache("jobProcessCache");
        LOGGER.debug("Job Process Cache added with id: 'jobProcessCache'");
    }

    public CacheManager getManager() {
        return manager;
    }

    public Cache getJobProcessCache() {
        return jobProcessCache;
    }

}
