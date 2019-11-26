package ro.uti.ksme.wps.common.utils.enums;

/**
 * @author Bogdan-Adrian Sincu created on 10/8/2019
 */
public enum ResponseType {
    DOCUMENT("document"),
    RAW("raw");

    private String type;

    ResponseType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
