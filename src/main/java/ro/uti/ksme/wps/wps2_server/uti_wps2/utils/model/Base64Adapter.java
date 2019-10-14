package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 10/11/2019
 * Time: 9:36 AM
 */
public class Base64Adapter extends XmlAdapter<String, byte[]> {
    @Override
    public byte[] unmarshal(String v) throws Exception {
        return v.getBytes();
    }

    @Override
    public String marshal(byte[] v) throws Exception {
        return new String(v);
    }
}
