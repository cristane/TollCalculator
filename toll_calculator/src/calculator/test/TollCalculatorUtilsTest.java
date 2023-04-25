package calculator.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import calculator.TollCalculatorUtils;
import interchanges.Location;
import interchanges.LocationDetails;
import interchanges.Route;

public class TollCalculatorUtilsTest {
	
	private static List<Location> locationList = new ArrayList<>();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//Create mock json objects
        String json1 = "{ \"name\": \"QEW\", \"routes\": [ { \"toId\": \"2\", \"distance\": \"6.062\"} ] }";
        String json2 = "{ \"name\": \"Dundas Street\", \"routes\": [ { \"toId\": \"3\", \"distance\": \"3.847\"}, { \"toId\": \"1\", \"distance\": \"6.062\"} ] }";

        // Mock expected values
        LocationDetails loc1 = new LocationDetails();
        loc1.setName("QEW");
        Route route1 = new Route();
        route1.setToId(2);
        route1.setDistance(6.062);
        List<Route> routes1 = new ArrayList<>();
        routes1.add(route1);
        loc1.setRoutes(routes1);

        LocationDetails loc2 = new LocationDetails();
        loc1.setName("Dundas Street");
        route1 = new Route();
        route1.setToId(1);
        route1.setDistance(6.062);
        Route route2 = new Route();
        route1.setToId(3);
        route1.setDistance(3.847);
        List<Route> routes2 = new ArrayList<>();
        routes2.add(route1);
        routes2.add(route2);
        loc2.setRoutes(routes2);
        
        //Parse json string into LocationDetails object
		Gson gson = new Gson();
		LocationDetails locDetails1 = gson.fromJson((String) json1, LocationDetails.class);
		LocationDetails locDetails2 = gson.fromJson((String) json2, LocationDetails.class);

		//Assert LocationDetails values
		assertEquals("QEW", locDetails1.getName());
		assertEquals("Dundas Street", locDetails2.getName());
		
		//Set mocked locations to object
		Location location1 = new Location();
		location1.setLocationId("1");
		location1.setLocationDetails(locDetails1);
		Location location2 = new Location();
		location2.setLocationId("2");
		location2.setLocationDetails(locDetails2);

		locationList.add(location1);
		locationList.add(location2);
	}

	@Test
	public void testFindUserLocation() {
		String locStr = "Dundas Street";
		
		//Expected
        String json2 = "{ \"name\": \"Dundas Street\", \"routes\": [ { \"toId\": \"3\", \"distance\": \"3.847\"}, { \"toId\": \"1\", \"distance\": \"6.062\"} ] }";
		Gson gson = new Gson();
		LocationDetails locDetails2 = gson.fromJson((String) json2, LocationDetails.class);
		Location location = new Location();
		location.setLocationId("2");
		location.setLocationDetails(locDetails2);
		
		//Actual
		Location actualLocation = TollCalculatorUtils.findUserLocation(locStr, locationList);
		
		//Assertions
		assertEquals(actualLocation.getLocationId(), location.getLocationId());
		assertEquals(actualLocation.getLocationDetails().getName(), location.getLocationDetails().getName());
	}

	@Test
	public void testCalculateDistance() {
		//Expected
		double expDistance = 6.062;
		
		//Actual
		double distance = TollCalculatorUtils.calculateDistance(locationList.get(0), locationList.get(1), locationList);
		
		//Assertion
		assertEquals(distance, expDistance, 0.001);
	}

	@Test
	public void testCalculateCost() {
		//Expected
		String expCost = "$1.52";
		
		//Actual
		double distance = TollCalculatorUtils.calculateDistance(locationList.get(0), locationList.get(1), locationList);
		String cost = TollCalculatorUtils.calculateCost(distance, "0.25");
		
		//Assertion
		assertEquals(expCost, cost);
	}

}
