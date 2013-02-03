package com.tom_e_white.chickenalerts;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AlertReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// Issue the notification
		Uri sound = Uri.parse("android.resource://" + context.getPackageName()
				+ "/" + R.raw.cluck);
		Notification notification = new Notification.Builder(context)
				.setContentTitle(context.getString(R.string.notification_title))
				.setContentText(context.getString(R.string.notification_text))
				.setTicker(context.getString(R.string.notification_text))
				.setSound(sound)
				.setSmallIcon(R.drawable.ic_launcher).build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);
		
		// Schedule next alert
		AlertScheduler scheduler = new AlertScheduler();
		scheduler.scheduleNextAlert(context);
	}

}