package com.tom_e_white.chickenalerts;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class ChickenAlertReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Notification notification = new Notification.Builder(context)
				.setContentTitle("Chicken Alert")
				.setContentText("Have you put the chickens to bed?")
				.setTicker("Have you put the chickens to bed?")
				.setSmallIcon(R.drawable.ic_launcher).build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);
		
		MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.cluck);
		mediaPlayer.start(); // no need to call prepare(); create() does that for you
	}

}