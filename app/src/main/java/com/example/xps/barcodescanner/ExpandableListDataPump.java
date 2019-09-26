package com.example.xps.barcodescanner;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Model.AllBookingInfo;
import Model.BasicJsonAllBooking;
import Model.BookingDetails;
import Model.OtherBookingDetails;
import Service.webservice;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExpandableListDataPump {

    static String name, barcode, ticketNo, status, claimedBy, claimedTime;
    static List<String> otherBookingDetails = new ArrayList<>();


    public static List<String> getData() {

        Retrofit retrofitallpost = new Retrofit.Builder()
                .baseUrl("https://gsm.blobstation.info/index.php/Gsmapi/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(1, TimeUnit.MINUTES) // write timeout
                .readTimeout(1, TimeUnit.MINUTES) // read timeout
                .build();

        final List<String> expandableListDetail = new ArrayList<String>();


        final webservice apiService = retrofitallpost.create(webservice.class);
        Call<BasicJsonAllBooking> call = apiService.getBookingInfoByBookingId("3320190802-5d43c0610e102");
        call.enqueue(new Callback<BasicJsonAllBooking>() {
            @Override
            public void onResponse(Call<BasicJsonAllBooking> call, Response<BasicJsonAllBooking> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        AllBookingInfo responsedata = response.body().getData();

                        String bookingId = responsedata.getBookingId();
                        String bookingTime = responsedata.getBookingTime();
                        String eventName = responsedata.getEventName();
                        String noTicket = responsedata.getNoOfTicket();
                        String seats = responsedata.getSeats();
                        String transactionId = responsedata.getTransactionId();
                        String note = responsedata.getNote();
                        String paymentType = responsedata.getPaymentType();

                        for (OtherBookingDetails bookingdetail : responsedata.getOtherBookingDetails()) {
                           // listofBookings.add(bookingdetail);
                            name = bookingdetail.getName();
                            barcode = bookingdetail.getBarcode();
                            ticketNo = bookingdetail.getTicketNo();
                            status = bookingdetail.getStatus();
                            claimedBy = bookingdetail.getClaimedBy();
                            claimedTime = bookingdetail.getClaimedTime();
//
                            otherBookingDetails.add(name);
                            otherBookingDetails.add(barcode);
                            otherBookingDetails.add(ticketNo);
                            otherBookingDetails.add(status);
                            otherBookingDetails.add(claimedBy);
                            otherBookingDetails.add(claimedTime);
                            expandableListDetail.addAll(otherBookingDetails);

                        }


                    }

                }
            }

            @Override
            public void onFailure(Call<BasicJsonAllBooking> call, Throwable t) {
                Log.i("Error", t.getMessage());
            }
        });

        return expandableListDetail;

    }
}





//
//        List<String> football = new ArrayList<String>();
//        football.add("Brazil");
//        football.add("Spain");
//        football.add("Germany");
//        football.add("Netherlands");
//        football.add("Italy");
//
//        List<String> basketball = new ArrayList<String>();
//        basketball.add("United States");
//        basketball.add("Spain");
//        basketball.add("Argentina");
//        basketball.add("France");
//        basketball.add("Russia");




