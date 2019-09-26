package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Ticket implements Serializable
{

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("result")
    @Expose
    private boolean result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("verifyBy")
    @Expose
    private String verifyBy;
    @SerializedName("verifyTime")
    @Expose
    private String verifyTime;


    public List<UserDetail> getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(List<UserDetail> userDetail) {
        this.userDetail = userDetail;
    }

    @SerializedName("userDetail")
    @Expose
    private List<UserDetail> userDetail = null;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getVerifyBy() {
        return verifyBy;
    }

    public void setVerifyBy(String verifyBy) {
        this.verifyBy = verifyBy;
    }

    public String getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(String verifyTime) {
        this.verifyTime = verifyTime;
    }

}

