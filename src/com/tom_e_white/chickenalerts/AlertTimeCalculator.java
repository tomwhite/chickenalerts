package com.tom_e_white.chickenalerts;

import java.util.Calendar;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

public class AlertTimeCalculator {
	
	private Location location = new Location("51.868383", "-3.151789"); // Crickhowell
	private int offsetInMinutes = 45;

	/**
	 * Calculate the time of the next alert after {@link cal}. 
	 * @param cal
	 * @return
	 */
	public Calendar calculateNextAlert(Calendar cal) {
		SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location,
				cal.getTimeZone());
		Calendar alertTime = calculator.getOfficialSunsetCalendarForDate(cal);
		alertTime.add(Calendar.MINUTE, offsetInMinutes);
		if (!alertTime.after(cal)) {
			Calendar calCopy = Calendar.getInstance(cal.getTimeZone());
			calCopy.setTime(cal.getTime());
			calCopy.add(Calendar.DATE, 1);
			alertTime = calculator.getOfficialSunsetCalendarForDate(calCopy);
			alertTime.add(Calendar.MINUTE, offsetInMinutes);
		}
		
		// TODO: remove - this is for testing
		Calendar calCopy = Calendar.getInstance(cal.getTimeZone());
		calCopy.setTime(cal.getTime());
		calCopy.add(Calendar.SECOND, 10);	
		return calCopy;
//		
//		return alertTime;
	}
}
