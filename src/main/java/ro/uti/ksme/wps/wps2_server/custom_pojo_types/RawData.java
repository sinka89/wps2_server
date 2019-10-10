package ro.uti.ksme.wps.wps2_server.custom_pojo_types;

import ro.uti.ksme.wps.wps2_server.pojo.wps._2.ComplexDataType;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.Format;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Bogdan-Adrian Sincu created on 10/7/2019
 * <p>
 * ComplexData.class extension to reprezent a file or a folder
 * Folder implementation is not done
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RawData", propOrder = {"isFile", "isDirectory", "fileTypes"})
public class RawData extends ComplexDataType {

    /* True if the RawData can be a file */
    @XmlAttribute(name = "isFile")
    private boolean isFile;

    /* True if the RawData can be a directory */
    @XmlAttribute(name = "isDirectory")
    private boolean isDirectory;

    /* Array of the file types allowed by the raw model. if no types are specified, accept all */
    @XmlElement(name = "fileTypes")
    private String[] fileTypes;

    public RawData(List<Format> formatList) {
        this.format = formatList;
    }

    protected RawData() {
        super();
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
