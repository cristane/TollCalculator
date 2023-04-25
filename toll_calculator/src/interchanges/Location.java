package interchanges;

public class Location {
    public String locationId;
    public LocationDetails locationDetails;
    
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public LocationDetails getLocationDetails() {
		return locationDetails;
	}
	public void setLocationDetails(LocationDetails locationDetails) {
		this.locationDetails = locationDetails;
	}
}