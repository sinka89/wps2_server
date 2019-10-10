package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util;

import java.util.HashMap;
import java.util.Map;

public enum JobControlOps {
    SYNC("sync", "sync-execute"),
    ASYNC("async", "async-execute"),
    AUTO("auto", "auto-execute"),
    DISMISS("dismiss", "dismiss");

    private static Map<String, JobControlOps> mappingJobToEnum;
    private String mode;
    private String jobControlMode;

    JobControlOps(String mode, String jobControlMode) {
        this.mode = mode;
        this.jobControlMode = jobControlMode;
    }

    public static JobControlOps getEnumByJobMode(String mode) {
        if (mappingJobToEnum == null) {
            initMapping();
        }
        return mappingJobToEnum.getOrDefault(mode.toLowerCase(), JobControlOps.AUTO);
    }

    private static void initMapping() {
        mappingJobToEnum = new HashMap<>();
        for (JobControlOps j : values()) {
            mappingJobToEnum.put(j.mode, j);
        }
    }

    public String getMode() {
        return mode;
    }

    public String getJobControlMode() {
        return jobControlMode;
    }
}
