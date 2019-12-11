package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 12/11/2019
 * Time: 1:35 PM
 */
public final class ProcessResultWrapper<T> {
    private String mimeType;
    private T data;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
