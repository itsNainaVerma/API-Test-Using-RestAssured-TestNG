import dataentities.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class serializationDeserialization {

    private static RequestSpecification ReqSpec;
    private static ResponseSpecification ResSpec;
    @BeforeTest
    public static void RequestSpecificBuilder(){
        ReqSpec = new RequestSpecBuilder().
                setBaseUri("https://zippopotam.us").
                setContentType(ContentType.JSON).
                build();
    }

    @BeforeTest
    public static void ResponseSpecificBuilder(){
        ResSpec = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                build();
    }
    
    @Test
    public void checkPlaceName_expectedBeverlyHills(){
        Location location =
                given().
                        spec(ReqSpec).log().body().
                        when().
                          get("us/90210").
                        then().log().all().
                          spec(ResSpec).
                          extract().
                            as(Location.class);  //deserialization
       // Map<String , String> firstPlace = location.getPlaces().get(0);
        Assert.assertEquals(location.getPlaces().get(0).getPlaceName(), "Beverly Hills");
    }

    @Test
    public void checkPlaceName_expectedRiga(){
        Location location = new Location();
                given().
                        body(location).  // this is serialization,where data of java object will be converted into JSON
                        spec(ReqSpec).
                        log().body().
                        when().
                        get("us/90210").
                        then().
                        log().all().
                        spec(ResSpec);
                       // extract().
                     // as(Location.class); //deserialization

        // Map<String , String> firstPlace = location.getPlaces().get(0);
        Assert.assertEquals(location.getPlaces().get(0).getPlaceName(), "Riga");  // API response will be "Beverly hills", but we updated it to "Rega" with serialization. hence it will pass.
    }
}
