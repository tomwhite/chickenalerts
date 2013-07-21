package com.tom_e_white.chickenalerts;

import static com.tom_e_white.chickenalerts.ChickenConstants.*;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

public class AlertReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (BuildConfig.DEBUG)
			Log.i(TAG, "AlertReceiver received " + intent.getAction());
				
		if (!"android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			// Issue the notification
			if (BuildConfig.DEBUG)
				Log.i(TAG, "Have you put the chickens to bed?");
			Uri sound = Uri.parse("android.resource://" + context.getPackageName()
					+ "/" + R.raw.cluck);
			Notification notification = new Notification.Builder(context)
					.setContentTitle(context.getString(R.string.notification_title))
					.setContentText(context.getString(R.string.notification_text))
					.setTicker(context.getString(R.string.notification_text))
					.setSound(sound)
					.setAutoCancel(true)
					.setSmallIcon(R.drawable.ic_stat_chicken)
					.build();
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(0, notification);
		}
		
		// Schedule next alert (if not a test)
		if (!intent.getBooleanExtra(AlertScheduler.TEST_ALERT, false)) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
			if (BuildConfig.DEBUG)
				Log.i(TAG, "sharedPreferences " + sharedPreferences.getAll());
			AlertScheduler scheduler = new AlertScheduler();
			scheduler.scheduleNextAlert(context, MainActivity.getDelay(sharedPreferences));
		}
	}

}