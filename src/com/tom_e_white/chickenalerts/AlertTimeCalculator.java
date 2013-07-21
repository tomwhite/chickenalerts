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
	public Calendar calculateNextAlert(Calendar cal, SunsetDefinition sunsetDefinition, int offsetInMinutes) {
		SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location,
				cal.getTimeZone());
		Calendar alertTime = getSunsetCalendar(cal, sunsetDefinition, calculator);
		alertTime.add(Calendar.MINUTE, offsetInMinutes);
		// advance a day if alert time has already passed for today
		if (!alertTime.after(cal)) {
			Calendar calCopy = Calendar.getInstance(cal.getTimeZone());
			calCopy.setTime(cal.getTime());
			calCopy.add(Calendar.DATE, 1);
			alertTime = getSunsetCalendar(calCopy, sunsetDefinition, calculator);
			alertTime.add(Calendar.MINUTE, offsetInMinutes);
		}
		return alertTime;
	}
	
	private Calendar getSunsetCalendar(Calendar cal, SunsetDefinition sunsetDefinition, SunriseSunsetCalculator calculator) {
		switch (sunsetDefinition) {
		case OFFICIAL:
			return calculator.getOfficialSunsetCalendarForDate(cal);
		case CIVIL:
		default:
			return calculator.getCivilSunsetCalendarForDate(cal);
		}		
	}
}
