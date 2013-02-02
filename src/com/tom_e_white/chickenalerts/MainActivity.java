package com.tom_e_white.chickenalerts;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void startAlert(View view) {
		Intent intent = new Intent(this, ChickenAlertReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				this.getApplicationContext(), 234324243, intent, 0);
		Calendar now = Calendar.getInstance();
		AlertTimeCalculator calculator = new AlertTimeCalculator();
		Calendar nextAlert = calculator.calculateNextAlert(now);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, nextAlert.getTimeInMillis(), pendingIntent);
		DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
		boolean today = now.get(Calendar.DATE) == nextAlert.get(Calendar.DATE);
		Toast.makeText(this, "Next Chicken Alert is at " + timeFormat.format(nextAlert.getTime()) + (today ? "" : " tomorrow"),
				Toast.LENGTH_LONG).show();
	}
	

}
