package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtherBookingDetails {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("ticketNo")
    @Expose
    private String ticketNo;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("ClaimedBy")
    @Expose
    private String ClaimedBy;
    @SerializedName("ClaimedTime")
    @Expose
    private String ClaimedTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClaimedBy() {
        return ClaimedBy;
    }

    public void setClaimedBy(String claimedBy) {
        ClaimedBy = claimedBy;
    }

    public String getClaimedTime() {
        return ClaimedTime;
    }

    public void setClaimedTime(String claimedTime) {
        ClaimedTime = claimedTime;
    }


}
