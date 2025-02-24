import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;

public class ParameterizingBasicAPITest {

    @DataProvider (name="place")
    public Object[][] ZipCodePlaceDetails(){
        return new Object[][]{
                {"400001", "IN", "University Road", "Maharashtra", "India"},
                {"140001", "IN", "Rup Nagar College", "Punjab", "India"},
                {"110001", "IN","Janpath", "New Delhi", "India"},
                {"400001", "IN", "Cannon Road", "Maharashtra", "India"},
                {"400001", "IN", "Fort Market", "Maharashtra", "India"},
                {"400001", "IN", "G T Hospital", "Maharashtra", "India"}

        };
    }

    @BeforeTest
    public void logRequestAndResponseDetails(){
        given().
                log().all().
                when().
                get("http://zippopotam.us/IN/400001").
                then().
                log().body();

    }

    @Test (priority = 0, dataProvider = "place")
    public void checkStatusCode_expectHttp200(String zipcode, String countryCode, String Place, String State, String Country){
        given().
                pathParams("zipcode",zipcode).
                pathParams("countryCode",countryCode).
                when().
                get("http://zippopotam.us/{countryCode}/{zipcode}").
                then().assertThat().statusCode(200);
    }

    @Test (priority = 1, dependsOnMethods = "checkStatusCode_expectHttp200", dataProvider = "place")
    public void checkContentType_expectApplicationJson(String zipcode, String countryCode, String Place, String State, String Country){
        given().pathParams("zipcode",zipcode).
                pathParams("countryCode",countryCode).
                when().
                get("http://zippopotam.us/{countryCode}/{zipcode}").
                then().
                assertThat().
                contentType(ContentType.JSON);
    }

    @Test (priority = 2, dependsOnMethods = "checkStatusCode_expectHttp200", dataProvider = "place")
    public void checkPostCodeInResponse_exceptFortMarket(String zipcode, String countryCode, String Place, String State, String Country){
        given().pathParams("zipcode",zipcode).
                pathParams("countryCode",countryCode).
                when().
                get("http://zippopotam.us/{countryCode}/{zipcode}").
                then().
                assertThat().
                body("'post code'", equalTo(zipcode));
    }

    @Test(priority = 3, dependsOnMethods = "checkStatusCode_expectHttp200", dataProvider = "place")
    public void checkStateNameInResponse_exceptMaharashtra(String zipcode, String countryCode, String Place, String State, String Country){
        given().pathParams("zipcode",zipcode).
                pathParams("countryCode",countryCode).
                when().
                get("http://zippopotam.us/{countryCode}/{zipcode}").
                then().
                assertThat().
                body("places[0].state", equalTo(State));
    }

    @Test (priority = 4, dependsOnMethods = "checkStatusCode_expectHttp200", dataProvider = "place")
    public void checkCountryInResponse_exceptIndia(String zipcode,String countryCode, String place, String State, String Country){
        given().pathParams("zipcode",zipcode).
                pathParams("countryCode",countryCode).
                when().
                get("http://zippopotam.us/{countryCode}/{zipcode}").
                then().
                assertThat().
                body("country", equalTo(Country));
    }

    @Test (priority = 5, dependsOnMethods = "checkStatusCode_expectHttp200", dataProvider = "place")
    public void checkCountryAbbreviationInResponse_exceptFortMarket(String zipcode, String countryCode, String Place, String State, String Country){
        given().pathParams("zipcode",zipcode).
                pathParams("countryCode",countryCode).
                when().
                get("http://zippopotam.us/{countryCode}/{zipcode}").
                then().
                assertThat().
                body("'country abbreviation'", equalTo(countryCode));
    }

    @Test (priority = 6, dependsOnMethods = "checkStatusCode_expectHttp200", dataProvider = "place")
    public void checkIfPlaceAvailable_exceptTrue(String zipcode, String countryCode, String Place, String State, String Country){
        given().pathParams("zipcode",zipcode).
                pathParams("countryCode",countryCode).
                when().
                get("http://zippopotam.us/{countryCode}/{zipcode}").
                then().assertThat().
                body("places.'place name'", hasItem(Place));
    }

    @Test (priority = 7, dependsOnMethods = "checkStatusCode_expectHttp200", dataProvider = "place")
    public void checkIfPlaceNotAvailable_exceptTrue(String zipcode, String countryCode, String Place, String State, String Country){
        given().pathParams("zipcode",zipcode).
                pathParams("countryCode",countryCode).
                when().
                get("http://zippopotam.us/{countryCode}/{zipcode}").
                then().assertThat().
                body("places.'place name'", not(hasItem("Mumbai")));
    }

}

