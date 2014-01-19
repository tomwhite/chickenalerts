package com.tom_e_white.chickenalerts;

import java.util.Calendar;
import java.util.TimeZone;

import junit.framework.TestCase;

public class AlertTimeCalculatorTest extends TestCase {

	public void testToday() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.set(Calendar.YEAR, 2013);
		cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		cal.set(Calendar.DATE, 3);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		AlertTimeCalculator calculator = new AlertTimeCalculator();
		Calendar nextAlert = calculator.calculateNextAlert(cal,
		        SunsetDefinition.OFFICIAL, 45, "51.868383,-3.151789");

		assertEquals(nextAlert.get(Calendar.YEAR), 2013);
		assertEquals(nextAlert.get(Calendar.MONTH), Calendar.FEBRUARY);
		assertEquals(nextAlert.get(Calendar.DATE), 3);
		assertEquals(nextAlert.get(Calendar.HOUR_OF_DAY), 17);
		assertEquals(nextAlert.get(Calendar.MINUTE), 49);
	}

	public void testTomorrow() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.set(Calendar.YEAR, 2013);
		cal.set(Calendar.MONTH, Calendar.FEBRUARY);
		cal.set(Calendar.DATE, 3);
		cal.set(Calendar.HOUR_OF_DAY, 18);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		AlertTimeCalculator calculator = new AlertTimeCalculator();
		Calendar nextAlert = calculator.calculateNextAlert(cal,
		        SunsetDefinition.OFFICIAL, 45, "51.868383,-3.151789");

		assertEquals(nextAlert.get(Calendar.YEAR), 2013);
		assertEquals(nextAlert.get(Calendar.MONTH), Calendar.FEBRUARY);
		assertEquals(nextAlert.get(Calendar.DATE), 4); // next day
		assertEquals(nextAlert.get(Calendar.HOUR_OF_DAY), 17);
		assertEquals(nextAlert.get(Calendar.MINUTE), 51); // sunset is 2 mins
														  // later
	}
}
