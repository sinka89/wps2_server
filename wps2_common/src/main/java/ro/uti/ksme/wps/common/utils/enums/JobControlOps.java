package ro.uti.ksme.wps.common.utils.enums;

import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;

public enum JobControlOps {
    SYNC("sync", "sync-execute"),
    ASYNC("async", "async-execute"),
    AUTO("auto", "auto-execute"),
    DISMISS("dismiss", "dismiss");

    private final static Map<String, JobControlOps> mappingJobToEnum = EnumSet.allOf(JobControlOps.class).stream().collect(Collectors.toMap(JobControlOps::getMode, j -> j));
    private final static Map<String, JobControlOps> mappingJobControlToEnum = EnumSet.allOf(JobControlOps.class).stream().collect(Collectors.toMap(JobControlOps::getJobControlMode, j -> j));
    private String mode;
    private String jobControlMode;

    JobControlOps(String mode, String jobControlMode) {
        this.mode = mode;
        this.jobControlMode = jobControlMode;
    }

    public static JobControlOps getEnumByJobMode(String mode) {
        return mappingJobToEnum.getOrDefault(mode.toLowerCase(), JobControlOps.AUTO);
    }

    public static JobControlOps getEnumByJobControlMode(String jobControlMode) {
        return mappingJobControlToEnum.getOrDefault(jobControlMode.toLowerCase(), JobControlOps.AUTO);
    }

    public String getMode() {
        return mode;
    }

    public String getJobControlMode() {
        return jobControlMode;
    }
}
