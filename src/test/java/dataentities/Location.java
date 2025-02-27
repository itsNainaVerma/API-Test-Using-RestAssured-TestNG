/* the commented code is implementation of list of <maps>.
 we can also use it instead of making separate class for "Place".
 But creating list of <maps> will not be very efficient way in many cases.
 */

package dataentities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
//import java.util.HashMap;
//import java.util.Map;


public class Location {

    private String postCode;
    private String country;
    private String countryAbbreviation;
    //private List <Map <String, String>> places;
    private List<place> places;

    public Location(){
        this.postCode = "1050";
        this.country = "Lativa";
        this.countryAbbreviation = "LV";

        this.places = new ArrayList<>();
        place Place = new place();
        this.places.add(Place);
        //this.places.add(addPlaces("Riga", "1", "Rigaa", "RI", "2"));
    }

/*    public Map<String, String> addPlaces(String placeName, String longitude, String state, String stateAbbreviation, String latitude){
        Map<String, String> place = new HashMap<>();
        place.put("place name", placeName);
        place.put("longitude", longitude);
        place.put("state", state);
        place.put("state abbreviation", stateAbbreviation);
        place.put("latitude", latitude);
        return place;
    }  */

    @JsonProperty("post code")
    public String getPostCode(){
        return postCode;
    }

    @JsonProperty("post code")
    public void setPostCode(String postCode){
        this.postCode = postCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    @JsonProperty("country abbreviation")
    public void setCountryAbbreviation(String countryAbbreviation) {
        this.countryAbbreviation = countryAbbreviation;
    }

    @JsonProperty("country abbreviation")
    public String getCountryAbbreviation() {
        return countryAbbreviation;
    }

 /*   public void setPlaces(List<Map<String, String>> places) {
        this.places = places;
    }

    public List<Map<String, String>> getPlaces() {
        return places;
    } */


    public List<place> getPlaces() {
        return places;
    }

    public void setPlaces(List<place> places) {
        this.places = places;
    }
}

