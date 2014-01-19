package com.tom_e_white.chickenalerts;

import android.location.Location;

public class LocationUtil {

	public static String toLocationString(double latitude, double longitude) {
		return Location.convert(latitude, Location.FORMAT_DEGREES) + ","
		        + Location.convert(longitude, Location.FORMAT_DEGREES);
	}

	public static String toLocationString(Location location) {
		return Location
		        .convert(location.getLatitude(), Location.FORMAT_DEGREES)
		        + ","
		        + Location.convert(location.getLongitude(),
		                Location.FORMAT_DEGREES);
	}

	public static Location fromLocationString(String locationString) {
		Location location = new Location((String) null);
		String[] parts = locationString.split(",");
		location.setLatitude(Location.convert(parts[0]));
		location.setLongitude(Location.convert(parts[1]));
		return location;
	}

}
