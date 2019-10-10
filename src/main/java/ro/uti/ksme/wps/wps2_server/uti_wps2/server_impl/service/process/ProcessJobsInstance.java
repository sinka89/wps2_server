package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.Wps2ServerProps;

public enum ProcessJobsInstance {
    INSTANCE;

    public Cache jobProcessCache;

    private CacheManager manager;

    ProcessJobsInstance() {
        this.manager = CacheManager.create();
        CacheConfiguration config = new CacheConfiguration();
        config.setName("jobProcessCache");
        config.setMaxEntriesLocalHeap(500);
        config.setMaxEntriesLocalDisk(1000000);
        config.setTimeToLiveSeconds(Wps2ServerProps.DESTROY_TIME_MILLI / 1000);
        config.setEternal(false);
        Cache cache = new Cache(config);
        this.manager.addCache(cache);
        this.jobProcessCache = this.manager.getCache("jobProcessCache");
    }

    public CacheManager getManager() {
        return manager;
    }
}
