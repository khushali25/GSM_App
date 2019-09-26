package com.example.xps.barcodescanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Model.AllActiveEvents;
import Model.BasicJson;
import Service.webservice;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText edtname;
    AppCompatButton btnsubmit;
    TextView showResult;
    //MaterialBetterSpinner materialDesignSpinner;
    Spinner spinner;
    String selectedItemText;
    Bundle bundle = new Bundle();
    private static final int REQUEST = 112;
    final String PREFS_NAME = "MyPrefsFile2";
    final String PREFS_FIRST_RUN = "first_run2";
    String eventId;

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
        setContentView(R.layout.activity_first);

        spinner = (Spinner)findViewById(R.id.spinnerfirstactivity);
        spinner.setDropDownWidth(900);

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(800);

        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        edtname = (TextInputEditText)findViewById(R.id.name_edit_text);
        edtname.setVisibility(View.GONE);
        checkFirstRun();
        showResult = (TextView)findViewById(R.id.result);
        btnsubmit = (AppCompatButton)findViewById(R.id.btnsubmit);

        GetAllActiveEvents();

        btnsubmit.setOnClickListener(this);

    }
    private void GetAllActiveEvents() {
        final ArrayList<String> data = new ArrayList<String>();

        final List<String> dataId = new ArrayList<>();
        final List<String> dataName = new ArrayList<>();


        final webservice apiService = retrofitallpost.create(webservice.class);
        Call<BasicJson> call = apiService.getEventsForScanning();
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

                            //   data.add(size.getId());
                        }

                        data.add(0,"Select Event");
                        dataId.add(0,"0");
                        dataName.add(0,"0");
                        showResult.setVisibility(View.GONE);
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(FirstActivity.this, android.R.layout.simple_dropdown_item_1line, data);
                       // materialDesignSpinner.setAdapter(spinnerArrayAdapter);
                        spinner.setAdapter(spinnerArrayAdapter);


                        try {
//                            materialDesignSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    showResult.setVisibility(View.GONE);
//                                    selectedItemText = materialDesignSpinner.getText().toString();
//                                    if (dataName.contains(selectedItemText)) {
//                                        // true
//                                        int index = dataName.indexOf(selectedItemText);
//                                        eventId = dataId.get(index);
//
//                                    }
//                                }
//                            });
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                         @Override
                         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                             if(position > 0){
                                 // get spinner value

                                 showResult.setVisibility(View.GONE);

                                 selectedItemText = spinner.getSelectedItem().toString();
                                 if (dataName.contains(selectedItemText)) {
                                     // true
                                     int index = dataName.indexOf(selectedItemText);
                                     eventId = dataId.get(index);
                                 }
                             }else{
                                 // show toast select gender
                                 selectedItemText = "null";
                                 eventId = "null";

                             }

                         }

                         @Override
                          public void onNothingSelected(AdapterView<?> parent) {

                             }
                        });
                        } catch (Exception ex) {
                            Log.i("onEmptyResponse", ex.getMessage());
                        }
                    }
                     else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<BasicJson> call, Throwable t) {
                Log.i("Error",t.getMessage());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {

                super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == 1) {
                    SharedPreferences settings = getSharedPreferences("prefs", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("firstRun", true);
                    editor.commit();
                }

        }
        catch(Exception ex)
        {
           // Crashlytics.logException(ex);
        }
        Log.e("data", data.toString());
    }
    private void checkFirstRun() {
        try
        {
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            // If the app is launched for first time, view splash screen and setup 'Next >>' link.
            if (sharedPreferences.getBoolean(PREFS_FIRST_RUN, true)) {
               // sharedPreferences.edit().putBoolean(PREFS_FIRST_RUN, false).apply();

                edtname.setVisibility(View.VISIBLE);
                showResult.setVisibility(View.VISIBLE);
                // Record that user have done first run.

            } else {
                edtname.setVisibility(View.GONE);
                showResult.setVisibility(View.GONE);

            }
        }
        catch(Exception ex)
        {
            //Crashlytics.logException(ex);
        }
    }
    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharePrefService ap = new SharePrefService(getApplicationContext());

        if (sharedPreferences.getBoolean(PREFS_FIRST_RUN, true))
        {

            String name = edtname.getText().toString();
            if (name.isEmpty() && eventId == null) {
                showResult.setText("Please enter name and select event");
                showResult.setVisibility(View.VISIBLE);
            }
            else if (name.isEmpty()) {
                showResult.setText("Please enter name");
                showResult.setVisibility(View.VISIBLE);
            }
            else if(selectedItemText == "null")
            {
                showResult.setText("Please select event");
                showResult.setVisibility(View.VISIBLE);
            }
            else if (eventId == null) {
                showResult.setText("Please select event");
                showResult.setVisibility(View.VISIBLE);
            }
            else {
                showResult.setVisibility(View.GONE);
                sharedPreferences.edit().putBoolean(PREFS_FIRST_RUN, false).apply();
                ap.saveAccessKey("Username", name);
                ap.saveAccessKey("Event", eventId);
                ap.saveAccessKey("eventName", selectedItemText);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(selectedItemText,"eventName");
                startActivity(intent);
              //  finish();
            }
        }
        else
            {
                if(selectedItemText == "null")
                {
                    showResult.setText("Please select event");
                    showResult.setVisibility(View.VISIBLE);
                }

                else {
                    showResult.setVisibility(View.GONE);
                    ap.saveAccessKey("Event", eventId);
                    ap.saveAccessKey("eventName", selectedItemText);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                 //   finish();
                }
            }


        }

}

