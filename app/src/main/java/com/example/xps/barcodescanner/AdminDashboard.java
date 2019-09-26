package com.example.xps.barcodescanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Model.AgentData;
import Model.AllActiveEvents;
import Model.AllEvents;
import Model.BasicJson;
import Model.BasicJsonBooking;
import Model.CleanData;
import Model.Ticket;
import Service.webservice;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminDashboard extends AppCompatActivity {

    Spinner spinner;
    SwipeRefreshLayout swipeRefreshLayout;
    String selectedItemText,eventId;
    TextView totaltxt,ticketsoldtxt,totalattendees,ticketsold;
    TableLayout tl;
    String name,total;
    Button btnbooking;
    Switch sw;

    final List<String> dataName = new ArrayList<>();

    Retrofit retrofitallpost=new Retrofit.Builder()
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
        setContentView(R.layout.activity_admin_dashboard);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
       // materialDesignSpinner = (MaterialBetterSpinner)
       //         findViewById(R.id.alleventspinner);
        spinner = (Spinner)findViewById(R.id.spinneradminactivity);
        spinner.setDropDownWidth(900);
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(1200);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        ticketsoldtxt = (TextView)findViewById(R.id.ticketsoldtxt);
        ticketsold = (TextView)findViewById(R.id.ticketsold);
        totaltxt = (TextView)findViewById(R.id.totalattendeestxt);
        totalattendees = (TextView)findViewById(R.id.totalattendees);
        tl = findViewById(R.id.table);
        swipeRefreshLayout = findViewById(R.id.simpleSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tl.removeAllViews();
                ticketsoldtxt.setText("");
                totaltxt.setText("");
                GetBookingDetailsByEventId();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        sw = (Switch) findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    showPopupPincode(AdminDashboard.this);
                } else {
                    // The toggle is disabled
                }
            }
        });


        btnbooking = (Button)findViewById(R.id.btnbooking);
        btnbooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookingDetailsActivity.class);
                startActivity(intent);
            }
        });
        GetAllEvents();

    }


    private void showPopupPincode(Context context) {

            final AlertDialog alertDialog;

            LayoutInflater layoutinflater = LayoutInflater.from(context);
            final View promptView = layoutinflater.inflate(R.layout.pincode_popup, null);
            alertDialog = new AlertDialog.Builder(context)
                    .setView(promptView)
                    .setTitle("Pincode")
                    .setPositiveButton("Submit", null)
                    .setNegativeButton("Cancel", null)
                    .create();
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    sw.setChecked(false);
                }
            });
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {

                    Button buttonPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    buttonPositive.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            EditText pincode = promptView.findViewById(R.id.pincodeEdt);
                            TextView showResult = (TextView) promptView.findViewById(R.id.pincodeError);
                            final String userValue = pincode.getText().toString();

                            if (!(userValue.equals("1925"))) {
                                try {
                                    showResult.setText("Please enter valid pincode number");
                                    showResult.setVisibility(View.VISIBLE);
                                } catch (Exception ex) {
                                    //Crashlytics.logException(ex);
                                    new Error(ex.getMessage());
                                }
                            }  else {

                                SendDataCleanEvent();
                                showResult.setVisibility(View.INVISIBLE);
                                alertDialog.dismiss();
                            }
                        }
                    });
                    Button buttonNegative = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                    buttonNegative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            sw.setChecked(false);

                        }
                    });
                }
            });

            alertDialog.show();
        }

    private void SendDataCleanEvent() {
        final webservice apiService = retrofitallpost.create(webservice.class);
        Call<CleanData> call = apiService.cleanData();
        call.enqueue(new Callback<CleanData>() {
            @Override
            public void onResponse(Call<CleanData> call, Response<CleanData> response) {
                CleanData data = response.body();

                boolean result = data.isResult();
                if(result == true)
                {
                    Toast.makeText(getApplicationContext(), "Reset Successfull", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Already Empty", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<CleanData> call, Throwable t) {

            }
        });
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
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
    }
    /**
     * This function add the headers to the table
     **/
    public void addHeaders() {

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());
        tr.addView(getTextView(0, "Agent Name", Color.DKGRAY, Typeface.BOLD, Color.LTGRAY));
        tr.addView(getTextView(0, "Tickets Scanned", Color.DKGRAY, Typeface.BOLD, Color.LTGRAY));
        tl.addView(tr, getTblLayoutParams());
    }

    /**
     * This function add the data to the table
     **/
    public void addData() {
        int numCompanies = name.length();
        TableLayout tl = findViewById(R.id.table);
        for (int i = 0; i < 1; i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(getLayoutParams());
            tr.addView(getTextView(i + 1, name, Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorRow)));
            tr.addView(getTextView(i + 1, total, Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorRow)));
            tl.addView(tr, getTblLayoutParams());
        }
    }


    private void GetAllEvents() {
        final ArrayList<String> data = new ArrayList<String>();

        final List<String> dataId = new ArrayList<>();
        final List<String> dataName = new ArrayList<>();

        final webservice apiService = retrofitallpost.create(webservice.class);
        Call<BasicJson> call = apiService.getAllEvents();

        call.enqueue(new Callback<BasicJson>() {
            @Override
            public void onResponse(Call<BasicJson> call, Response<BasicJson> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        for (AllActiveEvents eventdata : response.body().getData()) {
                            System.out.println(eventdata.toString());
                            data.add(eventdata.getName());

                            dataId.add(eventdata.getId());
                            dataName.add(eventdata.getName());

                        }
                        data.add(0,"Select Event");
                        dataId.add(0,"0");
                        dataName.add(0,"0");
                        //showResult.setVisibility(View.GONE);
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AdminDashboard.this, android.R.layout.simple_dropdown_item_1line, data);

                        //materialDesignSpinner.setAdapter(spinnerArrayAdapter);
                        spinner.setAdapter(spinnerArrayAdapter);
                     //   spinner.setSelection(null);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if(position > 0) {
                                    tl.removeAllViews();
                                    totaltxt.setVisibility(View.VISIBLE);
                                    ticketsoldtxt.setVisibility(View.VISIBLE);
                                    totalattendees.setVisibility(View.VISIBLE);
                                    ticketsold.setVisibility(View.VISIBLE);

                                    totaltxt.setText(null);
                                    ticketsoldtxt.setText(null);
                                    // showResult.setVisibility(View.GONE);
                                    selectedItemText = spinner.getSelectedItem().toString();
                                    if (dataName.contains(selectedItemText)) {
                                        // true
                                        int index = dataName.indexOf(selectedItemText);
                                        eventId = dataId.get(index);
                                        GetBookingDetailsByEventId();
                                    }
                                }
                                else
                                {
                                    tl.removeAllViews();
                                    totaltxt.setVisibility(View.GONE);
                                    ticketsoldtxt.setVisibility(View.GONE);
                                    totalattendees.setVisibility(View.GONE);
                                    ticketsold.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }
                }
                else {
                    Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BasicJson> call, Throwable t) {
                Log.i("Error",t.getMessage());
            }
        });
    }
    private void GetBookingDetailsByEventId() {

        final webservice apiService = retrofitallpost.create(webservice.class);
        Call<BasicJsonBooking> call = apiService.getBookingDetailsByEventId(eventId);
        call.enqueue(new Callback<BasicJsonBooking>() {
            @Override
            public void onResponse(Call<BasicJsonBooking> call, Response<BasicJsonBooking> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        AllEvents responsedata = response.body().getData();

                        String ticketsold = responsedata.getTicketSoldByEventId();
                        if(ticketsold == null)
                        {
                            ticketsoldtxt.setText("Not Found");
                        }
                        ticketsoldtxt.setText(ticketsold);

                        int totalattendees = responsedata.getTotalAttendeesNow();
                        if(totalattendees == 0)
                        {
                            totaltxt.setText("Not Found");
                        }
                        totaltxt.setText(String.valueOf(totalattendees));

                        List<AgentData> d = responsedata.getAgentData();
                        if(d.isEmpty()){
                            tl.removeAllViews();
                        }
                        else
                        {
                            addHeaders();

                               for (AgentData agentData : responsedata.getAgentData()) {
                                    name = agentData.getName();
                                    total = agentData.getTotal();
                                    addData();
                                }



                        }
                    }
                } else {
                    Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<BasicJsonBooking> call, Throwable t) {
                Log.i("Error", t.getMessage());
            }
        });
    }
   
}
