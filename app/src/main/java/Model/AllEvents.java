package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AllEvents implements Serializable {

    @SerializedName("eventId")
    @Expose
    private String eventId;
    @SerializedName("eventName")
    @Expose
    private String eventName;
    @SerializedName("ticketSoldByEventId")
    @Expose
    private String ticketSoldByEventId;
    @SerializedName("totalAttendeesNow")
    @Expose
    private int totalAttendeesNow;
    @SerializedName("agentData")
    @Expose
    private List<AgentData> agentData = null;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getTicketSoldByEventId() {
        return ticketSoldByEventId;
    }

    public void setTicketSoldByEventId(String ticketSoldByEventId) {
        this.ticketSoldByEventId = ticketSoldByEventId;
    }

    public int getTotalAttendeesNow() {
        return totalAttendeesNow;
    }

    public void setTotalAttendeesNow(int totalAttendeesNow) {
        this.totalAttendeesNow = totalAttendeesNow;
    }

    public List<AgentData> getAgentData() {
        return agentData;
    }

    public void setAgentData(List<AgentData> agentData) {
        this.agentData = agentData;
    }
}
