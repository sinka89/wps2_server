package ro.uti.ksme.wps.wps2_client.response;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 12/3/2019
 * Time: 1:48 PM
 */
public class ProgressInputStream extends FilterInputStream {
    public static final String PROP_TOTAL_NUM_BYTES_READ = "totalNumBytesRead";
    public static final String PROP_ALL_BYTES_READ = "allBytesReadFinished";
    private final PropertyChangeSupport propertyChangeSupport;
    private final long maxNumBytes;
    private volatile AtomicLong totalNumBytesRead = new AtomicLong(0);

    public ProgressInputStream(InputStream in, long maxNumBytes) {
        super(in);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.maxNumBytes = maxNumBytes;
    }

    public long getMaxNumBytes() {
        return this.maxNumBytes;
    }

    public long getTotalNumBytesRead() {
        return totalNumBytesRead.get();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return (int) updateProgress(super.read(b));
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return (int) updateProgress(super.read(b, off, len));
    }

    @Override
    public long skip(long n) throws IOException {
        return updateProgress(super.skip(n));
    }

    @Override
    public synchronized void mark(int readlimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void reset() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    private long updateProgress(long numBytesRead) {
        if (numBytesRead > 0) {
            long oldTotalNumBytesRead = this.totalNumBytesRead.get();
            long l = this.totalNumBytesRead.addAndGet(numBytesRead);
            propertyChangeSupport.firePropertyChange(PROP_TOTAL_NUM_BYTES_READ, oldTotalNumBytesRead, l);
        } else if (numBytesRead == -1) {
            propertyChangeSupport.firePropertyChange(PROP_ALL_BYTES_READ, this.totalNumBytesRead.get(), -1);
        }
        return numBytesRead;
    }
}
