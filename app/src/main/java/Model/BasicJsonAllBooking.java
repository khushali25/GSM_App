package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasicJsonAllBooking {
    @SerializedName("status")
    @Expose
    private float status;
    @SerializedName("result")
    @Expose
    private boolean result;

    @SerializedName("data")
    @Expose
    AllBookingInfo data;

    public float getStatus() {
        return status;
    }

    public void setStatus(float status) {
        this.status = status;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }


    public AllBookingInfo getData() {
        return data;
    }

    public void setData(AllBookingInfo data) {
        this.data = data;
    }





}
