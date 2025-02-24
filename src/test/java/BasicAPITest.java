import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;

public class BasicAPITest {

    @Test (priority = 0)
    public void checkStatusCode_expectHttp200(){
        given().
                when().
                    get("http://zippopotam.us/IN/400001").
                then().assertThat().statusCode(200);
    }
    @Test (priority = 1, dependsOnMethods = "checkStatusCode_expectHttp200")
    public void checkContentType_expectApplicationJson(){
        given().
                when().
                    get("http://zippopotam.us/IN/400001").
                then().
                    assertThat().
                    contentType(ContentType.JSON);
    }

    @Test (priority = 2, dependsOnMethods = "checkStatusCode_expectHttp200")
    public void checkPlaceNameInResponse_exceptFortMarket(){
        given().
                when().
                get("http://zippopotam.us/IN/400001").
                then().
                assertThat().
                body("places[8].'place name'", equalTo("Fort Market"));
    }
    @Test (priority = 3, dependsOnMethods = "checkStatusCode_expectHttp200")
    public void checkStateNameInResponse_exceptMaharashtra(){
        given().
                when().
                get("http://zippopotam.us/IN/400001").
                then().
                assertThat().
                body("places[8].state", equalTo("Maharashtra"));
    }
    @Test (priority = 4, dependsOnMethods = "checkStatusCode_expectHttp200")
    public void checkCountryInResponse_exceptIndia(){
        given().
                when().
                get("http://zippopotam.us/IN/400001").
                then().
                assertThat().
                body("country", equalTo("India"));
    }
    @Test (priority = 5, dependsOnMethods = "checkStatusCode_expectHttp200")
    public void checkCountryAbbreviationInResponse_exceptFortMarket(){
        given().
                when().
                get("http://zippopotam.us/IN/400001").
                then().
                assertThat().
                body("'country abbreviation'", equalTo("IN"));
    }
    @Test (priority = 6, dependsOnMethods = "checkStatusCode_expectHttp200")
    public void checkIfPlaceAvailable_exceptTrue(){
        given().
                when().
                get("http://zippopotam.us/IN/400001").
                then().assertThat().
                body("places.'place name'", hasItem("Fort Street"));
    }
    @Test (priority = 7, dependsOnMethods = "checkStatusCode_expectHttp200")
    public void checkIfPlaceNotAvailable_exceptTrue(){
        given().
                when().
                get("http://zippopotam.us/IN/400001").
                then().assertThat().
                body("places.'place name'", not(hasItem("Mumbai")));
    }
    @Test (priority = 8, dependsOnMethods = "checkStatusCode_expectHttp200")
    public void checkNumberOfPlaceAvailable_except26(){
        given().
                when().
                get("http://zippopotam.us/IN/400001").
                then().assertThat().
                body("places.'place name'", hasSize(26));
    }
}

