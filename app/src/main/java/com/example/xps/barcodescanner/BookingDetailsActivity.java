package com.example.xps.barcodescanner;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Model.AgentData;
import Model.AllBookingInfo;
import Model.AllEvents;
import Model.BasicJsonAllBooking;
import Model.BasicJsonBooking;
import Model.OtherBookingDetails;
import Model.Ticket;
import Model.UserDetail;
import Service.webservice;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookingDetailsActivity extends AppCompatActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;

    TableLayout tl;
    String name, barcode, ticketNo, status, claimedBy, claimedTime;
    String bookingId,bookingTime,eventName,noTicket,seats,transactionId,note,paymentType;
    TextView bookingId1,eventName1,noOfTicket1,seats1,bookingTime1,transactionId1,note1,paymentType1;
    TextView bookingIdtxt,eventNametxt,noOfTickettxt,seatstxt,bookingTimetxt,transactionIdtxt,notetxt,paymentTypetxt;
    Button btnscan;

    Retrofit retrofitallpost = new Retrofit.Builder()
            .baseUrl("https://gsmweb.montrealgujarati.com/index.php/Gsmapi/")
            .addConverterFactory(GsonConverterFactory.create(new Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES) // connect timeout
            .writeTimeout(1, TimeUnit.MINUTES) // write timeout
            .readTimeout(1, TimeUnit.MINUTES) // read timeout
            .build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        tl = findViewById(R.id.table);

        bookingId1 = findViewById(R.id.bookingIdadmin);
        eventName1 = findViewById(R.id.eventNamebooking);
        noOfTicket1 = findViewById(R.id.noOfTicket);
        seats1 = findViewById(R.id.seats);
        bookingTime1 = findViewById(R.id.bookingTime);
        transactionId1 = findViewById(R.id.transactionId);
        note1 = findViewById(R.id.note);
        paymentType1 = findViewById(R.id.paymentType);

        bookingIdtxt = findViewById(R.id.bookingIdtxt);
        eventNametxt = findViewById(R.id.eventNametxt);
        noOfTickettxt = findViewById(R.id.noOfTickettxt);
        seatstxt = findViewById(R.id.seatstxt);
        bookingTimetxt = findViewById(R.id.bookingTimetxt);
        transactionIdtxt = findViewById(R.id.transactionIdtxt);
        notetxt = findViewById(R.id.notetxt);
        paymentTypetxt = findViewById(R.id.paymentTypetxt);
        VisibilityDisabled();
        btnscan = findViewById(R.id.scan);
        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // barcodeValue.setVisibility(View.GONE);
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                VisibilityDisabled();
                tl.removeAllViews();
                //intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
                //intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());

                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Bring up gallery to select a photo
                    startActivityForResult(intent, RC_BARCODE_CAPTURE);
                }
            }
        });

        //GetBookingDetails();

      }

      private void VisibilityDisabled()
      {
          bookingId1.setVisibility(View.GONE);
          eventName1.setVisibility(View.GONE);
          noOfTicket1.setVisibility(View.GONE);
          seats1.setVisibility(View.GONE);
          bookingTime1.setVisibility(View.GONE);
          transactionId1.setVisibility(View.GONE);
          note1.setVisibility(View.GONE);
          paymentType1.setVisibility(View.GONE);

          bookingIdtxt.setVisibility(View.GONE);
          eventNametxt.setVisibility(View.GONE);
          noOfTickettxt.setVisibility(View.GONE);
          seatstxt.setVisibility(View.GONE);
          bookingTimetxt.setVisibility(View.GONE);
          transactionIdtxt.setVisibility(View.GONE);
          notetxt.setVisibility(View.GONE);
          paymentTypetxt.setVisibility(View.GONE);
      }
    private void VisibilityEnabled()
    {
        bookingId1.setVisibility(View.VISIBLE);
        eventName1.setVisibility(View.VISIBLE);
        noOfTicket1.setVisibility(View.VISIBLE);
        seats1.setVisibility(View.VISIBLE);
        bookingTime1.setVisibility(View.VISIBLE);
        transactionId1.setVisibility(View.VISIBLE);
        note1.setVisibility(View.VISIBLE);
        paymentType1.setVisibility(View.VISIBLE);

        bookingIdtxt.setVisibility(View.VISIBLE);
        eventNametxt.setVisibility(View.VISIBLE);
        noOfTickettxt.setVisibility(View.VISIBLE);
        seatstxt.setVisibility(View.VISIBLE);
        bookingTimetxt.setVisibility(View.VISIBLE);
        transactionIdtxt.setVisibility(View.VISIBLE);
        notetxt.setVisibility(View.VISIBLE);
        paymentTypetxt.setVisibility(View.VISIBLE);
    }

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setPadding(40, 20, 40, 20);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        //  tv.setOnClickListener(this);
        return tv;
    }

    @NonNull
    private LinearLayout.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
    }
    /**
     * This function add the headers to the table
     **/
    public void addHeaders() {

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());
        tr.addView(getTextView(0, "Name", Color.DKGRAY, Typeface.BOLD, Color.LTGRAY));
        tr.addView(getTextView(0, "Barcode", Color.DKGRAY, Typeface.BOLD, Color.LTGRAY));
        tr.addView(getTextView(0, "Ticket Number", Color.DKGRAY, Typeface.BOLD, Color.LTGRAY));
        tr.addView(getTextView(0, "Status", Color.DKGRAY, Typeface.BOLD, Color.LTGRAY));
        tr.addView(getTextView(0, "Claimed By", Color.DKGRAY, Typeface.BOLD, Color.LTGRAY));
        tr.addView(getTextView(0, "Claimed Time", Color.DKGRAY, Typeface.BOLD, Color.LTGRAY));
        tl.addView(tr, getTblLayoutParams());
    }

    /**
     * This function add the data to the table
     **/
    public void addData() {
//        String NA = "N/A";
//        if(name == ""){
//            name = NA;
//        }
//        if(status == ""){
//            status = NA;
//        }
//        if(ticketNo == ""){
//            ticketNo = NA;
//        }
//        if(barcode == ""){
//            NA = barcode;
//        }
//        if(claimedBy == " "){
//            claimedBy = NA;
//        }
//        if(claimedTime == " "){
//            claimedTime = NA;
//        }
        TableLayout tl = findViewById(R.id.table);
        for (int i = 0; i < 1; i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(getLayoutParams());
            tr.addView(getTextView(i + 1, name, Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorRow)));
            tr.addView(getTextView(i + 1, barcode, Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorRow)));
            tr.addView(getTextView(i + 1, ticketNo, Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorRow)));
            tr.addView(getTextView(i + 1, status, Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorRow)));
            tr.addView(getTextView(i + 1, claimedBy, Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorRow)));
            tr.addView(getTextView(i + 1, claimedTime, Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorRow)));

            tl.addView(tr, getTblLayoutParams());
        }

    }
    private void GetBookingDetails(String rawValue) {

        final webservice apiService = retrofitallpost.create(webservice.class);
        Call<BasicJsonAllBooking> call = apiService.getBookingInfoByBookingId(rawValue);
        call.enqueue(new Callback<BasicJsonAllBooking>() {
            @Override
            public void onResponse(Call<BasicJsonAllBooking> call, Response<BasicJsonAllBooking> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        AllBookingInfo responsedata = response.body().getData();

                         bookingId = responsedata.getBookingId();
                         bookingTime = responsedata.getBookingTime();
                         eventName = responsedata.getEventName();
                         noTicket = responsedata.getNoOfTicket();
                         seats = responsedata.getSeats();
                         transactionId = responsedata.getTransactionId();
                         note = responsedata.getNote();
                         paymentType = responsedata.getPaymentType();

                        bookingIdtxt.setText(bookingId);
                        bookingTimetxt.setText(bookingTime);
                        eventNametxt.setText(eventName);
                        noOfTickettxt.setText(noTicket);
                        seatstxt.setText(seats);
                        transactionIdtxt.setText(transactionId);
                        notetxt.setText(note);
                        paymentTypetxt.setText(paymentType);

                        addHeaders();
                        for(OtherBookingDetails bookingdetail : responsedata.getOtherBookingDetails()) {
                           name = bookingdetail.getName();
                           barcode = bookingdetail.getBarcode();
                           ticketNo = bookingdetail.getTicketNo();
                           status = bookingdetail.getStatus();
                           claimedBy = bookingdetail.getClaimedBy();
                           claimedTime = bookingdetail.getClaimedTime();
                           addData();
                       }
                        VisibilityEnabled();

                    }
                } else {
                    Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<BasicJsonAllBooking> call, Throwable t) {
                Log.i("Error", t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    GetBookingDetails(barcode.rawValue);
                   // get(barcode.rawValue,name,event);
                    //  statusMessage.setText(R.string.barcode_success);
                    //barcodeValue.setText(barcode.valueAt(0).displayValue);
                  //  Log.d(TAG, "Barcode read: " + barcode.displayValue);



                } else {
                    //statusMessage.setText(R.string.barcode_failure);
                    //Log.d(TAG, "No barcode captured, intent data is null");
                }
            }
            else {
                // statusMessage.setText(String.format(getString(R.string.barcode_error),
                //  CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}