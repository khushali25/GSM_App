package Service;

import Model.BasicJson;
import Model.BasicJsonAllBooking;
import Model.BasicJsonBooking;
import Model.CleanData;
import Model.Ticket;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface webservice {

    @GET("ticketValidation/{TicketId}/{UserName}/{EventId}")
    Call<Ticket> checkTicket(@Path("TicketId") String TicketId, @Path("UserName") String UserName, @Path("EventId") String EventId);

    @GET("getAllEvents")
    Call<BasicJson> getAllEvents();

    @GET("getEventsForScanning")
    Call<BasicJson> getEventsForScanning();

    @GET("getAllBookingDetails/{EventId}")
    Call<BasicJsonBooking> getBookingDetailsByEventId(@Path("EventId") String EventId);

    @GET("getBookingInfo/{TicketId}")
    Call<BasicJsonAllBooking> getBookingInfoByBookingId(@Path("TicketId") String TicketId);

    @GET("resetTicketStatus/41")
    Call<CleanData> cleanData();



}
