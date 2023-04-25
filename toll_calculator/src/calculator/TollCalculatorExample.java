package calculator;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;

import interchanges.Location;
import interchanges.LocationDetails;

public class TollCalculatorExample {
	
    private static final String JSON_PATH = "interchanges.json";
    private static final String PRICE_PER_KM = "0.25";

	public static void main(String[] args) {
		
		try {
			//Load data from JSON file
			FileReader fileReader = new FileReader(JSON_PATH);
			JSONTokener tokener = new JSONTokener(fileReader);
			JSONObject root = new JSONObject(tokener);
			JSONObject locations = root.getJSONObject("locations");
			
			//Populate locations list
			List<Location> locationList = new ArrayList<Location>();
			JSONArray locationIDs = locations.names();
			locationIDs.forEach((locationID) -> locationList.add(parseInterchObj((String) locationID, (JSONObject) locations.get((String) locationID))));

			//Get args from user
			Scanner s = new Scanner (System.in);
			
			Location entryLocation = null;
			Location exitLocation = null;
			
			while (entryLocation == null) {
				System.out.printf("Entry point name or id: ");
				String first = s.nextLine();
				entryLocation = TollCalculatorUtils.findUserLocation(first, locationList);
			}

			while (exitLocation == null) {
				System.out.printf("Exit point name or id: ");
				String second = s.nextLine();
				exitLocation = TollCalculatorUtils.findUserLocation(second, locationList);
			}

			System.out.println("Trip: " + entryLocation.getLocationDetails().getName() + " to " + exitLocation.getLocationDetails().getName());

			//Calculate distance
			double distance = TollCalculatorUtils.calculateDistance(entryLocation, exitLocation, locationList);
			System.out.printf("Distance: %.3f kms %n", distance);
			
			//Calculate cost
			System.out.println("Cost: " + TollCalculatorUtils.calculateCost(distance, PRICE_PER_KM));
			
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	private static Location parseInterchObj(String id, JSONObject interchange) {
		Location location = new Location();
		location.setLocationId(id);
		
		Gson gson = new Gson();
		LocationDetails locDetails = gson.fromJson(interchange.toString(), LocationDetails.class);
		location.setLocationDetails(locDetails);
		
		return location;
	}

}
