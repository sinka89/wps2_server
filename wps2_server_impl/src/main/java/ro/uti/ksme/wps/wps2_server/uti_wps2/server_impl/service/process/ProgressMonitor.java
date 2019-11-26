package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ProgressMonitor implements ProgressVisitor {
    public static String PROP_PROGRESS = "PROP_PROGRESS";
    public static String PROP_CANCEL = "PROP_CANCEL";
    public static String PROP_NAME = "PROP_NAME";

    private double progressDone = 0;
    private String taskName;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private boolean isCanceled;
    private int stepCount;
    private int stepDone = 0;

    public ProgressMonitor(String taskName) {
        this.isCanceled = false;
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    void setTaskName(String taskName) {
        String oldValue = taskName;
        this.taskName = taskName;
        triggerPropertyChangeEvent(PROP_NAME, oldValue, taskName);
    }

    private synchronized void pushProgress(double incProg) {
        if (progressDone + incProg > 1.0) {
            progressDone = 1.0;
        } else {
            progressDone += incProg;
        }
    }


    @Override
    public ProgressVisitor subProcess(int stepCount) {
        this.stepCount = stepCount;
        stepDone = 0;
        return this;
    }

    @Override
    public synchronized void endStep() {
        stepDone++;
        progressTo(stepDone);
    }

    @Override
    public void setStep(int i) {
        stepDone = i;
        progressTo(stepDone);
    }

    @Override
    public int getStepCount() {
        return stepCount;
    }

    @Override
    public void endOfProgress() {
        if (this.stepCount == 0) {
            progressTo(1);
        } else {
            progressTo(this.stepCount);
        }
    }

    @Override
    public double getProgression() {
        return progressDone;
    }

    @Override
    public boolean isCanceled() {
        return isCanceled;
    }

    @Override
    public void cancel() {
        boolean oldValue = this.isCanceled;
        this.isCanceled = true;
        propertyChangeSupport.firePropertyChange(PROP_CANCEL, oldValue, isCanceled);
    }

    @Override
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(property, listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private void progressTo(long progress) {
        double oldProgress = progressDone;
        pushProgress((double) progress / stepCount - progressDone);
        triggerPropertyChangeEvent(PROP_PROGRESS, oldProgress * 100, progressDone * 100);
    }

    private void triggerPropertyChangeEvent(String propName, Object oldValue, Object newValue) {
        PropertyChangeEvent event = new PropertyChangeEvent(this, propName, oldValue, newValue);
        propertyChangeSupport.firePropertyChange(event);
    }
}
