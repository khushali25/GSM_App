package com.example.xps.barcodescanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Model.AllActiveEvents;
import Model.AllEvents;
import Model.BasicJson;
import Model.Ticket;
import Model.UserDetail;
import Service.webservice;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.constraint.Constraints.TAG;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class MainActivity extends Activity implements View.OnClickListener {


    private TextView eventName,duplicatemessage,datetxt,timetxt,barcodeidtxt,bookingidtxt,nametxt,nameuser,barcodeId,bookingId,date,time,status,verify,verifytxt;
    Button failurebtn,successbtn,duplicatebtn,adminloginbtn;
    String message,book,barcodebook,namebook,verifytime,name,event,eventNameToDisplay,verifyby;
    Animation startAnimation;
    Snackbar snackbar;
    private int checkedItem = 0;
    Spinner spinner;
    String selectedItemText;
    String[] splitDateTime;

    private static final int RC_BARCODE_CAPTURE = 9001;
    SharePrefService ap;
    private static final String TAG = "BarcodeMain";
    String usernamelogin = "admin";
    String passwordlogin = "*gsm001";
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    Retrofit retrofitallpost=new Retrofit.Builder()
            .baseUrl("https://gsmweb.montrealgujarati.com/index.php/Gsmapi/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
            .build();

    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES) // connect timeout
            .writeTimeout(1, TimeUnit.MINUTES) // write timeout
            .readTimeout(1, TimeUnit.MINUTES) // read timeout
            .build();
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Intent thisIntent = getIntent();
       
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        }
       // materialDesignSpinner = (MaterialBetterSpinner)
           //     findViewById(R.id.alleventspinnermain);
        spinner = (Spinner)findViewById(R.id.spinnermainactivity);
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
        eventName = (TextView)findViewById(R.id.eventname);
        duplicatemessage = (TextView)findViewById(R.id.duplicatemessage);
        datetxt = (TextView)findViewById(R.id.datetxt);
        timetxt = (TextView)findViewById(R.id.timetxt);
        bookingidtxt = (TextView)findViewById(R.id.bookingidtxt);
        barcodeidtxt = (TextView)findViewById(R.id.barcodetxt);
        nametxt = (TextView)findViewById(R.id.usernametxt);
        status = (TextView)findViewById(R.id.status);
        date = (TextView)findViewById(R.id.date);
        time = (TextView)findViewById(R.id.time);
        verify = (TextView)findViewById(R.id.verifyby);
        nameuser = (TextView)findViewById(R.id.username);
        barcodeId = (TextView)findViewById(R.id.barcode);
        bookingId = (TextView)findViewById(R.id.bookingId);
        verifytxt = (TextView)findViewById(R.id.verifybytxt);

        successbtn = (Button) findViewById(R.id.member);
        failurebtn = (Button) findViewById(R.id.nonmember);
        duplicatebtn = (Button) findViewById(R.id.duplicatemember);

        if(isNetworkConnected()) {
            ap = new SharePrefService(this);
            name = ap.getAccessKey("Username");
            event = ap.getAccessKey("Event");
            eventNameToDisplay = ap.getAccessKey("eventName");
            ap.saveAccessKey("Username", name);
            ap.saveAccessKey("Event", event);
            ap.saveAccessKey("eventName",eventNameToDisplay);



            VisibilityDisabled();
            eventName.setText(eventNameToDisplay);
            // spinner.getEmptyView();

            findViewById(R.id.read_barcode).setOnClickListener(this);
            findViewById(R.id.read_barcode_manual).setOnClickListener(this);
            findViewById(R.id.adminloginbtn).setOnClickListener(this);

            checkedItem = R.id.home;
        }
        GetAllEvents();

         startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinking_animation);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }



    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = null;
        try {
            cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        catch(Exception ex)
        {
           // Crashlytics.logException(ex);
        }
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        try {
            if(isNetworkConnected()) {
               // if (checkedItem == R.id.home) {
                    finish();
               // }

            }
            else{
                snackbarerror();
            }

        }
        catch (Exception ex)
        {
            //Crashlytics.logException(ex);
        }
    }
    public void snackbarerror()
    {
        try {
            LinearLayout relativeLayout = (LinearLayout) findViewById(R.id.relativelayout);
            snackbar = Snackbar
                    .make(relativeLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isNetworkConnected())
                                snackbar.dismiss();
                            else
                                snackbarerror();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
        catch(Exception ex)
        {
           // Crashlytics.logException(ex);
        }
    }
    private void GetAllEvents() {
        if(isNetworkConnected()) {
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

                            }

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, data);
                            spinner.setAdapter(spinnerArrayAdapter);
                            int spinnerPosition = spinnerArrayAdapter.getPosition(eventNameToDisplay);

                            //set the default according to value
                            spinner.setSelection(spinnerPosition);

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    selectedItemText = spinner.getSelectedItem().toString();
                                    if (dataName.contains(selectedItemText)) {
                                        // true
                                        int index = dataName.indexOf(selectedItemText);
                                        event = dataId.get(index);
                                        ap.saveAccessKey("Event", event);
                                        eventName.setText(selectedItemText);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });



                        }

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }


                @Override
                public void onFailure(Call<BasicJson> call, Throwable t) {
                    Log.i("Error", t.getMessage());
                }
            });
        }
        else {
            snackbarerror();
        }
    }
    private void CheckTicket(final String ticket,final String username,final String event) {

        final webservice apiService = retrofitallpost.create(webservice.class);
        Call<Ticket> call = apiService.checkTicket(ticket,username,event);
        call.enqueue(new Callback<Ticket>() {

            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Ticket ticketid = response.body();
                        boolean result = ticketid.isResult();
                        message = ticketid.getMessage();
                        verifyby = ticketid.getVerifyBy();
                        if(verifyby.isEmpty())
                        {
                            verifyby = "N/A";
                        }
                        List<UserDetail> user = response.body().getUserDetail();
                        if(user.isEmpty())
                        {
                            namebook = "N/A";
                            barcodebook = "N/A";
                            book = "N/A";

                        }
                        else {
                            for (UserDetail userdetail : response.body().getUserDetail()) {

                                namebook = userdetail.getName();
                                barcodebook = userdetail.getBarcode_id();
                                book = userdetail.getBooking_id();
                            }
                        }

                        verifytime = ticketid.getVerifyTime();

                        splitDateTime = verifytime.split(" ");

                        if (result == true) {
                            SuccessIcon();
                        } else if (result == false) {
                            FailureIcon();
                        } else if (result == false && message == "Ticket not found") {
                            FailureIcon();
                        } else {
                            DuplicateIcon();
                        }

                        Log.e(TAG, "Success");
                    }
                }
            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t)
            {

                Log.e(TAG, t.getMessage());
            }

        });
        //nametxt.setText("Ticket not valid");
    }

    private void VisibilityEnable()
    {
        status.setVisibility(View.VISIBLE);
        date.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);
        bookingId.setVisibility(View.VISIBLE);
        nameuser.setVisibility(View.VISIBLE);
        barcodeId.setVisibility(View.VISIBLE);
        verify.setVisibility(View.VISIBLE);

        duplicatemessage.setVisibility(View.VISIBLE);
        datetxt.setVisibility(View.VISIBLE);
        timetxt.setVisibility(View.VISIBLE);
        bookingidtxt.setVisibility(View.VISIBLE);
        barcodeidtxt.setVisibility(View.VISIBLE);
        nametxt.setVisibility(View.VISIBLE);
        verifytxt.setVisibility(View.VISIBLE);

            nametxt.setText(namebook);
            barcodeidtxt.setText(barcodebook);
            bookingidtxt.setText(book);

        duplicatemessage.setText(message);
        verifytxt.setText(verifyby);

        if(verifytime.isEmpty())
        {
            datetxt.setText("N/A");
            timetxt.setText("N/A");
        }
        else {
            datetxt.setText(splitDateTime[0]);
            timetxt.setText(splitDateTime[1]);
        }

    }

    private void VisibilityDisabled()
    {
        status.setVisibility(View.GONE);
        date.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
        bookingId.setVisibility(View.GONE);
        nameuser.setVisibility(View.GONE);
        barcodeId.setVisibility(View.GONE);
        verify.setVisibility(View.GONE);

        duplicatemessage.setVisibility(View.GONE);
        datetxt.setVisibility(View.GONE);
        timetxt.setVisibility(View.GONE);
        bookingidtxt.setVisibility(View.GONE);
        barcodeidtxt.setVisibility(View.GONE);
        nametxt.setVisibility(View.GONE);
        verifytxt.setVisibility(View.GONE);

        failurebtn.setVisibility(View.GONE);
        duplicatebtn.setVisibility(View.GONE);
        successbtn.setVisibility(View.GONE);

    }

    private void FailureIcon() {

        successbtn.setVisibility(View.GONE);
        duplicatebtn.setVisibility(View.GONE);
        failurebtn.setVisibility(View.VISIBLE);
        VisibilityEnable();
        failurebtn.startAnimation(startAnimation);
        try {
            RingtoneManager.getRingtone(getApplicationContext(),Uri.parse("android.resource://com.example.xps.barcodescanner/"+R.raw.sound)).play();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void DuplicateIcon() {
        successbtn.setVisibility(View.GONE);
        failurebtn.setVisibility(View.GONE);
        duplicatebtn.setVisibility(View.VISIBLE);
        VisibilityEnable();
        duplicatebtn.startAnimation(startAnimation);
        try {
            RingtoneManager.getRingtone(getApplicationContext(),Uri.parse("android.resource://com.example.xps.barcodescanner/"+R.raw.sound)).play();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void SuccessIcon() {
        failurebtn.setVisibility(View.GONE);
        duplicatemessage.setVisibility(View.VISIBLE);
        successbtn.setVisibility(View.VISIBLE);
        VisibilityEnable();
        successbtn.startAnimation(startAnimation);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.
            VisibilityDisabled();
           // barcodeValue.setVisibility(View.GONE);
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            //intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
            //intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());

            if (intent.resolveActivity(getPackageManager()) != null) {
                // Bring up gallery to select a photo
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
        }
        if (v.getId() == R.id.read_barcode_manual) {
            // launch barcode activity.
           VisibilityDisabled();
           // barcodeValue.setVisibility(View.GONE);
            showManualScanPopup(this);
        }
        if (v.getId() == R.id.adminloginbtn) {

            String username = ap.getAccessKey("AdminLoginUserName");
            String password = ap.getAccessKey("AdminLoginPassword");

            if(username.isEmpty() && password.isEmpty())
            {
                showAdminLoginPopup(this);

            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), AdminDashboard.class);
                startActivity(intent);
              //  alertDialog.dismiss();
            }
        }

    }
    public void showManualScanPopup(final Context context) {
        final AlertDialog alertDialog;

        LayoutInflater layoutinflater = LayoutInflater.from(context);
        final View promptView = layoutinflater.inflate(R.layout.manual_scan_popup, null);
        alertDialog = new AlertDialog.Builder(context)
                .setView(promptView)
                .setTitle("Manual Scan")
                .setPositiveButton("Submit", null)
                .setNegativeButton("Cancel", null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button buttonPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        EditText ticketNumber = promptView.findViewById(R.id.ticketnumber);
                        TextView showResult = (TextView) promptView.findViewById(R.id.result);
                        final String userValue = ticketNumber.getText().toString();

                        if (userValue.equals("")) {
                            try {
                                showResult.setText("Please enter ticket number");
                                showResult.setVisibility(View.VISIBLE);
                            } catch (Exception ex) {
                                //Crashlytics.logException(ex);
                                new Error(ex.getMessage());
                            }
                        }  else {

                            CheckTicket(userValue,name,event);
//                            PrefService ap = new PrefService(context);
//                            ap.saveAccessKey("subscribe", userValue);
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
                    }
                });
            }
        });

        alertDialog.show();
    }
    public void showAdminLoginPopup(final Context context) {

        LayoutInflater layoutinflater = LayoutInflater.from(context);
        final View promptView = layoutinflater.inflate(R.layout.admin_login_popup, null);
        alertDialog = new AlertDialog.Builder(context)
                .setView(promptView)
                .setTitle("Log In")
                .setPositiveButton("Log in", null)
                .setNegativeButton("Cancel", null)
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button buttonPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        EditText username = promptView.findViewById(R.id.usernameedt);
                        EditText password = promptView.findViewById(R.id.passwordedt);
                        TextView showResult = (TextView) promptView.findViewById(R.id.error);
                        final String userName = username.getText().toString();
                        final String userPassword = password.getText().toString();

                        if ((userName.equals("") && userPassword.equals(""))) {
                            try {
                                showResult.setText("Please enter username and password");
                                showResult.setVisibility(View.VISIBLE);
                            } catch (Exception ex) {
                                //Crashlytics.logException(ex);
                                new Error(ex.getMessage());
                            }
                        } else if (userName.equals(usernamelogin) && userPassword.equals(passwordlogin) ) {
                            try {
                                showResult.setVisibility(View.GONE);
                                ap.saveAccessKey("AdminLoginUserName", usernamelogin);
                                ap.saveAccessKey("AdminLoginPassword", passwordlogin);
                                Intent intent = new Intent(getApplicationContext(), AdminDashboard.class);
                                startActivity(intent);
                                alertDialog.dismiss();

                            } catch (Exception ex) {
                                //Crashlytics.logException(ex);
                                new Error(ex.getMessage());
                            }
                        }
                        else if (!(userName.equals(usernamelogin) && userPassword.equals(passwordlogin)) ) {
                            try {
                                showResult.setText("Please enter valid username and password");
                                showResult.setVisibility(View.VISIBLE);
                            } catch (Exception ex) {
                                //Crashlytics.logException(ex);
                                new Error(ex.getMessage());
                            }
                        }
                        else {

                           // CheckTicket(ticketNumber.getText().toString());
//                            PrefService ap = new PrefService(context);
//                            ap.saveAccessKey("subscribe", userValue);
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
                    }
                });
            }
        });

        alertDialog.show();
    }


    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    CheckTicket(barcode.rawValue,name,event);
                  //  statusMessage.setText(R.string.barcode_success);
                    //barcodeValue.setText(barcode.valueAt(0).displayValue);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);



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