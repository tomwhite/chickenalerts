package com.tom_e_white.chickenalerts;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void onToggleClicked(View view) {
	    boolean on = ((Switch) view).isChecked();
	    if (on) {
	    	enableAlerts();
	    } else {
	    	disableAlerts();
	    }
	}
	
	public void testAlert(View view) {
		AlertScheduler scheduler = new AlertScheduler();
		scheduler.scheduleTestAlert(getApplicationContext());		
	}

	private void enableAlerts() {
		saveSettings(true);

		// Schedule next alert
		Calendar now = Calendar.getInstance();
		AlertScheduler scheduler = new AlertScheduler();
		Calendar nextAlert = scheduler.scheduleNextAlert(getApplicationContext(), now);
		
		// Tell user when next alert was scheduled for
		DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
		boolean today = now.get(Calendar.DATE) == nextAlert.get(Calendar.DATE);
		String text = getString(today ? R.string.next_alert_today : R.string.next_alert_tomorrow,
				timeFormat.format(nextAlert.getTime()));
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	
	private void disableAlerts() {
		saveSettings(false);
		
		// Cancel next alert
		AlertScheduler scheduler = new AlertScheduler();
		scheduler.cancelNextAlert(getApplicationContext());		
	}

	private void saveSettings(boolean on) {
		SharedPreferences prefs = getSharedPreferences("com.tom_e_white.chickenalerts", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean("enabled", on);
		editor.commit();	
	}
}
