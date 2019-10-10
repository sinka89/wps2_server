package ro.uti.ksme.wps.wps2_server.uti_wps2.dto;

public class ExceptionDTO {
    private Integer status;
    private String exMsg;

    public ExceptionDTO() {
    }

    public ExceptionDTO(Integer status, String exMsg) {
        this.status = status;
        this.exMsg = exMsg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getExMsg() {
        return exMsg;
    }

    public void setExMsg(String exMsg) {
        this.exMsg = exMsg;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Error Could not be parsed... \n ErrorStatus: ").append(this.status != null ? this.status : 0).append("\n")
                .append("With Message: ").append(this.exMsg != null ? this.exMsg : "N/A").append("-----");
        return sb.toString();
    }
}
