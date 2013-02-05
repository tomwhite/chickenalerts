package com.tom_e_white.chickenalerts;

import java.util.Calendar;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

public class AlertTimeCalculator {
	
	private Location location = new Location("51.868383", "-3.151789"); // Crickhowell

	/**
	 * Calculate the time of the next alert after {@link cal}. 
	 * @param cal
	 * @return
	 */
	public Calendar calculateNextAlert(Calendar cal, int offsetInMinutes) {
		SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location,
				cal.getTimeZone());
		Calendar alertTime = calculator.getOfficialSunsetCalendarForDate(cal);
		alertTime.add(Calendar.MINUTE, offsetInMinutes);
		// advance a day if alert time has already passed for today
		if (!alertTime.after(cal)) {
			Calendar calCopy = Calendar.getInstance(cal.getTimeZone());
			calCopy.setTime(cal.getTime());
			calCopy.add(Calendar.DATE, 1);
			alertTime = calculator.getOfficialSunsetCalendarForDate(calCopy);
			alertTime.add(Calendar.MINUTE, offsetInMinutes);
		}
		return alertTime;
	}
}
