import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class XmlResponseTest {

    private static RequestSpecification ReqSpec;
    private static ResponseSpecification ResSpec;

    @BeforeClass
    public void RequestSpecificationBuilder() {
        ReqSpec = new RequestSpecBuilder().
                setBaseUri("https://httpbin.org").
                setAccept(ContentType.XML).
                build();
    }

    @BeforeClass
    public void ResponseSpecificationBuilder() {
        ResSpec = new ResponseSpecBuilder().
                expectContentType(ContentType.XML).
                expectStatusCode(200).
                build();
    }

    @Test
    public void verifyXMLResponseTags(){
        given().spec(ReqSpec).
                log().all().
                when().
                get("/xml").
                then().log().
                body().
                spec(ResSpec).
                assertThat().
                body("slideshow.slide[1].item[2].em", equalTo("buys")).
                body("slideshow.slide[1].@type", equalTo("all")).
                body("slideshow.slide[-1].item[2]", not(equalTo("who"))).
                body("slideshow.slide.findAll{it.title='all'}", notNullValue());
    }

}
