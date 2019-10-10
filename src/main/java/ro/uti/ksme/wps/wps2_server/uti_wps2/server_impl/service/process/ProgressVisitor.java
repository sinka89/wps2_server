package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import java.beans.PropertyChangeListener;

public interface ProgressVisitor {
    public static String PROPERTY_CANCELED = "CANCELED";

    ProgressVisitor subProcess(int stepCount);

    void endStep();

    void setStep(int idStep);

    int getStepCount();

    void endOfProgress();

    double getProgression();

    boolean isCanceled();

    void cancel();

    void addPropertyChangeListener(String property, PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);
}
