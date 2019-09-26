package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BasicJsonBooking implements Serializable
{

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("result")
    @Expose
    private boolean result;
    @SerializedName("data")
    @Expose
    private AllEvents data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public AllEvents getData() {
        return data;
    }

    public void setData(AllEvents data) {
        this.data = data;
    }

}