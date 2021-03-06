package ro.uti.ksme.wps.wps2.custom_pojo_types;

import ro.uti.ksme.wps.wps2.pojo.wps._2.ComplexDataType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.Format;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Bogdan-Adrian Sincu created on 10/7/2019
 * <p>
 * ComplexData.class extension to reprezent a file or a folder
 * Folder implementation is not done
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RawData", propOrder = {"isFile", "isDirectory", "fileTypes", "byteData"})
@XmlRootElement(name = "RawData")
public class RawData extends ComplexDataType implements Serializable {

    /* True if the RawData can be a file */
    @XmlAttribute(name = "isFile")
    private boolean isFile;

    /* True if the RawData can be a directory */
    @XmlAttribute(name = "isDirectory")
    private boolean isDirectory;

    /* Array of the file types allowed by the raw model. if no types are specified, accept all */
    @XmlElement(name = "fileTypes")
    private String[] fileTypes;

    @XmlElement(name = "byteData")
    private byte[] byteData;

    public RawData(List<Format> formatList) {
        this.format = formatList;
    }

    protected RawData() {
        super();
    }

    public byte[] getByteData() {
        return byteData;
    }

    public void setByteData(byte[] byteData) {
        this.byteData = byteData;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public String[] getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(String[] fileTypes) {
        this.fileTypes = fileTypes;
    }
}
