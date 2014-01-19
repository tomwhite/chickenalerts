package com.tom_e_white.chickenalerts;

import static com.tom_e_white.chickenalerts.ChickenConstants.*;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
	
	private SettingsFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onResume() {
		super.onResume();
		fragment = (SettingsFragment) getFragmentManager().findFragmentById(R.id.frag);
		fragment.getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		setSummaries(fragment.getPreferenceScreen().getSharedPreferences());
		
		Preference updateLocationButton = (Preference) fragment.getPreferenceScreen().findPreference(ChickenConstants.UPDATE_LOCATION_BUTTON);
		updateLocationButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference pref) {
    			String newLocationString = "51.4791,0.0003"; // TODO: get from location client
    			Preference locationPref = fragment.findPreference(PREF_LOCATION);
    			locationPref.setSummary(newLocationString);
    			locationPref.getEditor().putString(PREF_LOCATION, newLocationString).apply();
    			Toast.makeText(MainActivity.this, "Location set to " + newLocationString, Toast.LENGTH_LONG).show();
    			updateAlertsIfNeeded();
                return true;
            }
        });

		Preference testButton = (Preference) fragment.getPreferenceScreen().findPreference(ChickenConstants.TEST_BUTTON);
		testButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference pref) { 
        		AlertScheduler scheduler = new AlertScheduler();
        		scheduler.scheduleTestAlert(getApplicationContext());		
                return true;
            }
        });

	}
	
	@Override
	protected void onPause() {
		super.onPause();
		fragment.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		fragment = null;
	}

	private void enableAlerts() {
		if (BuildConfig.DEBUG)
			Log.i(TAG, "enableAlerts");
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		if (BuildConfig.DEBUG)
			Log.i(TAG, "sharedPreferences " + sharedPreferences.getAll());
		SunsetDefinition sunsetDefinition = getSunsetDefinition(sharedPreferences);
		int delay = getDelay(sharedPreferences);
		String location = getLocation(sharedPreferences);
		
		// Schedule next alert
		Calendar now = Calendar.getInstance();
		AlertScheduler scheduler = new AlertScheduler();
		Calendar nextAlert = scheduler.scheduleNextAlert(getApplicationContext(), sunsetDefinition, delay, location, now);
		
		// Tell user when next alert was scheduled for
		DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
		boolean today = now.get(Calendar.DATE) == nextAlert.get(Calendar.DATE);
		String text = getString(today ? R.string.next_alert_today : R.string.next_alert_tomorrow,
				timeFormat.format(nextAlert.getTime()));
		sharedPreferences.edit().putString(PREF_NEXT_ALERT, text).apply();
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	
	private void disableAlerts() {
		if (BuildConfig.DEBUG)
			Log.i(TAG, "disableAlerts");
		// Cancel next alert
		AlertScheduler scheduler = new AlertScheduler();
		scheduler.cancelNextAlert(getApplicationContext());
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPreferences.edit().remove(PREF_NEXT_ALERT).apply();
	}
	
	private void updateAlertsIfNeeded() {
		if (BuildConfig.DEBUG)
			Log.i(TAG, "updateAlertsIfNeeded");
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean on = sharedPreferences.getBoolean(PREF_ENABLED, false);
	    if (on) {
	    	disableAlerts();
	    	enableAlerts();
	    }
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(PREF_ENABLED)) {
			Preference pref = fragment.findPreference(key);
			boolean on = sharedPreferences.getBoolean(PREF_ENABLED, false);
		    if (on) {
		    	pref.setSummary(getString(R.string.pref_enabled));
		    	enableAlerts();
		    } else {
		    	pref.setSummary(getString(R.string.pref_disabled));
		    	disableAlerts();
		    }
		} else if (key.equals(PREF_SUNSET)) {
			ListPreference pref = (ListPreference) fragment.findPreference(key);
			String sunsetDefinition = sharedPreferences.getString(PREF_SUNSET, DEFAULT_SUNSET);
			pref.setSummary(pref.getEntries()[pref.findIndexOfValue(sunsetDefinition)]);
			updateAlertsIfNeeded();
		} else if (key.equals(PREF_DELAY)) {
			Preference pref = fragment.findPreference(key);
			int delay = getDelay(sharedPreferences);
			pref.setSummary(getString(R.string.pref_delay_summary_param, delay));
			updateAlertsIfNeeded();
		} else if (key.equals(PREF_LOCATION)) {
			Preference pref = fragment.findPreference(PREF_LOCATION);
			String location = getLocation(sharedPreferences);
			pref.setSummary(location);			
			updateAlertsIfNeeded();
		}
	}
	
	private void setSummaries(SharedPreferences sharedPreferences) {
		{
			Preference pref = fragment.findPreference(PREF_ENABLED);
			boolean on = sharedPreferences.getBoolean(PREF_ENABLED, false);
		    if (on) {
		    	pref.setSummary(getString(R.string.pref_enabled));
		    } else {
		    	pref.setSummary(getString(R.string.pref_disabled));
		    }
		}
		{
			ListPreference pref = (ListPreference) fragment.findPreference(PREF_SUNSET);
			String sunsetDefinition = sharedPreferences.getString(PREF_SUNSET, DEFAULT_SUNSET);
			pref.setSummary(pref.getEntries()[pref.findIndexOfValue(sunsetDefinition)]);
		}
		{
			Preference pref = fragment.findPreference(PREF_DELAY);
			int delay = getDelay(sharedPreferences);
			pref.setSummary(getString(R.string.pref_delay_summary_param, delay));			
		}
		{
			Preference pref = fragment.findPreference(PREF_LOCATION);
			String location = getLocation(sharedPreferences);
			pref.setSummary(location);			
		}
	}
	
	public static SunsetDefinition getSunsetDefinition(SharedPreferences sharedPreferences) {
		return SunsetDefinition.valueOf(sharedPreferences.getString(PREF_SUNSET, DEFAULT_SUNSET).toUpperCase(Locale.ENGLISH));
	}
	
	public static int getDelay(SharedPreferences sharedPreferences) {
		String delayString = sharedPreferences.getString(PREF_DELAY, DEFAULT_DELAY + "");
		int delay = DEFAULT_DELAY;
		try {
			delay = Integer.parseInt(delayString.trim());
		} catch (NumberFormatException e) {
			// set to default
		}
		return delay;
	}

	public static String getLocation(SharedPreferences sharedPreferences) {
		return sharedPreferences.getString(PREF_LOCATION, DEFAULT_LOCATION);
	}

}
