package com.tom_e_white.chickenalerts;

import java.util.Calendar;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

public class AlertTimeCalculator {
	
	private Location location = new Location("51.868383", "-3.151789"); // Crickhowell
	private int offsetInMinutes = 30;

	public Calendar calculateNextAlert(Calendar cal) {
		SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, cal.getTimeZone());
		Calendar alertTime = calculator.getOfficialSunsetCalendarForDate(cal);
		alertTime.add(Calendar.MINUTE, offsetInMinutes);
		if (alertTime.before(cal)) {
			Calendar calCopy = Calendar.getInstance(cal.getTimeZone());
			calCopy.setTime(cal.getTime());
			calCopy.add(Calendar.DATE, 1);
			alertTime = calculator.getOfficialSunsetCalendarForDate(calCopy);
			alertTime.add(Calendar.MINUTE, offsetInMinutes);
		}
		
		Calendar calCopy = Calendar.getInstance(cal.getTimeZone());
		calCopy.setTime(cal.getTime());
		calCopy.add(Calendar.SECOND, 5);	
		return calCopy;
//		
//		return alertTime;
	}
}
