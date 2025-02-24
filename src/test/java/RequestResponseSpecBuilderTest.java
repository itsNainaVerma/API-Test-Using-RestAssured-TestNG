import io.restassured.builder.*;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItem;

public class RequestResponseSpecBuilderTest {

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

    @DataProvider(name="place")
    public Object[][] ZipCodePlaceDetails() {
        return new Object[][]{
                {"400001", "IN", "University Road", "Maharashtra", "India"},
                {"140001", "IN", "Rup Nagar College", "Punjab", "India"},
                {"110001", "IN", "Janpath", "New Delhi", "India"},
                {"400001", "IN", "Cannon Road", "Maharashtra", "India"},
                {"400001", "IN", "Fort Market", "Maharashtra", "India"},
                {"400001", "IN", "G T Hospital", "Maharashtra", "India"}
        };
    }

    @BeforeClass
    public void logRequestAndResponseDetails(){
        given().
                spec(ReqSpec).
                log().all().
                when().
                get("IN/400001").
                then().
                log().body();

    }

    @Test(priority = 0, dataProvider = "place")
    public void checkStatusCodeAndContentType_expectHttp200andApplicationJSON(String zipcode, String countryCode, String Place, String State, String Country){
        given().
                spec(ReqSpec).
                pathParams("zipcode",zipcode).
                pathParams("countryCode",countryCode).
                when().
                   get("{countryCode}/{zipcode}").
                then().
                    spec(ResSpec);
    }

    @Test (priority = 2, dependsOnMethods = "checkStatusCodeAndContentType_expectHttp200andApplicationJSON", dataProvider = "place")
    public void checkPostCodeInResponse_expectTrue(String zipcode, String countryCode, String Place, String State, String Country){
        given().
                spec(ReqSpec).
                pathParams("zipcode",zipcode).
                pathParams("countryCode",countryCode).
                when().
                get("/{countryCode}/{zipcode}").
                then().
                spec(ResSpec).
                and().
                assertThat().
                body("'post code'", equalTo(zipcode));
    }

    @Test(priority = 3, dependsOnMethods = "checkStatusCodeAndContentType_expectHttp200andApplicationJSON", dataProvider = "place")
    public void checkStateNameInResponse_expectTrue(String zipcode, String countryCode, String Place, String State, String Country){
        given().spec(ReqSpec).
                pathParams("zipcode",zipcode).
                pathParams("countryCode",countryCode).
                when().
                get("/{countryCode}/{zipcode}").
                then().
                spec(ResSpec).
                and().
                assertThat().
                body("places[0].state", equalTo(State));
    }

    @Test (priority = 4, dependsOnMethods = "checkStatusCodeAndContentType_expectHttp200andApplicationJSON", dataProvider = "place")
    public void checkCountryInResponse_expectTrue(String zipcode, String countryCode, String place, String State, String Country){
        given().spec(ReqSpec).
                pathParams("zipcode",zipcode).
                pathParams("countryCode",countryCode).
                when().
                get("/{countryCode}/{zipcode}").
                then().
                assertThat().
                body("country", equalTo(Country));
    }

    @Test (priority = 5, dependsOnMethods = "checkStatusCodeAndContentType_expectHttp200andApplicationJSON", dataProvider = "place")
    public void checkCountryAbbreviationInResponse_expectTrue(String zipcode, String countryCode, String Place, String State, String Country){
        given().spec(ReqSpec).
                pathParams("zipcode",zipcode).
                pathParams("countryCode",countryCode).
                when().
                get("/{countryCode}/{zipcode}").
                then().
                assertThat().
                body("'country abbreviation'", equalTo(countryCode));
    }

    @Test (priority = 6, dependsOnMethods = "checkStatusCodeAndContentType_expectHttp200andApplicationJSON", dataProvider = "place")
    public void checkIfPlaceAvailable_expectTrue(String zipcode, String countryCode, String Place, String State, String Country){
        given().spec(ReqSpec).
                pathParams("zipcode",zipcode).
                pathParams("countryCode",countryCode).
                when().
                get("/{countryCode}/{zipcode}").
                then().assertThat().
                body("places.'place name'", hasItem(Place));
    }

    @Test (priority = 7, dependsOnMethods = "checkStatusCodeAndContentType_expectHttp200andApplicationJSON", dataProvider = "place")
    public void checkIfPlaceNotAvailable_expectTrue(String zipcode, String countryCode, String Place, String State, String Country){
        given().spec(ReqSpec).
                pathParams("zipcode",zipcode).
                pathParams("countryCode",countryCode).
                when().
                get("/{countryCode}/{zipcode}").
                then().spec(ResSpec).
                and().
                assertThat().
                body("places.'place name'", not(hasItem("Mumbai")));
    }

}
