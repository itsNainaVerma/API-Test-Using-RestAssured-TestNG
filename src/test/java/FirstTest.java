import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class FirstTest {

    @Test
    public void checkStatusCode_expect200(){

        given().
                when().
                get("http://zippopotam.us/IN/400001").
                then().
                assertThat().statusCode(200);

    }
    @Test
    public void logRequestAndResponseDetails(){
        given().
                log().all().
                when().
                      get("http://zippopotam.us/IN/400001").
                then().
                     log().body();

    }

}
