package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllBookingInfo {

    @SerializedName("bookingId")
    @Expose
    private String bookingId;
    @SerializedName("eventName")
    @Expose
    private String eventName;
    @SerializedName("noOfTicket")
    @Expose
    private String noOfTicket;
    @SerializedName("seats")
    @Expose
    private String seats;
    @SerializedName("bookingTime")
    @Expose
    private String bookingTime;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("paymentType")
    @Expose
    private String paymentType;
    @SerializedName("otherBookingDetails")
    @Expose
    List<OtherBookingDetails> otherBookingDetails = null;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getNoOfTicket() {
        return noOfTicket;
    }

    public void setNoOfTicket(String noOfTicket) {
        this.noOfTicket = noOfTicket;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public List<OtherBookingDetails> getOtherBookingDetails() {
        return otherBookingDetails;
    }

    public void setOtherBookingDetails( List<OtherBookingDetails> otherBookingDetails) {
        this.otherBookingDetails = otherBookingDetails;
    }

}
