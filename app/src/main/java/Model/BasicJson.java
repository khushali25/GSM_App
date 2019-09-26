package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BasicJson implements Serializable
{
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("result")
    @Expose
    private boolean result;
    @SerializedName("data")
    @Expose
    private List<AllActiveEvents> data = null;
   // private final static long serialVersionUID = 3418511557733948497L;

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

    public List<AllActiveEvents> getData() {
        return data;
    }

    public void setData(List<AllActiveEvents> data) {
        this.data = data;
    }

}

