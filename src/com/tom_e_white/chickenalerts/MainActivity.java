package com.tom_e_white.chickenalerts;

import static com.tom_e_white.chickenalerts.ChickenConstants.*;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
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
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		fragment.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		fragment = null;
	}
	
	public void testAlert(View view) {
		AlertScheduler scheduler = new AlertScheduler();
		scheduler.scheduleTestAlert(getApplicationContext());		
	}

	private void enableAlerts(int delay) {
		// Schedule next alert
		Calendar now = Calendar.getInstance();
		AlertScheduler scheduler = new AlertScheduler();
		Calendar nextAlert = scheduler.scheduleNextAlert(getApplicationContext(), delay, now);
		
		// Tell user when next alert was scheduled for
		DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
		boolean today = now.get(Calendar.DATE) == nextAlert.get(Calendar.DATE);
		String text = getString(today ? R.string.next_alert_today : R.string.next_alert_tomorrow,
				timeFormat.format(nextAlert.getTime()));
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	
	private void disableAlerts() {
		// Cancel next alert
		AlertScheduler scheduler = new AlertScheduler();
		scheduler.cancelNextAlert(getApplicationContext());		
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(PREF_ENABLED)) {
			Preference pref = fragment.findPreference(key);
			boolean on = sharedPreferences.getBoolean(PREF_ENABLED, false);
		    if (on) {
		    	pref.setSummary(getString(R.string.pref_enabled));
				int delay = getDelay(sharedPreferences);
		    	enableAlerts(delay);
		    } else {
		    	pref.setSummary(getString(R.string.pref_disabled));
		    	disableAlerts();
		    }
		} else if (key.equals(PREF_DELAY)) {
			Preference pref = fragment.findPreference(key);
			int delay = getDelay(sharedPreferences);
			pref.setSummary(getString(R.string.pref_delay_summary_param, delay));
	    	disableAlerts();
	    	enableAlerts(delay);
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
			Preference pref = fragment.findPreference(PREF_DELAY);
			int delay = getDelay(sharedPreferences);
			pref.setSummary(getString(R.string.pref_delay_summary_param, delay));			
		}
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
	
}
