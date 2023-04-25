package calculator;

import java.text.NumberFormat;
import java.util.List;

import interchanges.Location;
import interchanges.Route;

public class TollCalculatorUtils {

	public static Location findUserLocation (String inputText, List<Location> locationList) {
		for (Location location : locationList) {
			if (location.getLocationId().equals(inputText)) return location;
			if (location.getLocationDetails().getName().equals(inputText)) return location;
		}
		
		System.out.println("Location not recognized. Please try again.");
		return null;
	}
	
	public static double calculateDistance (Location entry, Location exit, List<Location> locationList) {
		double distance = 0.0;
		
		Location newLocation = entry;
		while (newLocation != null && !newLocation.getLocationId().equals(exit.getLocationId())) {
			Route route = getRouteToNextLocation(newLocation, exit, distance, locationList);
			distance += route.getDistance();
			newLocation = findUserLocation(String.valueOf(route.getToId()), locationList);
		}
		
		return distance;
	}
	
	public static String calculateCost (double distance, String price) {
		return NumberFormat.getCurrencyInstance().format(distance * Double.valueOf(price));
	}
	
	private static Route getRouteToNextLocation (Location newPoint, Location exit, Double distance, List<Location> locationList) {
		for (Route route : newPoint.getLocationDetails().getRoutes()) {
			if (Math.abs(route.getToId() - Long.valueOf(exit.getLocationId())) < Math.abs(Long.valueOf(newPoint.getLocationId()) - Long.valueOf(exit.getLocationId()))) {
				//Correct direction of travel
				return route;
			}
		}
		
		return null;
	}
	
}
