package com.example.xps.barcodescanner;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefService {

        private SharedPreferences nameSharedPrefs;
        private SharedPreferences.Editor namePrefsEditor; //Declare Context,Prefrences name and Editor name
        private Context mContext;
        private static final String PREF_NAME = "First_Activity";
        private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

        public SharePrefService(Context context)
        {
            try {
                this.mContext = context;
                nameSharedPrefs = android.preference.PreferenceManager.getDefaultSharedPreferences(mContext);
                namePrefsEditor = nameSharedPrefs.edit();
            }
            catch(Exception ex)
            {
              //  Crashlytics.logException(ex);
            }
        }
        public String getAccessKey(String key) // Return Get the Value
        {
            return nameSharedPrefs.getString(key, "");
        }
        public void saveAccessKey(String key, String value) {
            try {
                namePrefsEditor.putString(key, value);
                namePrefsEditor.commit();
            }
            catch(Exception ex)
            {
               // Crashlytics.logException(ex);
            }
        }
        public void setFirstTimeLaunch(boolean isFirstTime) {
            try {
                namePrefsEditor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
                namePrefsEditor.commit();
            }
            catch(Exception ex)
            {
              //  Crashlytics.logException(ex);
            }
        }


}
